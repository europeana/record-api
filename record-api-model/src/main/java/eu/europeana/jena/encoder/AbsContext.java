/**
 * 
 */
package eu.europeana.jena.encoder;

import eu.europeana.jena.encoder.library.ClassTemplate.FieldDefinition;
import eu.europeana.jena.encoder.library.ClassTemplate.PropertyDefinition;
import eu.europeana.jena.encoder.library.ClassTemplate.ReflectionDefinition;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import java.lang.reflect.Field;

/**
 * @author Hugo
 * @since 24 Oct 2023
 */
public abstract class AbsContext
{
    protected Resource             resource;
    protected Property             property;
    protected ReflectionDefinition definition;

    public AbsContext() { this(null, null, null); }

    public AbsContext(Resource resource) {
        this(resource, null, null);
    }

    public AbsContext(Resource resource, Property property) {
        this(resource, property, null);
    }

    public AbsContext(Resource resource, Property property
                    , ReflectionDefinition def) {
        this.resource   = resource;
        this.property   = property;
        this.definition = def;
    }

    public Resource getResource() { return this.resource; }

    public String   getURI() {
        if ( this.resource.isAnon() ) {
            return this.resource.getId().getLabelString();
        }
        return this.resource.getURI();
    }
    

    public Property getProperty() { return this.property; }
    public boolean  hasProperty() { return (this.property != null); }

    public PropertyDefinition getPropertyDefinition() {
        return this.definition.getPropertyDefinition();
    }

    public ReflectionDefinition getDefinition() { return this.definition;         }
    //public Field           getField()           { return this.field.getField(); }
}
