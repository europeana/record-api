package eu.europeana.api.record.model.data;

import com.fasterxml.jackson.annotation.*;

import dev.morphia.Datastore;
import dev.morphia.annotations.*;
import dev.morphia.mapping.codec.references.MorphiaProxy;
import dev.morphia.mapping.lazy.proxy.ReferenceException;
import eu.europeana.api.edm.RDF;
import eu.europeana.api.record.model.EDMClass;
import eu.europeana.api.record.model.ModelConstants;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

/**
 * @author Hugo
 * @since 8 Aug 2023
 */
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@Entity(discriminator = ModelConstants.Shared, discriminatorKey = RDF.type)
public class SharedReference implements ObjectReference {

    private static final Logger LOGGER = LogManager.getLogger(SharedReference.class);

    public static ThreadLocal<ObjectRepository> repo = new ThreadLocal<>();

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

    /* 
     * this method needs to check further if the object is actually dereferenced 
     * or not (looking at the MorphiaProxy). 
     * I havent done it yet because this will break the serialisation.
     */
    public boolean isDereferenced() { return (this.object != null); }

    public EDMClass getDereferencedObject() {
        EDMClass obj = this.object;
        if ( obj instanceof MorphiaProxy ) { 
            try {
                obj = (EDMClass)((MorphiaProxy)obj).unwrap();
            }
            catch (ReferenceException e) {
                LOGGER.warn("Could not dereference: " + this.id);
                return null;
            }
        }
        return obj;
    }

    public String toString() { return ("<" + id + ">"); }

    @PrePersist
    public void prePersist(Document doc, Datastore ds) {
        if ( this.object != null ) {
            ObjectRepository repo = this.repo.get();
            repo.save(this.object);
        }
    }

    @PostLoad
    public void postLoad(Document document, Datastore ds)
    {
        // TODO:
        // Consider again removing the id field since the reference is now based
        // on that. The challenge will be getting the id from the reference without
        // forcing a load from the db.
        
        //OUTDATED:
        // when lazy loading the object still comes with an "artificial" value, for which,
        // the check against null passes and the call to getID() forces the effective load of the object
        // which effectively breaks the lazy loading. We need to find a better way to do this!
        //if ( this.object != null ) { this.id = this.object.getID(); }
    }
}