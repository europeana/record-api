package eu.europeana.api.record.exception;

import eu.europeana.api.error.EuropeanaApiException;
import org.springframework.http.HttpStatus;

/** Exception thrown when an error occurs due to bad user input. */
public class HttpBadRequestException extends EuropeanaApiException {

    public HttpBadRequestException(String msg) {
        super(msg);
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
