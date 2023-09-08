package eu.europeana.api.record.web;

import eu.europeana.api.commons.web.http.HttpHeaders;
import eu.europeana.api.record.datatypes.DataValue;
import eu.europeana.api.record.exception.HttpBadRequestException;
import eu.europeana.api.record.exception.RecordAlreadyExistsException;
import eu.europeana.api.record.exception.RecordApiException;
import eu.europeana.api.record.exception.RecordDoesNotExistsException;
import eu.europeana.api.record.impl.AgentImpl;
import eu.europeana.api.record.impl.ObjectReferenceImpl;
import eu.europeana.api.record.impl.RecordImpl;
import eu.europeana.api.record.model.Entity;
import eu.europeana.api.record.model.Record;
import eu.europeana.api.record.serialization.JsonLdSerializer;
import eu.europeana.api.record.service.RecordService;
import eu.europeana.api.record.utils.RecordUtils;
import eu.europeana.api.record.vocabulary.EntityTypes;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
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
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.Map;
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
            value = "Register a new record",
            nickname = "registerRecord",
            response = java.lang.Void.class)
    @PostMapping(
            value = "/record/",
            produces = {MediaType.APPLICATION_JSON_VALUE, HttpHeaders.CONTENT_TYPE_JSONLD})
    public ResponseEntity<String> registerRecord(
            @RequestBody RecordImpl record, HttpServletRequest request) throws RecordApiException, IOException {

        System.out.println(record.getProxies().get(0));
        System.out.println(record.getProxies().get(1));

        // TODO request validation for language tagged values to be single
        //  and others validation yet to be added
        if (StringUtils.isNotEmpty(record.getAbout())) {
            LOGGER.debug("Registering new Record={}", record.getAbout());
        } else {
            // id is mandatory in request body
            throw new HttpBadRequestException("Mandatory field missing in the request body: id");
        }

        // check if Record already exists
        if (recordService.existsByID(record.getAbout())) {
            throw new RecordAlreadyExistsException(record.getAbout());
        }


        ObjectReferenceImpl object = (ObjectReferenceImpl) record.getProxies().get(0).getDescription().get(3);

        System.out.println(object);
        System.out.println(object.getReferencedObject().getClass());

        RecordImpl savedRecord = (RecordImpl) recordService.saveRecord(record);
        String body = jsonLdSerializer.serialize(savedRecord);
        return ResponseEntity.status(HttpStatus.OK).body(body);
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
        Optional<Record> record = recordService.getRecord(about);

        if (record.isEmpty()) {
            throw new RecordDoesNotExistsException(about);
        }

        String body = jsonLdSerializer.serialize(record.get());
        return ResponseEntity.status(HttpStatus.OK).body(body);

    }

}
