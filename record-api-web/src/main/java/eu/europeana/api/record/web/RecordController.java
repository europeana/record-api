package eu.europeana.api.record.web;

import eu.europeana.api.commons.web.http.HttpHeaders;
import eu.europeana.api.record.exception.HttpBadRequestException;
import eu.europeana.api.record.exception.RecordAlreadyExistsException;
import eu.europeana.api.record.exception.RecordApiException;
import eu.europeana.api.record.impl.RecordImpl;
import eu.europeana.api.record.service.RecordService;
import eu.europeana.api.record.utils.RecordUtils;
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

@RestController
@Validated
public class RecordController {

    private static final Logger LOGGER = LogManager.getLogger(RecordController.class);

    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }


    @ApiOperation(
            value = "Register a new record",
            nickname = "registerRecord",
            response = java.lang.Void.class)
    @PostMapping(
            value = "/record/",
            produces = {MediaType.APPLICATION_JSON_VALUE, HttpHeaders.CONTENT_TYPE_JSONLD})
    public ResponseEntity<String> registerRecord(
            @RequestBody RecordImpl record, HttpServletRequest request) throws RecordApiException {

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

        recordService.saveRecord(record);
        return new ResponseEntity<>(HttpStatus.OK);
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
            HttpServletRequest request) throws Exception {

        String about = RecordUtils.buildRecordId(collectionId, recordId);
        RecordImpl record = (RecordImpl) recordService.getRecord(about);
        return new ResponseEntity(HttpStatus.OK);
    }



}
