package eu.europeana.api.record.model.data;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import dev.morphia.Datastore;
import dev.morphia.annotations.*;
import eu.europeana.api.record.model.EDMObject;
import org.bson.Document;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

/**
 * @author Hugo
 * @since 8 Aug 2023
 */
@Entity(discriminator = "SharedReference", discriminatorKey = "type")
@JsonPropertyOrder({ID, TYPE})
public class SharedReference extends DataValue implements ObjectReference {

    @Property(value = ID)
    protected String id;

    @Reference(value = MONGO_OBJECT, lazy = false)
    protected EDMObject object;


    public SharedReference() {
    }

    public SharedReference(String id) {
        this.id = id;
    }

    public SharedReference(EDMObject object) {
        this.id = object.getID();
        this.object = object;
    }


    public String getID() {
        return this.id;
    }

    @JsonIgnore
    public boolean isLocal() {
        return (this.id.startsWith("#") || this.id.startsWith("\\"));
    }

    @JsonIgnore
    public boolean isDereferenced() {
        return this.object != null;
    }

    @JsonUnwrapped
    public EDMObject getDereferencedObject() {
        return (EDMObject) this.object;
    }

    @JsonGetter(TYPE)
    public String getType() {
        return "SharedReference";
    }

    @PrePersist
    public void prePersist(Document doc, Datastore ds) {
        if (this.object != null) {
            ds.save(this.object);
        }
    }

    @PostLoad
    public void postLoad(Document document, Datastore ds) {
        if (this.object != null) {
            this.id = this.object.getID();
        }
    }
}
