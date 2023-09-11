package eu.europeana.api.record.model.data;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.PostLoad;
import dev.morphia.annotations.PostPersist;
import dev.morphia.annotations.Property;
import eu.europeana.api.record.model.EDMObject;
import org.bson.Document;

/**
 * @author Hugo
 * @since 8 Aug 2023
 */
@Entity(discriminator = "LocalReference", discriminatorKey = "type")
public class LocalReference extends DataValue implements ObjectReference {
    @Property("id")
    protected String id;

    @Property("object")
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

    public boolean isLocal() {
        return true;
    }

    public boolean isDereferenced() {
        return this.object != null;
    }

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
