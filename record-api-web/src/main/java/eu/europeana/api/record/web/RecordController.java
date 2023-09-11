package eu.europeana.api.record.web;

import eu.europeana.api.commons.web.http.HttpHeaders;
import eu.europeana.api.record.exception.RecordApiException;
import eu.europeana.api.record.exception.RecordDoesNotExistsException;
import eu.europeana.api.record.model.ProvidedCHO;
import eu.europeana.api.record.serialization.JsonLdSerializer;
import eu.europeana.api.record.service.RecordService;
import eu.europeana.api.record.test.TestDataBuilder;
import eu.europeana.api.record.utils.RecordUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Optional;

@RestController
@Validated
public class RecordController {

    private static final Logger LOGGER = LogManager.getLogger(RecordController.class);

    private final RecordService recordService;

    private final JsonLdSerializer jsonLdSerializer;

    @Autowired
    public RecordController(RecordService recordService, JsonLdSerializer jsonLdSerializer) {
        this.recordService = recordService;
        this.jsonLdSerializer = jsonLdSerializer;
    }

    @ApiOperation(
            value = "Retrieve a record",
            nickname = "retrieveRecord",
            response = java.lang.Void.class)
    @GetMapping(
            value = {
                    "/record/{collectionId}/{recordId}",
                    "/record/{collectionId}/{recordId}.jsonld",
                    "/record/{collectionId}/{recordId}.json"
            },
            produces = {HttpHeaders.CONTENT_TYPE_JSONLD, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> retrieveRecord (
            @PathVariable String collectionId,
            @PathVariable String recordId,
            HttpServletRequest request) throws RecordApiException, IOException {

        String about = RecordUtils.buildRecordId(collectionId, recordId);

//        ProvidedCHO testRecord = new TestDataBuilder().newRecord();
//        recordService.saveRecord(testRecord);
//
//        LOGGER.info("saved Data");

        Optional<ProvidedCHO> record = recordService.getRecord(about);

        if (record.isEmpty()) {
            throw new RecordDoesNotExistsException(about);
        }

        String body = jsonLdSerializer.serialize(record.get());
        return ResponseEntity.status(HttpStatus.OK).body(body);

    }

}
