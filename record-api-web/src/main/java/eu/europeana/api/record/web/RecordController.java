package eu.europeana.api.record.web;

import eu.europeana.api.commons.web.http.HttpHeaders;
import eu.europeana.api.record.impl.RecordImpl;
import eu.europeana.api.record.model.Record;
import eu.europeana.api.record.service.RecordService;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class RecordController {

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
            @RequestBody RecordImpl record, HttpServletRequest request) throws Exception {

        // TODO validation for language tagged values to be single

        System.out.println(record.getProxies().get(0));
        System.out.println(record.getProxies().get(1));

        System.out.println(record.getAggregation());
        System.out.println(record.getAgents().get(0));
        System.out.println(record.getAgents().get(1));

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

        String about = "http://data.europeana.eu/item/" + collectionId + "/" + recordId;
        System.out.println("about " +about);
        RecordImpl record = (RecordImpl) recordService.getRecord(about);

        System.out.println(record.getProxies().get(0));
        System.out.println(record.getProxies().get(1));

        System.out.println(record.getAggregation());
        System.out.println(record.getAgents().get(0));
        System.out.println(record.getAgents().get(1));

        return new ResponseEntity(HttpStatus.OK);
    }



}
