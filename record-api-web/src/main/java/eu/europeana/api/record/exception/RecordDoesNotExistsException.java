package eu.europeana.api.record.exception;

import org.springframework.http.HttpStatus;

public class RecordDoesNotExistsException  extends RecordApiException {

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
