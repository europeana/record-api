package eu.europeana.api.record.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.PostLoad;
import dev.morphia.annotations.PostPersist;
import dev.morphia.annotations.Property;
import eu.europeana.api.record.model.EDMObject;
import org.bson.Document;

import static eu.europeana.api.record.vocabulary.RecordFields.ID;
import static eu.europeana.api.record.vocabulary.RecordFields.MONGO_OBJECT;

/**
 * @author Hugo
 * @since 8 Aug 2023
 */
@Entity(discriminator = "LocalReference", discriminatorKey = "type")
public class LocalReference extends DataValue implements ObjectReference {

    @Property(ID)
    protected String id;

    @Property(MONGO_OBJECT)
    protected EDMObject object;

    public LocalReference() {
    }

    public LocalReference(String id) {
        this.id = id;
        this.object = null;
    }

    public LocalReference(EDMObject object) {
        this.id = object.getID();
        this.object = object;
    }


    public String getID() {
        return this.id;
    }

    @JsonIgnore
    public boolean isLocal() {
        return true;
    }

    @JsonIgnore
    public boolean isDereferenced() {
        return this.object != null;
    }

    @JsonUnwrapped
    public EDMObject getDereferencedObject() {
        return (EDMObject) this.object;
    }

    @PostPersist
    public void postPersist(Document doc) {
        if (this.object != null) {
            doc.remove("id");
        }
    }

    @PostLoad
    public void postLoad(Document doc) {
        if (this.object != null) {
            this.id = this.object.getID();
        }
    }
}
