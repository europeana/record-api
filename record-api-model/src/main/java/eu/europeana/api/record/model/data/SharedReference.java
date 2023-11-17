package eu.europeana.api.record.model.data;

import com.fasterxml.jackson.annotation.*;
import dev.morphia.Datastore;
import dev.morphia.annotations.*;
import dev.morphia.query.filters.Filters;
import eu.europeana.api.edm.RDF;
import eu.europeana.api.record.model.EDMClass;
import org.bson.Document;

import static eu.europeana.api.record.model.ModelConstants.ID;
import static eu.europeana.api.record.model.ModelConstants.SHARED;
import static eu.europeana.api.record.model.ModelConstants.OBJECT;

/**
 * @author Hugo
 * @since 8 Aug 2023
 */
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@Entity(discriminator = SHARED, discriminatorKey = RDF.type)
public class SharedReference implements ObjectReference {

    @Property(ID)
    protected String id;

    @Reference(value = OBJECT, lazy = true)
    protected EDMClass object;


    public SharedReference() {}

    public SharedReference(String id) {
        this.id = id;
    }

    public SharedReference(EDMClass object) {
        this.id     = object.getID();
        this.object = object;
    }


    public String getID() { return this.id; }

    public boolean isLocal() {
        return (this.id.startsWith("#") || this.id.startsWith("\\"));
    }

    public boolean isDereferenced() { return (this.object != null); }

    public EDMClass getDereferencedObject() { return this.object; }

    public String toString() { return ("<" + id + ">"); }

    @PrePersist
    public void prePersist(Document doc, Datastore ds) {
        if ( this.object != null ) {
            EDMClass o = this.object;

            EDMClass o2 = ds.find(o.getClass())
                    .filter(Filters.eq(ID, o.getID()))
                    .first();
            if ( o2 == null ) { ds.save(o); }
            else { this.object = o2; }

//              (UpdateOperators.set("id", o.getID())).execute(new ModifyOptions()
//                .returnDocument(ReturnDocument.AFTER));
//            ds.getMapper().getCollection(o.getClass());

//            ds.find(o.getClass()).filter(Filters.eq("id", o.getID())).ficount();
//            if ( count  > 0 ) { return; }

        }
    }

    @PostLoad
    public void postLoad(Document document, Datastore ds)
    {
        if ( this.object != null ) { this.id = this.object.getID(); }
    }
}