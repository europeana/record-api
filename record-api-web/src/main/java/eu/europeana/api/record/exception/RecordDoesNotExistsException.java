package eu.europeana.api.record.exception;

import eu.europeana.api.error.EuropeanaApiException;
import org.springframework.http.HttpStatus;

public class RecordDoesNotExistsException extends EuropeanaApiException {

    public RecordDoesNotExistsException(String about) {
        super("Record does not exists for '" + about);
    }

    @Override
    public boolean doLogStacktrace() {
        return false;
    }

    @Override
    public HttpStatus getResponseStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
