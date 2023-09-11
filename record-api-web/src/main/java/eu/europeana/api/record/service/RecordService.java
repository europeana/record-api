package eu.europeana.api.record.service;

import eu.europeana.api.record.config.AppConfig;
import eu.europeana.api.record.config.RecordApiConfiguration;
import eu.europeana.api.record.model.ProvidedCHO;
import eu.europeana.api.record.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public boolean existsByID(String about) {
        return recordRepository.existsByRecordId(about);
    }

}
