package eu.europeana.api.record.model.data;

import com.fasterxml.jackson.annotation.*;
import dev.morphia.Datastore;
import dev.morphia.annotations.*;
import eu.europeana.api.record.model.EDMClass;
import org.bson.Document;

import static eu.europeana.api.record.vocabulary.RecordFields.ID;
import static eu.europeana.api.record.vocabulary.RecordFields.MONGO_OBJECT;

/**
 * @author Hugo
 * @since 8 Aug 2023
 */
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@Entity(discriminator = "SharedReference", discriminatorKey = "type")
public class SharedReference extends DataValue implements ObjectReference {

    @Property(ID)
    protected String id;

    // with lazy we don't get the objects
    @Reference(value = MONGO_OBJECT, lazy = false)
    protected EDMClass      object;


    public SharedReference() {}

    public SharedReference(String id)
    {
        this.id = id;
    }

    public SharedReference(EDMClass object)
    {
        this.id     = object.getID();
        this.object = object;
    }


    public String getID()
    {
        return this.id;
    }

    public boolean isLocal()
    {
        return (this.id.startsWith("#") || this.id.startsWith("\\"));
    }

    public boolean isDereferenced()
    {
        return this.object != null;
    }

    public EDMClass getDereferencedObject()
    {
        return this.object;
    }

    @PrePersist
    public void prePersist(Document doc, Datastore ds) {
        if ( this.object != null ) {
            ds.save(this.object);
        }
    }

    @PostLoad
    public void postLoad(Document document, Datastore ds) {
        if ( this.object != null ) {
            this.id = this.object.getID();
        }
    }
}
