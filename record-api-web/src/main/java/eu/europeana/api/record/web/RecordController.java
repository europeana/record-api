package eu.europeana.api.record.web;

import eu.europeana.api.commons.web.http.HttpHeaders;
import eu.europeana.api.error.EuropeanaApiException;
import eu.europeana.api.format.RdfFormat;
import eu.europeana.api.record.exception.RecordDoesNotExistsException;
import eu.europeana.api.record.io.FormatHandlerRegistry;
import eu.europeana.api.record.model.ProvidedCHO;
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

import java.io.*;
import java.util.Optional;

import static eu.europeana.api.record.utils.RecordConstants.*;

@RestController
@Validated
public class RecordController {

    private static final Logger LOGGER = LogManager.getLogger(RecordController.class);

    private final RecordService recordService;

    private final FormatHandlerRegistry formatHandlerRegistry;

    @Autowired
    public RecordController(RecordService recordService, FormatHandlerRegistry formatHandlerRegistry) {
        this.recordService = recordService;
        this.formatHandlerRegistry = formatHandlerRegistry;
    }


    @ApiOperation(
            value = "Retrieve a record",
            nickname = "retrieveRecord",
            response = java.lang.Void.class)
    @GetMapping(
            value = {
                    "/record/v3/{datasetId}/{localId}",
            },
            headers = { ACCEPT_HEADER_JSONLD, ACCEPT_HEADER_JSON,
                    ACCEPT_HEADER_APPLICATION_TEXT_XML, ACCEPT_HEADER_RDF_XML, ACCEPT_HEADER_APPLICATION_RDF_XML, ACCEPT_HEADER_APPLICATION_XML,
                    ACCEPT_HEADER_APPLICATION_TURTLE_TEXT, ACCEPT_HEADER_APPLICATION_TURTLE, ACCEPT_HEADER_APPLICATION_TURTLE_X
            },
            produces = {HttpHeaders.CONTENT_TYPE_JSONLD_UTF8, HttpHeaders.CONTENT_TYPE_JSON_UTF8,
                    MediaType.TEXT_XML_VALUE, HttpHeaders.CONTENT_TYPE_RDF_XML, HttpHeaders.CONTENT_TYPE_APPLICATION_RDF_XML, MediaType.APPLICATION_XML_VALUE,
                    MEDIA_TYPE_TURTLE_TEXT, MEDIA_TYPE_TURTLE, MEDIA_TYPE_TURTLE_X})
    public ResponseEntity<String> retrieveJsonRecord(
            @PathVariable String datasetId,
            @PathVariable String localId,
            HttpServletRequest request) throws EuropeanaApiException, IOException {
        RdfFormat rdfFormat = RecordUtils.getRDFFormat.apply(localId, request);
        localId = RecordUtils.getIdWithoutExtension(localId);
        LOGGER.debug("datasetId : {} , localId : {}, RDF format : {}", datasetId, localId, rdfFormat);
        return createResponse(datasetId, localId, rdfFormat);
    }


    private ResponseEntity<String> createResponse(String collectionId, String recordId, RdfFormat rdfFormat) throws EuropeanaApiException, IOException {
        String about = RecordUtils.buildRecordId(collectionId, recordId);
//        ProvidedCHO testRecord = new TestDataBuilder().newRecord();
//        recordService.saveRecord(testRecord);
//        LOGGER.info("saved Data");

        Optional<ProvidedCHO> record = recordService.getRecord(about);
        if (record.isEmpty()) {
            throw new RecordDoesNotExistsException(about);
        }

        OutputStream stream = new ByteArrayOutputStream();
        formatHandlerRegistry.get(rdfFormat).write(record.get(), stream );
        return ResponseEntity.status(HttpStatus.OK).body(stream.toString());
    }
}
