package eu.europeana.api.record.db.repository;

import dev.morphia.Datastore;
import dev.morphia.UpdateOptions;
import dev.morphia.query.FindOptions;
import dev.morphia.query.MorphiaCursor;
import dev.morphia.query.filters.Filter;
import dev.morphia.query.filters.Filters;
import dev.morphia.query.updates.SetEntityOperator;
import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.record.model.ModelConstants;
import eu.europeana.api.record.model.ProvidedCHO;
import eu.europeana.api.record.model.data.SharedObject;
import eu.europeana.api.record.model.data.SharedReference;
import eu.europeana.api.record.model.data.SharedReferenceHandler;
import eu.europeana.api.record.model.entity.ContextualEntity;
import eu.europeana.api.repository.ObjectRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;

import java.util.Collection;

/**
 * Record repository to storing and retrieving record data
 * @author srishti singh
 * @since 4 August 2023
 */
//@Repository(AppConfigConstants.BEAN_RECORD_REPO)
public class RecordRepository implements ObjectRepository<ProvidedCHO>
                                        , SharedReferenceHandler {

    private static final Logger LOGGER = LogManager.getLogger(RecordRepository.class);

//    @Resource(name = AppConfigConstants.BEAN_RECORD_DATA_STORE)
    protected Datastore datastore;

    public RecordRepository(Datastore ds) {
        this.datastore = ds;
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
        return datastore
                .find(ProvidedCHO.class)
                .filter(filter)
                .iterator(opts);
    }


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

        try {
            datastore.find(ProvidedCHO.class).filter(idf)
                     .update(upsert, new SetEntityOperator(record));
            
            return record;
        }
        catch (RuntimeException e) {
            LOGGER.error("Error storing cho: " + record.getID(), e);
            return null;
        }
    }

    @Override
    public void loadShared(SharedReference ref) {
        ref.setDereferencedObject(findEntityById(ref.getID()));
    }

    /*
     * There is a problem with cycle references where this method is called 
     * indefinitely.
     * 
     * The best solution is to change the ID of entities to be the URI and not ObjectId
     */
    @Override
    public void saveShared(SharedObject obj) {
        Filter idf = Filters.eq("_id", obj.getID());
        long count = datastore.find(obj.getClass()).filter(idf).count();
        if ( count > 0 ) { return; }

        datastore.save(obj);
    }

    public Datastore getDatastore() {
        return datastore;
    }

    protected ContextualEntity findEntityById(String id) {
        return datastore
                .find(ContextualEntity.class)
                .filter(Filters.eq(ModelConstants.id, id))
                .iterator(new FindOptions())
                .tryNext();
    }

    protected MorphiaCursor<ContextualEntity> findEntitiesByIds(Collection<String> ids) {
        Filter filter = Filters.in("_id", ids);
        return datastore.find(ContextualEntity.class)
                        .filter(filter).iterator();
    }
}
