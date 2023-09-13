package eu.europeana.api.record.model.data;

import com.fasterxml.jackson.annotation.*;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.PostLoad;
import dev.morphia.annotations.PostPersist;
import dev.morphia.annotations.Property;
import eu.europeana.api.record.model.EDMClass;
import org.bson.Document;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

/**
 * @author Hugo
 * @since 8 Aug 2023
 */
@Entity(discriminator = "LocalReference", discriminatorKey = "type")
@JsonPropertyOrder({ID, TYPE})
public class LocalReference extends DataValue implements ObjectReference {

    @JsonProperty(ID)
    @Property(ID)
    protected String id;

    @JsonUnwrapped
    @Property(MONGO_OBJECT)
    protected EDMClass object;

    public LocalReference() {
    }

    public LocalReference(String id) {
        this.id = id;
        this.object = null;
    }

    public LocalReference(EDMClass object) {
        this.id = object.getID();
        this.object = object;
    }


    public String getID() {
        return this.id;
    }

    public boolean isLocal() {
        return true;
    }

    public boolean isDereferenced() {
        return this.object != null;
    }

    public EDMClass getDereferencedObject() {
        return (EDMClass) this.object;
    }

    @PostPersist
    public void postPersist(Document doc) {
        if (this.object != null) {
            doc.remove(ID);
        }
    }

    @PostLoad
    public void postLoad(Document doc) {
        if (this.object != null) {
            this.id = this.object.getID();
        }
    }
}
