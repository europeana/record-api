package eu.europeana.api.record.db.repository;

import dev.morphia.Datastore;
import dev.morphia.InsertOptions;
import dev.morphia.UpdateOptions;
import dev.morphia.internal.DatastoreHolder;
import dev.morphia.query.FindOptions;
import dev.morphia.query.MorphiaCursor;
import dev.morphia.query.filters.Filter;
import dev.morphia.query.filters.Filters;
import dev.morphia.query.updates.SetEntityOperator;
import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.record.model.EDMClass;
import eu.europeana.api.record.model.ModelConstants;
import eu.europeana.api.record.model.ProvidedCHO;
import eu.europeana.api.record.model.data.SharedObject;
import eu.europeana.api.record.model.data.SharedReference;
import eu.europeana.api.record.model.data.SharedReferenceHandler;
import eu.europeana.api.record.model.entity.ContextualEntity;
import eu.europeana.api.repository.ObjectRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.BsonObjectId;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Record repository to storing and retrieving record data
 * @author srishti singh
 * @since 4 August 2023
 */
//@Repository(AppConfigConstants.BEAN_RECORD_REPO)
public class OptimisedRecordRepositoryCopy implements ObjectRepository<ProvidedCHO>, SharedReferenceHandler {

    private static final Logger LOGGER = LogManager.getLogger(OptimisedRecordRepositoryCopy.class);

    private static ThreadLocal<List<ContextualEntity>> 
        bulkEntity = new ThreadLocal<>();

    private static ThreadLocal<List<SharedReference>> 
        bulkRefs = new ThreadLocal<>();

    @Resource(name = AppConfigConstants.BEAN_RECORD_DATA_STORE)
    private Datastore datastore;

    /**
     * Saves the given record to the database.
     *
     * @param record record to save
     * @return saved record
     */
    @Override
    public ProvidedCHO save(ProvidedCHO record) {
        UpdateOptions upsert = new UpdateOptions().upsert(true);
        Filter idf = Filters.eq(ModelConstants.id, record.getID());

        
        SharedReference.handler.set(this);
        try {
            List<ContextualEntity> col = new ArrayList<>();
            bulkEntity.set(col);

            UpdateResult result = 
                    datastore.find(ProvidedCHO.class).filter(idf)
                             .update(upsert, new SetEntityOperator(record));
            //ProvidedCHO cho = datastore.save(record);

            int count1 = col.size();

            // note that this for loop using a cursor is necessary because
            // new entities may be added while iterating (ie. when an entity refers to 
            // other entities that are not directly linked to the record)
            for ( int i = 0; i < col.size(); i++ ) {
                saveInternal(col.get(i));
            }

            int count2 = col.size();
            if ( count1 != count2 ) {
                LOGGER.debug("Caught recursive entities: " + record.getID());
            }
            
            return record;
        }
        catch (RuntimeException e) {
            LOGGER.error("Error storing cho: " + record.getID(), e);
            return null;
        }
        finally {
            bulkEntity.remove();
        }
    }


    /** @return the total number of records in the database */
    @Override
    public long size() {
        return datastore.find(ProvidedCHO.class).count();
    }

    /**
     * Check if an Record exists that matches the given parameters using DBCollection.count().
     *
     * @param recordId ID of the dataset
     * @return true if yes, otherwise false
     */
    @Override
    public boolean exists(String recordId) {
        return datastore
                .find(ProvidedCHO.class)
                .filter(Filters.eq(ModelConstants.id, recordId))
                .count()
                > 0;
    }

    /**
     * Find Record that matches the given record id
     *
     * @param recordId : Id of the record to be fetched
     * @return record matching record id
     */
    @Override
    public ProvidedCHO findById(String recordId) {
        return findById(recordId, new FindOptions());
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
        return datastore
                .find(ProvidedCHO.class)
                .filter(Filters.eq(ModelConstants.id, recordId))
                .iterator(opts)
                .tryNext();
    } 

