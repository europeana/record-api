package eu.europeana.jena.encoder.library;

import eu.europeana.api.edm.NamespaceDeclaration;
import eu.europeana.jena.encoder.JenaDecoderException;
import eu.europeana.jena.encoder.JenaEncoderException;
import eu.europeana.jena.encoder.codec.JenaCodec;
import eu.europeana.jena.encoder.library.ClassTemplate.FieldDefinition;
import eu.europeana.jena.encoder.library.ClassTemplate.ReflectionDefinition;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hugo
 * @since 15 Oct 2023
 */
public class ClassTemplate {

    public static abstract class ReflectionDefinition {

        protected PropertyDefinition property;
        protected JenaCodec<?>       codec;
        protected boolean            isCollection;

        public ReflectionDefinition(PropertyDefinition property
                                  , JenaCodec<?> codec, boolean isCollection) {
            this.property     = property;
            this.codec        = codec;
            this.isCollection = isCollection;
        }

        public JenaCodec<?> getCodec() { return this.codec; }

        public PropertyDefinition getPropertyDefinition() { return property; }

        public Property getProperty() { 
            return ( property != null ? property.getProperty() : null ); 
        }

        public boolean hasPropertyDefinition() { return (property != null); }

        public boolean isCollection() { return isCollection; }


        public abstract boolean merge(ReflectionDefinition def);

        public abstract boolean acceptsRead();

        public abstract boolean acceptsWrite();

        public abstract Type getTargetType();

        public abstract <T> T getValue(Object o);

        public abstract void setValue(Object o, Object value);
    }

    public static class FieldDefinition extends ReflectionDefinition {

        protected Field field;

        public FieldDefinition(Field field, PropertyDefinition property
                             , JenaCodec<?> codec, boolean isCollection) {
            super(property, codec, isCollection);
            this.field = field;
            field.setAccessible(true);
        }

        public Field     getField() { return this.field; }

        public String toString() { return this.field.toString(); }


        @Override
        public boolean merge(ReflectionDefinition def) { return false; }

        @Override
        public boolean acceptsRead() { return true; }

        @Override
        public boolean acceptsWrite() { return true; }

        @Override
        public Type getTargetType() { 
            return field.getGenericType();
        }

        @Override
        public <T> T getValue(Object o) {
            try {
                return (T)this.field.get(o);
            }
            catch (IllegalArgumentException | IllegalAccessException e) {
                throw new JenaEncoderException(e);
            }
        }

        @Override
        public void setValue(Object o, Object value) {
            try {
                field.set(o, value);
            }
            catch (IllegalArgumentException | IllegalAccessException e) {
                throw new JenaDecoderException(e);
            }
        }
    }

    public static class MethodDefinition extends ReflectionDefinition {

        protected Method readMethod;

        protected Method writeMethod;

        public MethodDefinition(Method readMethod, Method writeMethod
                              , PropertyDefinition property
                              , JenaCodec<?> codec, boolean isCollection) {
            super(property, codec, isCollection);
            this.readMethod  = readMethod;
            this.writeMethod = writeMethod;
        }

        public Method getReadMethod()  { return this.readMethod; }

        public Method getWriteMethod() { return this.writeMethod; }

        public String toString() { 
            return "(read=" 
                  + (readMethod == null ? "none" : readMethod.toString())
                  + ",write="
                  + (writeMethod == null ? "none" : writeMethod.toString())
                  + ")";
        }

        @Override
        public boolean merge(ReflectionDefinition def) {
            if ( def instanceof FieldDefinition ) { return false; }

            MethodDefinition mdef = (MethodDefinition)def;
            if ( mdef.readMethod != null && this.readMethod == null ) {
                this.readMethod = mdef.readMethod;
            }
            else { return false; }

            if ( mdef.writeMethod != null && this.writeMethod == null ) {
                this.writeMethod = mdef.writeMethod;
            }
            else { return false; }

            return true;
        }

        @Override
        public boolean acceptsRead() { return readMethod != null; }

        @Override
        public boolean acceptsWrite() { return writeMethod != null; }

        @Override
        public Type getTargetType() { 
            if ( writeMethod == null ) { return null; }
            Type[] types = writeMethod.getGenericParameterTypes();
            return ( types.length > 0 ? types[0] : null );
        }

        @Override
        public <T> T  getValue(Object o) {
            try {
                return (T)this.readMethod.invoke(o);
            }
            catch (IllegalArgumentException | IllegalAccessException 
                 | InvocationTargetException e) {
                throw new JenaEncoderException(e);
            }
        }

        @Override
        public void setValue(Object o, Object value) {
            try {
                this.writeMethod.invoke(o, value);
            }
            catch (IllegalAccessException | InvocationTargetException e) {
                throw new JenaDecoderException(e);
            }            
        }
    }

    public static class PropertyDefinition {

        protected NamespaceDeclaration namespace;
        protected Property             property;
        protected boolean              inverse;

        public PropertyDefinition(Property property
                                , NamespaceDeclaration namespace
                                , boolean inverse) {
            this.property  = property;
            this.namespace = namespace;
            this.inverse   = inverse;
        }

        public Property getProperty() { return this.property; }

        public NamespaceDeclaration getNamespace() { return this.namespace; }

        public boolean isInverse() { return this.inverse; }

        public String toString() { return this.property.toString(); }
    }
    
    public static class ResourceDefinition {

        protected NamespaceDeclaration namespace;
        protected Resource             resource;

        public ResourceDefinition(Resource resource
                                , NamespaceDeclaration namespace) {
            this.resource  = resource;
            this.namespace = namespace;
        }

        public NamespaceDeclaration getNamespace() { return this.namespace; }

        public Resource             getResource()  { return this.resource;  }
    }

    protected Class<?>           clazz;
    protected Constructor<?>     contructor;
    protected JenaCodec<?>       codec;
    protected FieldDefinition    id;
    protected ResourceDefinition type;
    protected ArrayList<ReflectionDefinition> defs = new ArrayList<>();

    public JenaCodec<?>          getCodec()  { return this.codec;  }
    public FieldDefinition       getId()     { return this.id;     }
    public ResourceDefinition    getType()   { return this.type;   }

    public List<ReflectionDefinition> getDefinitions() { return this.defs; }

    public ReflectionDefinition getDefinition(Property prop) { 
        if ( prop == null ) { return null; }

        for ( ReflectionDefinition def : this.defs ) {
            if ( prop.equals(def.getProperty()) ) { return def; }
        }
        return null;
    }

    public <T> T newObject() {
        try {
            return ((Constructor<T>)this.contructor).newInstance();
        }
        catch (InstantiationException | IllegalAccessException
             | InvocationTargetException e) {
            throw new JenaDecoderException(e);
        }
    }

    public boolean acceptsClass(Class<?> clazz) {
        return clazz.isAssignableFrom(this.clazz);
    }

    public boolean registerDefinition(ReflectionDefinition  def) {
        if ( !def.hasPropertyDefinition() ) {
            this.defs.add(def);
            return true;
        }
        ReflectionDefinition existing = getDefinition(def.getProperty());
        if ( existing == null ) {
            this.defs.add(def);
            return true;
        }
        return existing.merge(def);
    }

    public String toString() {
        return ("Template<" + this.clazz.getName() + ">");
    }
}
