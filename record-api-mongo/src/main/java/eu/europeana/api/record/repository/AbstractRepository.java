package eu.europeana.api.record.repository;

import dev.morphia.Datastore;
import eu.europeana.api.record.vocabulary.AppConfigConstants;

import javax.annotation.Resource;

public abstract class AbstractRepository {

    @Resource(name = AppConfigConstants.BEAN_RECORD_DATA_STORE)
    Datastore datastore;


    protected Datastore getDataStore() {
        return datastore;
    }
}
