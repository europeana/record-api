package eu.europeana.api.record.db.repository;

import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
import dev.morphia.query.MorphiaCursor;
import dev.morphia.query.filters.Filter;
import dev.morphia.query.filters.Filters;
import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.record.model.ProvidedCHO;
import eu.europeana.api.record.model.data.SharedObject;
import eu.europeana.api.record.model.data.SharedReference;
import eu.europeana.api.record.model.entity.ContextualEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Record repository to storing and retrieving record data
 * @author srishti singh
 * @since 4 August 2023
 */
//@Repository(AppConfigConstants.BEAN_RECORD_REPO)
public class OptimisedRecordRepository extends RecordRepository {

    private static final Logger LOGGER = LogManager.getLogger(OptimisedRecordRepository.class);

    private static ThreadLocal<List<SharedObject>> 
        bulkObjs = new ThreadLocal<>();

    private static ThreadLocal<List<SharedReference>> 
        bulkRefs = new ThreadLocal<>();

    public OptimisedRecordRepository(Datastore ds) {
        super(ds);
    }

    /**
     * Find Record that matches the given record id and supplying find options
     *
     * @param recordId : Id of the record to be fetched
     * @param opts     : options for find
     * @return record matching record id
     */
    @Override
    public ProvidedCHO findById(String recordId, FindOptions opts) {
        SharedReference.handler.set(this);
        try {
            bulkRefs.set(new ArrayList<>());
            return super.findById(recordId, opts);
        }
        finally {
            bulkLoad();
        }
    } 

    /**
     * Fetches records based on a filter
     * @param filter: filter to be used to select records
     * @param opts  : options for find
     * @return list of ProvidedCho(s)
     */
    @Override
    public MorphiaCursor<ProvidedCHO> findByFilter(Filter filter
                                                 , FindOptions opts) {
        SharedReference.handler.set(this);
        try {
            bulkRefs.set(new ArrayList<>());
            return super.findByFilter(filter, opts);
        }
        finally {
            bulkLoad();
        }
    }

    /**
     * Saves the given record to the database.
     *
     * @param record record to save
     * @return saved record
     */
    @Override
    public ProvidedCHO save(ProvidedCHO obj) {
        SharedReference.handler.set(this);
        try {
            bulkObjs.set(new ArrayList<SharedObject>());

            return bulkSave(super.save(obj));
        }
        catch (RuntimeException e) {
            LOGGER.error("Error storing cho: " + obj, e);
            return null;
        }
    }


    
    /*
     * Records the shared reference so that it can be loaded in bulk and set
     * with the value.
     * 
     * This is method is called by the SharedReference whenever a object 
     * containing this reference is loaded.
     */
    @Override
    public void loadShared(SharedReference ref) {
        bulkRefs.get().add(ref);
    }

    /*
     * Records the shared objects so that they can be saved in bulk and only once.
     * 
     * This is method is called by the SharedReference whenever a object 
     * containing this reference is stored.
     */
    @Override
    public void saveShared(SharedObject obj) {
        if ( obj instanceof ContextualEntity ) {
            List<SharedObject> list = bulkObjs.get();
            if ( !list.contains(obj) ) { list.add(obj); }
        }
    }


    /* INTERNAL METHODS */

    /*
     * TODO: Handle profiles which may limit the properties or require entities
     * to be further expanded.
     */
    private void bulkLoad() {
        List<SharedReference> list = bulkRefs.get();
        try {
            if ( list.isEmpty() ) { return; }

            //collects recursively all references and loads the shared objects
            Map<String,ContextualEntity> cache = new HashMap<>();
            recursiveLoad(list, 0, cache);

            //set all collected references with the objects
            for ( SharedReference ref : list ) {
                ContextualEntity e = cache.get(ref.getID());
                ref.setDereferencedObject(e);
            }
        }
        finally {
            bulkRefs.remove();
        }
    }
    private void recursiveLoad(List<SharedReference> list, int lastPosition
                             , Map<String,ContextualEntity> cache) {

        int size = list.size();
        List<String> toFetch = new ArrayList<>(size-lastPosition);
        for ( int i = lastPosition; i < size; i++) {
            String id = list.get(i).getID();
            if ( cache.containsKey(id) ) { continue; }

            toFetch.add(id);
            //must add in the cache in case the resource, for whatever reason, 
            //is not in the database
            cache.put(id, null);
        }

        //happens when the new resources had already been loaded as part of this
        //cycle
        if ( toFetch.isEmpty() ) { return; }

        try ( MorphiaCursor<ContextualEntity> cursor 
                = super.findEntitiesByIds(toFetch)) {
            while ( cursor.hasNext() ) {
                ContextualEntity entity = cursor.next();
                cache.put(entity.getID(), entity);
            }
        }

        lastPosition = size;
        if ( lastPosition == list.size() ) { return; }

        //attempt to load shared references that were part of the resources that 
        //were loaded
        recursiveLoad(list, lastPosition, cache);
    }
    
    private ProvidedCHO bulkSave(ProvidedCHO obj) {
        List<SharedObject> list = bulkObjs.get();
        try {
            if ( list.isEmpty() ) { return obj; }

            int count1 = list.size();

            //TODO: Check if we can save several entities at the same time!

            // note that this for loop using a cursor is necessary because
            // new entities may be added while iterating (ie. when an entity refers to 
            // other entities that are not directly linked to the record)
            for ( int i = 0; i < list.size(); i++ ) {
                super.saveShared(list.get(i));
            }

            int count2 = list.size();
            if ( count1 != count2 ) {
                LOGGER.debug("Caught recursive entities: " + obj);
            }
            return obj;
        }
        finally {
            bulkObjs.remove();
        }
    }
}
