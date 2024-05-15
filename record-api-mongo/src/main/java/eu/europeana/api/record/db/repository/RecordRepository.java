package eu.europeana.api.record.db.repository;

import dev.morphia.Datastore;
import dev.morphia.internal.DatastoreHolder;
import dev.morphia.query.FindOptions;
import dev.morphia.query.MorphiaCursor;
import dev.morphia.query.filters.Filter;
import dev.morphia.query.filters.Filters;
import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.record.model.ProvidedCHO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Record repository to storing and retrieving record data
 * @author srishti singh
 * @since 4 August 2023
 */
@Repository(AppConfigConstants.BEAN_RECORD_REPO)
public class RecordRepository {

    @Resource(name = AppConfigConstants.BEAN_RECORD_DATA_STORE)
    private Datastore datastore;

    /**
     * Saves the given record to the database.
     *
     * @param record record to save
     * @return saved record
     */
    public ProvidedCHO save(ProvidedCHO record) {
        return datastore.save(record);
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
                .filter(Filters.eq("id", recordId))
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
        List<Filter> filters = new ArrayList<>();
        filters.add(Filters.eq("id", recordId));

        return datastore
                .find(ProvidedCHO.class)
                .filter(filters.toArray(Filter[]::new))
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
        List<Filter> filters = new ArrayList<>();
        filters.add(Filters.in("id", recordIds));
        return datastore
                .find(ProvidedCHO.class)
                .filter(filters.toArray(Filter[]::new))
                .iterator(opts);
    }

    public Datastore getDatastore() {
        return datastore;
    }

    //    public void save(EDMClass o)
//    {
//        if ( o != null ) {
//            long count = datastore.find(o.getClass()).filter(Filters.eq("id", o.getID())).count();
//            if ( count  > 0 ) { return; }
//
//            datastore.save(o);
//        }
//    }


}
