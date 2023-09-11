/**
 *
 */
package eu.europeana.api.record.model.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.morphia.Datastore;
import dev.morphia.annotations.*;
import eu.europeana.api.record.model.EDMObject;
import org.bson.Document;

/**
 * @author Hugo
 * @since 8 Aug 2023
 */
@Entity(discriminator = "SharedReference", discriminatorKey = "type")
public class SharedReference extends DataValue implements ObjectReference {
    @Property(value = "id")
    protected String id;

    @Reference(value = "object", lazy = false)
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

    public boolean isLocal() {
        return (this.id.startsWith("#") || this.id.startsWith("\\"));
    }

    public boolean isDereferenced() {
        return this.object != null;
    }

    public EDMObject getDereferencedObject() {
        return (EDMObject) this.object;
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
