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

    public static ThreadLocal<SharedReferenceHandler> handler = new ThreadLocal<>();

    @Property(ModelConstants.id)
    protected String id;

    @Reference(value = ModelConstants.object, lazy = true)
    protected SharedObject object;


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

    public SharedObject getDereferencedObject() {
        SharedObject obj = this.object;
        //explain why there is a if with MorphiaProxy
        if ( obj instanceof MorphiaProxy ) { 
            try {
                obj = (SharedObject)((MorphiaProxy)obj).unwrap();
            }
            catch (ReferenceException e) {
                LOGGER.warn("Could not dereference: " + this.id);
                return null;
            }
        }
        return obj;
    }

    public void setDereferencedObject(SharedObject obj) {
        this.object = obj;
    }

    public String toString() { return ("<" + id + ">"); }

    @PrePersist
    public void prePersist(Document doc, Datastore ds) {
        if ( this.object != null ) {
            SharedReferenceHandler handler = this.handler.get();
            if ( handler != null ) { handler.saveShared(this.object); }
        }
    }

    @PostLoad
    public void postLoad(Document document, Datastore ds) {
        
        if ( this.object != null ) {
            SharedReferenceHandler handler = this.handler.get();
            if ( handler != null ) { handler.loadShared(this); }
        }
        // TODO:
        // Consider again removing the id field since the reference is now based
        // on that. The challenge will be getting the id from the reference without
        // forcing a load from the db.
    }
}