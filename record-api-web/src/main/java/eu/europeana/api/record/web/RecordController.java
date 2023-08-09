package eu.europeana.api.record.web;

import eu.europeana.api.commons.web.http.HttpHeaders;
import eu.europeana.api.record.Record;
import eu.europeana.api.record.service.RecordService;
import io.swagger.annotations.ApiOperation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<String> registerEntity(
            @RequestBody Record record, HttpServletRequest request) throws Exception {


        System.out.println(record);
        recordService.saveRecord(record);

        return null;
    }

}
