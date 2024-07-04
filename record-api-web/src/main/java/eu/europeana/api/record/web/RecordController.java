package eu.europeana.api.record.web;

import dev.morphia.internal.DatastoreHolder;
import dev.morphia.query.MorphiaCursor;
import eu.europeana.api.commons.web.http.HttpHeaders;
import eu.europeana.api.error.EuropeanaApiException;
import eu.europeana.api.format.RdfFormat;
import eu.europeana.api.record.exception.RecordDoesNotExistsException;
import eu.europeana.api.record.io.FormatHandlerRegistry;
import eu.europeana.api.record.model.ProvidedCHO;
import eu.europeana.api.record.model.RecordRequest;
import eu.europeana.api.record.profile.ViewProfileRegistry;
import eu.europeana.api.record.service.RecordService;
import eu.europeana.api.record.utils.RecordUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;


import java.io.*;
import java.util.*;

import static eu.europeana.api.record.utils.RecordConstants.*;

@Tag(
        name = "Record API rest endpoints"
//        description = "Record API retrieval in different formats"

)
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

    /**
     * Retrieves the Record in the format requested
     * Format is requested two-way - either as an extension in the localID or the Accept Header
     * If present in localId : example - UEDIN_214.xml or UEDIN_214.json Or a valid Accept header.
     *                                   Extensions are given preference over Accept header values
     *                                   If both are provided then default format is set to JSONLD
     * @param datasetId Dataset Id
     * @param localId local id
     * @param request HttpServlet request
     * @return Response Entity with StreamingResponseBody
     * @throws EuropeanaApiException throws generic EuropeanaApiException
     */

    @Operation(
            summary = "retrieveRecord",
            description = "Retrieve record in json/json-ld, XML, Turtle, N3, NT "
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 OK"
    )
    @GetMapping(
            value = {
                    "/record/v3/{datasetId}/{localId}",
            },
            headers = { ACCEPT_HEADER_JSONLD, ACCEPT_HEADER_JSON,
                    ACCEPT_HEADER_APPLICATION_TEXT_XML, ACCEPT_HEADER_RDF_XML, ACCEPT_HEADER_APPLICATION_RDF_XML, ACCEPT_HEADER_APPLICATION_XML,
                    ACCEPT_HEADER_APPLICATION_TURTLE_TEXT, ACCEPT_HEADER_APPLICATION_TURTLE, ACCEPT_HEADER_APPLICATION_TURTLE_X,
                    ACCEPT_HEADER_APPLICATION_N3, ACCEPT_HEADER_APPLICATION_N3_RDF, ACCEPT_HEADER_APPLICATION_N3_TEXT,
                    ACCEPT_HEADER_APPLICATION_NT, ACCEPT_HEADER_APPLICATION_NT_TEXT, ACCEPT_HEADER_APPLICATION_NT_TRIPLES
            },
            produces = {HttpHeaders.CONTENT_TYPE_JSONLD, MediaType.APPLICATION_JSON_VALUE,
                    MediaType.TEXT_XML_VALUE, HttpHeaders.CONTENT_TYPE_RDF_XML, HttpHeaders.CONTENT_TYPE_APPLICATION_RDF_XML, MediaType.APPLICATION_XML_VALUE,
                    MEDIA_TYPE_TURTLE_TEXT, MEDIA_TYPE_TURTLE, MEDIA_TYPE_TURTLE_X,
                    MEDIA_TYPE_N3_TEXT, MEDIA_TYPE_N3_RDF, MEDIA_TYPE_N3,
                    MEDIA_TYPE_TURTLE_TEXT, MEDIA_TYPE_TURTLE, MEDIA_TYPE_TURTLE_X})
    public ResponseEntity<StreamingResponseBody> retrieveRecord(
            @PathVariable String datasetId,
            @PathVariable String localId,
            @RequestParam(value = "profile", required = false)
            String profile,
            HttpServletRequest request) throws EuropeanaApiException {
        return createResponse(datasetId, localId, profile, request);
    }

    /**
     * Retrieve Multiple records. This is currently only available for json format
     * @param urls urls for which records to be fetched
     * @param request http request
     * @return List of records
     * @throws EuropeanaApiException
     */

    @Operation(
            summary = "retrieveRecords",
            description = "Retrieve mutiple record in json/json-ld"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status 200 OK"
    )
    @PostMapping(value = "/record/v3/retrieve",
            produces = {HttpHeaders.CONTENT_TYPE_JSONLD_UTF8, HttpHeaders.CONTENT_TYPE_JSON_UTF8})
    public ResponseEntity<StreamingResponseBody> retrieveRecords(
            @RequestBody List<String> urls, HttpServletRequest request) throws EuropeanaApiException {
        return createResponseMultipleRecords(urls);
    }

    private ResponseEntity<StreamingResponseBody> createResponse(String datasetId, String localId, String profile, HttpServletRequest request) throws EuropeanaApiException {
        RecordRequest recordRequest = RecordUtils.getRecordRequest(datasetId, localId, profile, request);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("datasetId : {} , localId : {}, RDF format : {}", datasetId, recordRequest.getLocalId(), recordRequest.getRdfFormat());
        }
        Optional<ProvidedCHO> record = recordService.getRecord(
                recordRequest.getAbout(),
                ViewProfileRegistry.getProjection(recordRequest.getProfiles()));
        if (!record.isPresent()) {
            throw new RecordDoesNotExistsException(recordRequest.getAbout());
        }
        ProvidedCHO providedCHO = record.get();
        StreamingResponseBody responseBody = new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream out) throws IOException {
                //this is needed because Jackson serialises the response in a new thread
                recordService.init();
                formatHandlerRegistry.get(recordRequest.getRdfFormat()).write(providedCHO, out);
                out.flush();
            }
        };
        return new ResponseEntity<>(responseBody, RecordUtils.getHeaders(request, recordRequest), HttpStatus.OK);
    }

    private ResponseEntity<StreamingResponseBody> createResponseMultipleRecords(List<String> urls) throws EuropeanaApiException {
        List<String> recordIds = RecordUtils.buildRecordIds(urls);
        MorphiaCursor<ProvidedCHO> records = recordService.retrieveMultipleByRecordIds(recordIds);
        if (records.available() == 0) {
            throw new RecordDoesNotExistsException(urls.toString());
        }

        // create response headers
        org.springframework.http.HttpHeaders httpHeaders= new org.springframework.http.HttpHeaders();
        httpHeaders.setContentType(RecordUtils.getMediaTypeObject(RdfFormat.JSONLD));
        StreamingResponseBody responseBody = new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream out) throws IOException {
                try {
                   //this is needed because Jackson serialises the response in a new thread
                    recordService.init(); 
                    formatHandlerRegistry.get(RdfFormat.JSONLD).write(records, records.available(), out);
                    out.flush();
                }
                finally { records.close(); }
            }
        };
        return new ResponseEntity<>(responseBody, httpHeaders, HttpStatus.OK);
    }

}
