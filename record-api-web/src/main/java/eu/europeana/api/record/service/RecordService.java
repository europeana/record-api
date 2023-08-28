package eu.europeana.api.record.service;

import eu.europeana.api.record.config.AppConfig;
import eu.europeana.api.record.config.RecordApiConfiguration;
import eu.europeana.api.record.model.Record;
import eu.europeana.api.record.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(AppConfig.BEAN_RECORD_SERVICE)
public class RecordService {

    private final RecordRepository recordRepository;

    final RecordApiConfiguration recordApiConfiguration;

    @Autowired
    public RecordService(RecordRepository recordRepository, RecordApiConfiguration recordApiConfiguration) {
        this.recordRepository = recordRepository;
        this.recordApiConfiguration = recordApiConfiguration;
    }

    public Record saveRecord(Record record) {
        return recordRepository.save(record);
    }

    public Record getRecord(String about) {
        return recordRepository.findById(about);
    }

    public boolean existsByID(String about) {
        return recordRepository.existsByRecordId(about);
    }

}