    /**
     * Fetches the records for the list of record ids
     * @param recordIds ids to be fetched
     * @return list of ProvidedCho(s)
     */
    @Override
    public MorphiaCursor<ProvidedCHO> findByIds(Collection<String> recordIds) {
        return findByIds(recordIds, new FindOptions());
    }

    /**
     * Fetches the records for the list of record ids
     * @param recordIds ids to be fetched
     * @param opts  : options for find
     * @return list of ProvidedCho(s)
     */
    @Override
    public MorphiaCursor<ProvidedCHO> findByIds(Collection<String> recordIds
                                              , FindOptions opts) {
        return findByFilter(Filters.in(ModelConstants.id, recordIds), opts);
    }

    /**
     * Fetches records based on a filter
     * @param filter: filter to be used to select records
     * @return list of ProvidedCho(s)
     */
    @Override
    public MorphiaCursor<ProvidedCHO> findByFilter(Filter filter) {
        return findByFilter(filter, new FindOptions());
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
            return datastore
                    .find(ProvidedCHO.class)
                    .filter(filter)
                    .iterator(opts);
        }
        finally {
            bulkLoad();
        }
    }



    public Datastore getDatastore() {
        return datastore;
    }


    @Override
    public void loadShared(SharedReference ref) {
        bulkRefs.get().add(ref);
    }

    /*
     * There is a problem with cycle references where this method is called 
     * indefinitely.
     * 
     * The best solution is to change the ID of entities to be the URI and not ObjectId
     */
    @Override
    public void saveShared(SharedObject obj) {
        if ( obj instanceof ContextualEntity ) {
            List<ContextualEntity> list = bulkEntity.get();
            ContextualEntity e = (ContextualEntity)obj;
            if ( !list.contains(e) ) { list.add(e); }
        }

        /*
        try {
        EDMClass o = ref.getDereferencedObject();
            Filter idf = Filters.eq("_id", o.getID());
            long count = datastore.find(o.getClass()).filter(idf).count();
            if ( count > 0 ) { return; }

            datastore.save(o);

            UpdateOptions upsert = new UpdateOptions().upsert(true);
            Filter idf = Filters.eq(ModelConstants.id, o.getID());
            UpdateResult result = 
                datastore.find(o.getClass()).filter(idf)
                         .update(upsert, new SetEntityOperator(o));

            BsonObjectId objID = (BsonObjectId)result.getUpsertedId();
            if ( objID != null ) {
                ContextualEntity ce = (ContextualEntity)o;
                ce.setObjectID(objID.getValue());
                return;
            }
    
            EDMClass o2 = datastore.find(o.getClass()).filter(idf).first();
            if ( o2 != null ) {
                ref.setDereferencedObject(o2);
            }
        }
        catch (RuntimeException e) {
            LOGGER.error("Error storing entity: " + o.getID());
            throw e;
        }
            */

        
//      (UpdateOperators.set(ModelConstants.id, o.getID())).execute(new ModifyOptions()
//      .returnDocument(ReturnDocument.AFTER));
//  ds.getMapper().getCollection(o.getClass());

//  ds.find(o.getClass()).filter(Filters.eq(ModelConstants.id, o.getID())).ficount();
//  if ( count  > 0 ) { return; }
    }








    //    public void save(EDMClass o)
//    {
//        if ( o != null ) {
//            long count = datastore.find(o.getClass()).filter(Filters.eq(ModelConstants.id, o.getID())).count();
//            if ( count  > 0 ) { return; }
//
//            datastore.save(o);
//        }
//    }

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

        Filter filter = Filters.in("_id", toFetch);
        try ( MorphiaCursor<ContextualEntity> cursor 
                = datastore.find(ContextualEntity.class)
                           .filter(filter).iterator() ){
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

    private void saveInternal(ContextualEntity entity) {
        Filter idf = Filters.eq("_id", entity.getID());
        long count = datastore.find(entity.getClass()).filter(idf).count();
        if ( count > 0 ) { return; }

        datastore.save(entity);
    }
}
