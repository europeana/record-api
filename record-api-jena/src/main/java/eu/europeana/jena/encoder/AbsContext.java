/**
 * 
 */
package eu.europeana.jena.encoder;

import eu.europeana.jena.encoder.library.ClassTemplate.FieldDefinition;
import eu.europeana.jena.encoder.library.ClassTemplate.PropertyDefinition;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import java.lang.reflect.Field;

/**
 * @author Hugo
 * @since 24 Oct 2023
 */
public abstract class AbsContext
{
    protected Resource        resource;
    protected Property        property;
    protected FieldDefinition field;

    public AbsContext() { this(null, null, null); }

    public AbsContext(Resource resource) {
        this(resource, null, null);
    }

    public AbsContext(Resource resource, Property property) {
        this(resource, property, null);
    }

    public AbsContext(Resource resource, Property property
                    , FieldDefinition field) {
        this.resource = resource;
        this.property = property;
        this.field    = field;
    }

    public Resource getResource() { return this.resource; }

    public Property getProperty() { return this.property; }
    public boolean  hasProperty() { return (this.property != null); }

    public PropertyDefinition getPropertyDefinition() {
        return this.field.getPropertyDefinition();
    }

    public FieldDefinition getFieldDefinition() { return this.field;            }
    public Field           getField()           { return this.field.getField(); }
}
