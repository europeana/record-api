package eu.europeana.jena.encoder.library;

import eu.europeana.api.edm.NamespaceDeclaration;
import eu.europeana.api.edm.NamespaceResolver;
import eu.europeana.jena.encoder.annotation.*;
import eu.europeana.jena.encoder.codec.CodecRegistry;
import eu.europeana.jena.encoder.codec.JenaCodec;
import eu.europeana.jena.encoder.codec.JenaResourceCodec;
import eu.europeana.jena.encoder.library.ClassTemplate.FieldDefinition;
import eu.europeana.jena.encoder.library.ClassTemplate.MethodDefinition;
import eu.europeana.jena.encoder.library.ClassTemplate.PropertyDefinition;
import eu.europeana.jena.encoder.library.ClassTemplate.ReflectionDefinition;
import eu.europeana.jena.encoder.library.ClassTemplate.ResourceDefinition;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.RDF;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.apache.jena.rdf.model.ResourceFactory.createProperty;
import static org.apache.jena.rdf.model.ResourceFactory.createResource;

/**
 * @author Hugo
 * @since 15 Oct 2023
 */
public class TemplateLibrary {
    private CodecRegistry registry;
    private NamespaceResolver nsResolver;
    private Map<Class<?>, ClassTemplate> templatePerClass;
    private Map<String, ClassTemplate> templatePerType;
    private ResourceUriNormalizer uriNormalizer;

    public TemplateLibrary(CodecRegistry registry, NamespaceResolver nsResolver
            , ResourceUriNormalizer uriNormalizer) {
        this.registry = registry;
        this.nsResolver = nsResolver;
        templatePerClass = new HashMap();
        templatePerType = new HashMap();
        this.uriNormalizer = uriNormalizer;
    }

    public CodecRegistry getCodecRegistry() {
        return registry;
    }

    public ResourceUriNormalizer getUriNormalizer() {
        return uriNormalizer;
    }

    public Collection<ClassTemplate> getTemplates() {
        return templatePerClass.values();
    }

    public ClassTemplate getTemplateByClass(Class<?> clazz) {
        return this.templatePerClass.get(clazz);
    }

    public ClassTemplate getTemplateByType(Type type) {
        return getTemplateByClass(getClassByType(type));
    }

    public ClassTemplate getTemplateByClassRecursively(Class<?> clazz) {

        ClassTemplate template = this.templatePerClass.get(clazz);
        if ( template != null ) { return template; }

        for ( Class c : clazz.getInterfaces() )
        {
            template = this.templatePerClass.get(c);
            if ( template != null ) { return template; }
        }

        Class parent = clazz.getSuperclass();
        return ( parent == null ? null : getTemplateByClassRecursively(parent) );

    }

    public ClassTemplate getTemplateByType(Resource type) {
        return this.templatePerType.get(type.getURI());
    }

    public ClassTemplate getTemplateForResource(Resource r) {
        StmtIterator iter = r.listProperties(RDF.type);
        try {
            while (iter.hasNext()) {
                RDFNode type = iter.next().getObject();
                if (!type.isResource()) {
                    continue;
                }

                ClassTemplate template = getTemplateByType(type.asResource());
                if (template != null) {
                    return template;
                }
            }
            return null;
        } finally {
            iter.close();
        }
    }

    public JenaCodec getCodec(Class<?> clazz) {
        return (JenaCodec) registry.get(clazz);
    }

    public JenaCodec getCodecRecursively(Class<?> clazz) {
        return (JenaCodec) registry.getRecursively(clazz);
    }

    public JenaCodec getCodecRecursively(Type type) {
        return getCodecRecursively(getClassByType(type));
    }

    public JenaCodec getCodec(Object o) {
        return getCodec(o.getClass());
    }

    public void importClass(Class<?> clazz) {
        ClassTemplate template = new ClassTemplate();
        template.clazz = clazz;
        template.contructor = getConstructor(clazz);
        templatePerClass.put(clazz, template);

        JenaCodec codec = processCodec(clazz);
        if (codec == null) {
            codec = new JenaResourceCodec(template);
        }
        template.codec = codec;

        if (clazz.isAnnotationPresent(JenaResource.class)) {
            template.type = getType(clazz.getAnnotation(JenaResource.class));
            template.id = getID(clazz);
            templatePerType.put(template.type.resource.getURI(), template);
        }
        processFields(template, clazz);
        processMethods(template, clazz);
    }

    private Class<?> getClassByType(Type type) {
        return (Class<?>)( type instanceof ParameterizedType ?
                ((ParameterizedType)type).getRawType()
                : type );
    }


