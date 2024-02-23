package eu.europeana.api.record.web;

import eu.europeana.api.commons.web.http.HttpHeaders;
import eu.europeana.api.error.EuropeanaApiException;
import eu.europeana.api.record.exception.RecordDoesNotExistsException;
import eu.europeana.api.record.io.FormatHandlerRegistry;
import eu.europeana.api.record.model.ProvidedCHO;
import eu.europeana.api.record.model.RecordRequest;
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
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

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


    /**
     * Retrieves the Record in the format requested
     * Format is requested two way - either as an extension in the localID or the Accept Header
     * If present in localId : example - UEDIN_214.xml or UEDIN_214.json Or a valid Accept header.
     *                                   Extensions are given preference over Accept header values
     *                                   If both are provided then default format is set to JSONLD
     * @param datasetId Dataset Id
     * @param localId local id
     * @param request HttpServlet request
     * @return Response Entity with StreamingResponseBody
     * @throws EuropeanaApiException
     * @throws IOException
     */
    @ApiOperation(
            value = "Retrieve a record",
            nickname = "retrieveRecord",
            response = StreamingResponseBody.class)
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
    public ResponseEntity<StreamingResponseBody> retrieveRecord(
            @PathVariable String datasetId,
            @PathVariable String localId,
            HttpServletRequest request) throws EuropeanaApiException {
        return createResponse(datasetId, localId, request);
    }

    private ResponseEntity<StreamingResponseBody> createResponse(String datasetId, String localId, HttpServletRequest request) throws EuropeanaApiException {
        RecordRequest recordRequest = RecordUtils.getRecordRequest(datasetId, localId, request);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("datasetId : {} , localId : {}, RDF format : {}", datasetId, recordRequest.getLocalId(), recordRequest.getRdfFormat());
        }
        Optional<ProvidedCHO> record = recordService.getRecord(recordRequest.getAbout());
        if (!record.isPresent()) {
            throw new RecordDoesNotExistsException(recordRequest.getAbout());
        }
        StreamingResponseBody responseBody = new StreamingResponseBody() {
            @Override
            public void writeTo(OutputStream out) throws IOException {
                formatHandlerRegistry.get(recordRequest.getRdfFormat()).write(record.get(), out);
                out.flush();
                }
        };
        return new ResponseEntity<>(responseBody, RecordUtils.getHeaders(request, recordRequest), HttpStatus.OK);
    }
}
