package eu.europeana.api.record.web;

import eu.europeana.api.commons.web.http.HttpHeaders;
import eu.europeana.api.record.exception.RecordApiException;
import eu.europeana.api.record.exception.RecordDoesNotExistsException;
import eu.europeana.api.record.model.ProvidedCHO;
import eu.europeana.api.record.serialization.JsonLdSerializer;
import eu.europeana.api.record.service.RecordService;
import eu.europeana.api.record.utils.RecordUtils;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Optional;

import static eu.europeana.api.record.utils.RecordConstants.*;

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
                    "/v3/record/{datasetId}/{localId}.jsonld",
                    "/v3/record/{datasetId}/{localId}.json"
            },
            produces = {HttpHeaders.CONTENT_TYPE_JSONLD, MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<String> retrieveJsonRecord(
            @PathVariable String datasetId,
            @PathVariable String localId,
            HttpServletRequest request) throws RecordApiException, IOException {
        return createResponse(datasetId, localId);
    }


    @ApiOperation(
            value = "Retrieve a record",
            nickname = "retrieveRecord",
            response = java.lang.Void.class)
    @GetMapping(
            value = {
                    "/v3/record/{datasetId}/{localId}.xml",
                    "/v3/record/{datasetId}/{localId}.rdf"
            },
            produces = {MediaType.TEXT_XML_VALUE, MediaType.APPLICATION_XML_VALUE, HttpHeaders.CONTENT_TYPE_APPLICATION_RDF_XML, HttpHeaders.CONTENT_TYPE_RDF_XML})
    public ResponseEntity<String> retrieveXmlRecord(
            @PathVariable String datasetId,
            @PathVariable String localId,
            HttpServletRequest request) {
        LOGGER.info("retrieveXmlRecord");
        return null;

    }

    @ApiOperation(
            value = "Retrieve a record",
            nickname = "retrieveRecord",
            response = java.lang.Void.class)
    @GetMapping(
            value = {
                    "/v3/record/{datasetId}/{localId}.ttl",
            },
            produces = {MEDIA_TYPE_TURTLE_TEXT, MEDIA_TYPE_TURTLE, MEDIA_TYPE_TURTLE_X})
    public ResponseEntity<String> retrieveTurtleRecord(
            @PathVariable String datasetId,
            @PathVariable String localId,
            HttpServletRequest request) {
        LOGGER.info("retrieveTurtleRecord");
        return null;
    }


    @ApiOperation(
            value = "Retrieve a record",
            nickname = "retrieveRecord",
            response = java.lang.Void.class)
    @GetMapping(
            value = {
                    "/v3/record/{datasetId}/{localId}",
            },
            headers = { ACCEPT_HEADER_JSONLD, ACCEPT_HEADER_JSON, ACCEPT_HEADER_APPLICATION_RDF_XML, ACCEPT_HEADER_RDF_XML, ACCEPT_HEADER_APPLICATION_XML},
            produces = {HttpHeaders.CONTENT_TYPE_JSONLD_UTF8, HttpHeaders.CONTENT_TYPE_JSON_UTF8, MediaType.TEXT_XML_VALUE,
                    MediaType.APPLICATION_XML_VALUE, HttpHeaders.CONTENT_TYPE_APPLICATION_RDF_XML, HttpHeaders.CONTENT_TYPE_RDF_XML})
    public ResponseEntity<String> retrieveRecord(
            @PathVariable String datasetId,
            @PathVariable String localId,
            HttpServletRequest request) {
        LOGGER.info("retrieve Header Record");
        return null;

    }


    private ResponseEntity<String> createResponse(String collectionId, String recordId) throws RecordApiException, IOException {
        String about = RecordUtils.buildRecordId(collectionId, recordId);
//        ProvidedCHO testRecord = new TestDataBuilder().newRecord();
//        recordService.saveRecord(testRecord);
//        LOGGER.info("saved Data");

        Optional<ProvidedCHO> record = recordService.getRecord(about);

        if (record.isEmpty()) {
            throw new RecordDoesNotExistsException(about);
        }

        String body = jsonLdSerializer.serialize(record.get());
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }
}
