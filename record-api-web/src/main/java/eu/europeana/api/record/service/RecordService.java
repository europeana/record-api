package eu.europeana.api.record.service;

import eu.europeana.api.record.config.AppConfig;
import eu.europeana.api.record.config.RecordApiConfiguration;
import eu.europeana.api.record.model.ProvidedCHO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import eu.europeana.api.record.db.repository.RecordRepository;

import java.util.List;
import java.util.Optional;

@Service(AppConfig.BEAN_RECORD_SERVICE)
public class RecordService {

    private final RecordRepository recordRepository;

    final RecordApiConfiguration recordApiConfiguration;

    @Autowired
    public RecordService(RecordRepository recordRepository, RecordApiConfiguration recordApiConfiguration) {
        this.recordRepository = recordRepository;
        this.recordApiConfiguration = recordApiConfiguration;
    }

    public ProvidedCHO saveRecord(ProvidedCHO providedCHO) {
        return recordRepository.save(providedCHO);
    }

    public Optional<ProvidedCHO> getRecord(String about) {
        return Optional.ofNullable(recordRepository.findById(about));
    }

    public List<ProvidedCHO> retrieveMultipleByRecordIds(List<String> recordIds) {
        return recordRepository.findByRecordIds(recordIds);
    }

    public boolean existsByID(String about) {
        return recordRepository.existsByRecordId(about);
    }

}
