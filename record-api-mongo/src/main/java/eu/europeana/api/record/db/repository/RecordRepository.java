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
import eu.europeana.api.record.model.data.ObjectRepository;
import eu.europeana.api.record.model.data.SharedObject;
import eu.europeana.api.record.model.data.SharedReference;
import eu.europeana.api.record.model.entity.ContextualEntity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.BsonObjectId;
import org.springframework.stereotype.Repository;

import com.mongodb.client.result.UpdateResult;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Record repository to storing and retrieving record data
 * @author srishti singh
 * @since 4 August 2023
 */
@Repository(AppConfigConstants.BEAN_RECORD_REPO)
public class RecordRepository implements ObjectRepository {

    private static final Logger LOGGER = LogManager.getLogger(RecordRepository.class);

    private static ThreadLocal<List<ContextualEntity>> 
        bulkEntity = new ThreadLocal<>();

    @Resource(name = AppConfigConstants.BEAN_RECORD_DATA_STORE)
    private Datastore datastore;

    /**
     * Saves the given record to the database.
     *
     * @param record record to save
     * @return saved record
     */
    public ProvidedCHO save(ProvidedCHO record) {
        UpdateOptions upsert = new UpdateOptions().upsert(true);
        Filter idf = Filters.eq("id", record.getID());

        
        SharedReference.repo.set(this);
        try {
            List<ContextualEntity> col = new ArrayList();
            bulkEntity.set(col);

            UpdateResult result = 
                    datastore.find(ProvidedCHO.class).filter(idf)
                             .update(upsert, new SetEntityOperator(record));
            //ProvidedCHO cho = datastore.save(record);

            int count1 = col.size();

            // note that this for loop using a cursor is necessary because
            // new entities may be added while iterating when an entity refers to 
            // other entities that are not directly linked to the record
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
    public long count() {
        return datastore.find(ProvidedCHO.class).count();
    }

    /**
     * Check if an Record exists that matches the given parameters using DBCollection.count().
     *
     * @param recordId ID of the dataset
     * @return true if yes, otherwise false
     */
    public boolean existsByRecordId(String recordId) {
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
    public ProvidedCHO findById(String recordId, FindOptions opts) {
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
    public MorphiaCursor<ProvidedCHO> findByRecordIds(List<String> recordIds) {
        return findByRecordIds(recordIds, new FindOptions());
    }

    /**
     * Fetches the records for the list of record ids
     * @param recordIds ids to be fetched
     * @param opts  : options for find
     * @return list of ProvidedCho(s)
     */
    public MorphiaCursor<ProvidedCHO> findByRecordIds(List<String> recordIds
                                                    , FindOptions opts) {
        return findByFilter(Filters.in(ModelConstants.id, recordIds), opts);
    }

    /**
     * Fetches records based on a filter
     * @param filter: filter to be used to select records
     * @return list of ProvidedCho(s)
     */
    public MorphiaCursor<ProvidedCHO> findByFilter(Filter filter) {
        return findByFilter(filter, new FindOptions());
    }

    /**
     * Fetches records based on a filter
     * @param filter: filter to be used to select records
     * @param opts  : options for find
     * @return list of ProvidedCho(s)
     */
    public MorphiaCursor<ProvidedCHO> findByFilter(Filter filter
                                                 , FindOptions opts) {
        return datastore
                .find(ProvidedCHO.class)
                .filter(filter)
                .iterator(opts);
    }

    /**
     * Fetches records based on a filter
     * @param filter: filter to be used to select records
     * @return list of ProvidedCho(s)
     */
    public MorphiaCursor<ProvidedCHO> findAll() {
        return findAll(new FindOptions());
    }

    /**
     * Fetches records based on a filter
     * @param filter: filter to be used to select records
     * @param opts  : options for find
     * @return list of ProvidedCho(s)
     */
    public MorphiaCursor<ProvidedCHO> findAll(FindOptions opts) {
        return datastore
                .find(ProvidedCHO.class)
                .iterator(opts);
    }

    public Datastore getDatastore() {
        return datastore;
    }


    /*
     * There is a problem with cycle references where this method is called 
     * indefinitely.
     * 
     * The best solution is to change the ID of entities to be the URI and not ObjectId
     */
    @Override
    public void save(SharedObject obj) {
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

    private void saveInternal(ContextualEntity entity) {
        Filter idf = Filters.eq("_id", entity.getID());
        long count = datastore.find(entity.getClass()).filter(idf).count();
        if ( count > 0 ) { return; }

        datastore.save(entity);
    }

}
