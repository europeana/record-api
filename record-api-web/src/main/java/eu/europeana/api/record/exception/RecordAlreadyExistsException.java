package eu.europeana.api.record.exception;

import eu.europeana.api.error.EuropeanaApiException;
import org.springframework.http.HttpStatus;

/** Exception thrown when a record already exists in the DB. */
public class RecordAlreadyExistsException  extends EuropeanaApiException {

    public RecordAlreadyExistsException(String about) {
        super("Record already exists for '" + about);
    }

    @Override
    public boolean doLogStacktrace() {
        return false;
    }

    @Override
    public HttpStatus getResponseStatus() {
        return HttpStatus.BAD_REQUEST;
    }
}