    private <T> Constructor<T> getConstructor(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        } catch (IllegalArgumentException | NoSuchMethodException e) {
            throw new JenaTemplateCompilerException(e);
        }
    }

    private void processFields(ClassTemplate template, Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            JenaProperty prop = field.getAnnotation(JenaProperty.class);
            boolean transitive = field.isAnnotationPresent(JenaTransitive.class);
            boolean hasCodec = field.isAnnotationPresent(eu.europeana.jena.encoder.annotation.JenaCodec.class);
            if (prop == null && !transitive && !hasCodec) {
                continue;
            }

            ReflectionDefinition def = newFieldDefinition(field, prop);
            if ( !template.registerDefinition(def) ) {
                throw new JenaTemplateCompilerException(
                        "Overlapping annotation for property: " 
                      + def.getPropertyDefinition().getProperty());
            }
        }
        Class<?> parent = clazz.getSuperclass();
        if (parent != null) {
            processFields(template, parent);
        }
    }

    private void processMethods(ClassTemplate template, Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            JenaProperty prop = method.getAnnotation(JenaProperty.class);
            boolean transitive = method.isAnnotationPresent(JenaTransitive.class);
            boolean hasCodec = method.isAnnotationPresent(eu.europeana.jena.encoder.annotation.JenaCodec.class);
            if (prop == null && !transitive && !hasCodec) {
                continue;
            }

            ReflectionDefinition def = newMethodDefinition(method, prop);
            if ( !template.registerDefinition(def) ) {
                throw new JenaTemplateCompilerException(
                        "Overlapping annotation for property: " 
                      + def.getPropertyDefinition().getProperty());
            }
        }
        Class<?> parent = clazz.getSuperclass();
        if (parent != null) {
            processMethods(template, parent);
        }
    }

    private FieldDefinition newFieldDefinition(Field field, JenaProperty prop) {
        return new FieldDefinition(field, getPropertyDefinition(prop)
                                 , processCodec(field), isCollection(field));
    }

    private MethodDefinition newMethodDefinition(Method m, JenaProperty prop) {
        PropertyDefinition def = getPropertyDefinition(prop);

        Method readMethod  = isReadMethod(m) ? m : null;
        Method writeMethod = isWriteMethod(m) ? m : null;
        if ( readMethod == null && writeMethod == null ) {
            throw new JenaTemplateCompilerException("Unrecognised method type: " + m);
        }
        return new MethodDefinition(readMethod, writeMethod, def, processCodec(m)
                                  , isCollection(readMethod, writeMethod));
    }

    private boolean isReadMethod(Method m) {
        return ( m.getReturnType() != null && m.getParameterCount() == 0 );
    }

    private boolean isWriteMethod(Method m) {
        return ( m.getReturnType() == null && m.getParameterCount() == 1 );
    }

    private boolean isCollection(Field field) {
        return ( field.isAnnotationPresent(JenaCollection.class) 
              || isCollection(field.getType()) );
    }

    private boolean isCollection(Method readMethod, Method writeMethod) {
        if ( readMethod != null ) {
            return ( readMethod.isAnnotationPresent(JenaCollection.class) 
                    || isCollection(readMethod.getReturnType()) );
            
        }
        if ( writeMethod != null ) {
            return ( writeMethod.isAnnotationPresent(JenaCollection.class) 
                    || isCollection(writeMethod.getParameterTypes()[0]) );
        }
        return false;
    }

    private boolean isCollection(Class<?> clazz) {
        return (Collection.class.isAssignableFrom(clazz) || clazz.isArray());
    }

    private JenaCodec<?> processCodec(AnnotatedElement elem) {
        if (!elem.isAnnotationPresent(eu.europeana.jena.encoder.annotation.JenaCodec.class)) {
            return null;
        }

        eu.europeana.jena.encoder.annotation.JenaCodec c
                = elem.getAnnotation(eu.europeana.jena.encoder.annotation.JenaCodec.class);
        try {
            return (JenaCodec<?>) c.using().getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException
                | IllegalArgumentException | NoSuchMethodException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private FieldDefinition getID(Class<?> clazz) {
        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(JenaId.class)) {
                return new FieldDefinition(f, null, null, false);
            }
        }
        Class<?> parent = clazz.getSuperclass();
        return (parent == null ? null : getID(parent));
    }

    private ResourceDefinition getType(JenaResource r) {
        String ns = r.ns();
        String ln = r.localName();
        if (ns == null || ln == null) {
            return null;
        }
        Resource type = createResource(ns + ln);

        NamespaceDeclaration decl = nsResolver.getDeclarationByNamespace(ns);
        if (decl == null) {
            throw new JenaTemplateCompilerException("Undeclared namespace: " + ns);
        }
        return new ResourceDefinition(type, decl);
    }

    private PropertyDefinition getPropertyDefinition(JenaProperty p) {
        if (p == null) {
            return null;
        }
        String ns = p.ns();
        String ln = p.localName();
        if (ns == null || ln == null) {
            return null;
        }
        Property prop = createProperty(ns + ln);

        NamespaceDeclaration decl = nsResolver.getDeclarationByNamespace(ns);
        if (decl == null) {
            throw new JenaTemplateCompilerException("Undeclared namespace: " + ns);
        }
        return new PropertyDefinition(prop, decl, p.inverse());
    }
}
