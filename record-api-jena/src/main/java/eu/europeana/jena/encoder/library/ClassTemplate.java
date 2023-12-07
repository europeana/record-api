package eu.europeana.jena.encoder.library;

import eu.europeana.api.edm.NamespaceDeclaration;
import eu.europeana.jena.encoder.JenaDecoderException;
import eu.europeana.jena.encoder.codec.JenaCodec;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hugo
 * @since 15 Oct 2023
 */
public class ClassTemplate {

    public static class FieldDefinition {
        protected Field              field;
        protected PropertyDefinition property;
        protected JenaCodec          codec;
        protected boolean            isCollection;

        public FieldDefinition(Field field, PropertyDefinition property
                             , JenaCodec codec, boolean isCollection) {
            this.field        = field;
            this.property     = property;
            this.codec        = codec;
            this.isCollection = isCollection;
        }

        public Field     getField() { return this.field; }

        public JenaCodec getCodec() { return this.codec; }

        public PropertyDefinition getPropertyDefinition() { return property; }

        public boolean hasPropertyDefinition() { return (property != null); }

        public boolean isCollection() { return isCollection; }
       
        public String toString() { return this.field.toString(); }
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
    protected JenaCodec          codec;
    protected Field              id;
    protected ResourceDefinition type;
    protected ArrayList<FieldDefinition> fields = new ArrayList();

    public JenaCodec             getCodec()  { return this.codec;  }
    public Field                 getId()     { return this.id;     }
    public ResourceDefinition    getType()   { return this.type;   }
    public List<FieldDefinition> getFields() { return this.fields; }

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

    public String toString() {
        return ("Template<" + this.clazz.getName() + ">");
    }

}
