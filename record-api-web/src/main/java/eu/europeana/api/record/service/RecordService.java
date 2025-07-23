package eu.europeana.api.record.service;

import dev.morphia.internal.DatastoreHolder;
import dev.morphia.query.FindOptions;
import dev.morphia.query.MorphiaCursor;
import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.record.config.AppConfig;
import eu.europeana.api.record.config.RecordApiConfiguration;
import eu.europeana.api.record.model.ProvidedCHO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import eu.europeana.api.record.db.repository.RecordRepository;

import java.util.List;
import java.util.Optional;

@Service(AppConfig.BEAN_RECORD_SERVICE)
public class RecordService {

    private final RecordRepository recordRepository;

    @Autowired
    public RecordService(
            @Qualifier(value = AppConfigConstants.BEAN_RECORD_REPO) 
            RecordRepository repo) {
        this.recordRepository = repo;
    }

    public void init() {
        DatastoreHolder.holder.set(recordRepository.getDatastore());
    }

    public ProvidedCHO saveRecord(ProvidedCHO providedCHO) {
        return recordRepository.save(providedCHO);
    }

    public Optional<ProvidedCHO> getRecord(String about) {
        return Optional.ofNullable(recordRepository.findById(about));
    }

    public Optional<ProvidedCHO> getRecord(String about, FindOptions opts) {
        return Optional.ofNullable(recordRepository.findById(about, opts));
    }

    public MorphiaCursor<ProvidedCHO> retrieveMultipleByRecordIds(List<String> recordIds) {
        return recordRepository.findByIds(recordIds);
    }

    public MorphiaCursor<ProvidedCHO> retrieveMultipleByRecordIds(List<String> recordIds
                                                                , FindOptions opts) {
        return recordRepository.findByIds(recordIds, opts);
    }

    public boolean existsByID(String about) {
        return recordRepository.exists(about);
    }

}
