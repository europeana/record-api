package eu.europeana.api.record.repository;

import dev.morphia.query.FindOptions;
import dev.morphia.query.filters.Filter;
import dev.morphia.query.filters.Filters;
import eu.europeana.api.record.model.ProvidedCHO;
import eu.europeana.api.record.vocabulary.AppConfigConstants;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Record repository to storing and retrieving record data
 * @author srishti singh
 * @since 4 August 2023
 */

// TODO switch record class to < ? extends Record interface > once the interface is ready
@Repository(AppConfigConstants.BEAN_RECORD_REPO)
public class RecordRepository extends AbstractRepository {


    /**
     * Saves the given record to the database.
     *
     * @param record record to save
     * @return saved record
     */
    public ProvidedCHO save(ProvidedCHO record) {
        return getDataStore().save(record);
    }


    /** @return the total number of records in the database */
    public long count() {
        return getDataStore().find(ProvidedCHO.class).count();
    }

    /**
     * Check if an Record exists that matches the given parameters using DBCollection.count().
     *
     * @param recordId ID of the dataset
     * @return true if yes, otherwise false
     */
    public boolean existsByRecordId(String recordId) {
        return getDataStore()
                .find(ProvidedCHO.class)
                .filter(Filters.eq("id", recordId))
                .count()
                > 0;
    }

    /**
     * Find Record that matches the given record id
     *
     * @param about : Id of the record to be fetched
     * @return record matching record id
     */
    public ProvidedCHO findById(String about) {
        List<Filter> filters = new ArrayList<>();
        filters.add(Filters.eq("id", about));

        return getDataStore()
                .find(ProvidedCHO.class)
                .filter(filters.toArray(Filter[]::new))
                .iterator(new FindOptions())
                .tryNext();
    }

}
