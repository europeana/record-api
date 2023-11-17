/**
 * 
 */
package eu.europeana.jena.encoder.library;

import eu.europeana.api.edm.NamespaceDeclaration;
import eu.europeana.api.edm.NamespaceResolver;
import eu.europeana.jena.encoder.annotation.*;
import eu.europeana.jena.encoder.codec.CodecRegistry;
import eu.europeana.jena.encoder.codec.JenaCodec;
import eu.europeana.jena.encoder.codec.JenaResourceCodec;
import eu.europeana.jena.encoder.library.ClassTemplate.FieldDefinition;
import eu.europeana.jena.encoder.library.ClassTemplate.PropertyDefinition;
import eu.europeana.jena.encoder.library.ClassTemplate.ResourceDefinition;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.RDF;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.apache.jena.rdf.model.ResourceFactory.*;

/**
 * @author Hugo
 * @since 15 Oct 2023
 */
public class TemplateLibrary
{
    private CodecRegistry                registry;
    private NamespaceResolver            nsResolver;
    private Map<Class<?>, ClassTemplate> templatePerClass;
    private Map<String  , ClassTemplate> templatePerType;
    private ResourceUriNormalizer        uriNormalizer;

    public TemplateLibrary(CodecRegistry registry, NamespaceResolver nsResolver
                         , ResourceUriNormalizer uriNormalizer) {
        this.registry      = registry;
        this.nsResolver    = nsResolver;
        templatePerClass   = new HashMap();
        templatePerType    = new HashMap();
        this.uriNormalizer = uriNormalizer;
    }

    public TemplateLibrary(CodecRegistry registry, NamespaceResolver nsResolver) {
        this(registry, nsResolver, DefaultUriNormalizer.INSTANCE);
    }

    public CodecRegistry getCodecRegistry() { return registry; }

    public ResourceUriNormalizer getUriNormalizer() { return uriNormalizer; }

    public Collection<ClassTemplate> getTemplates() {
        return templatePerClass.values();
    }

    public ClassTemplate getTemplateByClass(Class<?> clazz) {
        return this.templatePerClass.get(clazz);
    }

    public ClassTemplate getTemplateByType(Resource type) {
        return this.templatePerType.get(type.getURI());
    }

    public ClassTemplate getTemplateForResource(Resource r) {
        StmtIterator iter = r.listProperties(RDF.type);
        try {
            while ( iter.hasNext() ) {
                RDFNode type = iter.next().getObject();
                if ( !type.isResource() ) { continue; }
    
                ClassTemplate template = getTemplateByType(type.asResource());
                if ( template != null ) { return template; }
            }
            return null;
        }
        finally { iter.close(); }
    }

    public JenaCodec getCodec(Class<?> clazz)
    {
        return (JenaCodec)registry.get(clazz);
    }

    public JenaCodec getCodecRecursively(Class<?> clazz)
    {
        return (JenaCodec)registry.getRecursively(clazz);
    }

    public JenaCodec getCodec(Object o)
    {
        return getCodec(o.getClass());
    }

    public void importClass(Class<?> clazz)
    {
        ClassTemplate template = new ClassTemplate();
        template.contructor = getConstructor(clazz);
        templatePerClass.put(clazz, template);

        JenaCodec codec = processCodec(clazz);
        if ( codec == null ) { codec = new JenaResourceCodec(template); }
        template.codec = codec;

        if (clazz.isAnnotationPresent(JenaResource.class)) {
            template.type = getType(clazz.getAnnotation(JenaResource.class));
            template.id   = getID(clazz);
            template.id.setAccessible(true);
            templatePerType.put(template.type.resource.getURI(), template);
        }
        processFields(template, clazz);
    }

    private <T> Constructor<T> getConstructor(Class<T> clazz) {
        try {
            return clazz.getDeclaredConstructor();
        }
        catch (IllegalArgumentException | NoSuchMethodException e) {
            throw new JenaTemplateCompilerException(e);
        }
    }

    private void processFields(ClassTemplate template, Class<?> clazz)
    {
        for (Field field : clazz.getDeclaredFields())
        {
            JenaProperty        prop       = field.getAnnotation(JenaProperty.class);
            boolean             transitive = field.isAnnotationPresent(JenaTransitive.class);
            boolean             hasCodec   = field.isAnnotationPresent(eu.europeana.jena.encoder.annotation.JenaCodec.class);
            if ( prop == null && !transitive && !hasCodec ) { continue; }

            field.setAccessible(true);

            PropertyDefinition definition = getPropertyDefinition(prop);

            template.fields.add(new FieldDefinition(field, definition
                                                  , processCodec(field)
                                                  , isCollection(field)));
        }
        Class<?> parent = clazz.getSuperclass();
        if ( parent != null ) { processFields(template, parent); }
    }

    private boolean isCollection(Field field)
    {
        if (field.isAnnotationPresent(JenaCollection.class)) { return true; }

        Class<?> type = field.getType();
        return ( Collection.class.isAssignableFrom(type) || type.isArray() );
    }

    private JenaCodec processCodec(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(eu.europeana.jena.encoder.annotation.JenaCodec.class)) { return null; }
//            return (JenaCodec)registry.getRecursively(clazz);

        eu.europeana.jena.encoder.annotation.JenaCodec c 
            = clazz.getAnnotation(eu.europeana.jena.encoder.annotation.JenaCodec.class);
        try {
            return (JenaCodec)c.using().getDeclaredConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException
             | IllegalArgumentException | NoSuchMethodException
             | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JenaCodec processCodec(Field f) {
        if (!f.isAnnotationPresent(eu.europeana.jena.encoder.annotation.JenaCodec.class)) { return null; }

        eu.europeana.jena.encoder.annotation.JenaCodec c 
            = f.getAnnotation(eu.europeana.jena.encoder.annotation.JenaCodec.class);
        try {
            return (JenaCodec)c.using().getDeclaredConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException
             | IllegalArgumentException | NoSuchMethodException
             | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Field getID(Class<?> clazz)
    {
        for (Field f : clazz.getDeclaredFields()) {
            if (f.isAnnotationPresent(JenaId.class)) { return f; }
        }
        Class<?> parent = clazz.getSuperclass();
        return ( parent == null ? null : getID(parent) );
    }

    private ResourceDefinition getType(JenaResource r)
    {
        String ns = r.ns();
        String ln = r.localName();
        if ( ns == null || ln == null ) { return null; }
        Resource type = createResource(ns + ln);

        NamespaceDeclaration decl = nsResolver.getDeclarationByNamespace(ns);
        if ( decl == null ) { 
            throw new JenaTemplateCompilerException("Undeclared namespace: " + ns);
        }
        return new ResourceDefinition(type, decl);
    }

    private PropertyDefinition getPropertyDefinition(JenaProperty p)
    {
        if ( p == null ) { return null; }
        String ns = p.ns();
        String ln = p.localName();
        if ( ns == null || ln == null ) { return null; }
        Property prop = createProperty(ns + ln);
        
        NamespaceDeclaration decl = nsResolver.getDeclarationByNamespace(ns);
        if ( decl == null ) { 
            throw new JenaTemplateCompilerException("Undeclared namespace: " + ns);
        }
        return new PropertyDefinition(prop, decl, p.inverse());
    }
}
