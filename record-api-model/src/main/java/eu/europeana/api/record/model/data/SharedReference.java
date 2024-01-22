package eu.europeana.api.record.model.data;

import com.fasterxml.jackson.annotation.*;
import dev.morphia.Datastore;
import dev.morphia.annotations.*;
import dev.morphia.query.filters.Filters;
import eu.europeana.api.edm.RDF;
import eu.europeana.api.record.model.EDMClass;
import eu.europeana.api.record.model.ModelConstants;
import org.bson.Document;

/**
 * @author Hugo
 * @since 8 Aug 2023
 */
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@Entity(discriminator = ModelConstants.Shared, discriminatorKey = RDF.type)
public class SharedReference implements ObjectReference {

    @Property(ModelConstants.id)
    protected String id;

    @Reference(value = ModelConstants.object, lazy = true)
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
                    .filter(Filters.eq(ModelConstants.id, o.getID()))
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
        // when lazy loading the object still comes with an "artificial" value, for which,
        // the check against null passes and the call to getID() forces the effective load of the object
        // which effectively breaks the lazy loading. We need to find a better way to do this!
        if ( this.object != null ) { this.id = this.object.getID(); }
    }
}