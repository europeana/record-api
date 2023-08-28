package eu.europeana.api.record.exception;

import eu.europeana.api.record.model.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler that catches all errors and logs the interesting ones
 *
 * @author Srishti Singh
 * Created on 24-08-2023
 */
@RestControllerAdvice
@ResponseBody
class GlobalExceptionHandler {

    private static final Logger LOG = LogManager.getLogger(GlobalExceptionHandler.class);

     //  with Spring boot 3 (and Spring Framework 6) require a baseline of Jakarte EE 10
    //  You cannot use it with Java EE or Jakarte EE versions below that.
    //  You have to remove the explicit dependency on jakarta.servlet-api from your pom.xml.
    //  Java Servlet 4 is below the baseline and in particular still uses the package names starting with javax.servlet.
    //  If you remove the explicit dependency, Spring will pull in transitively the correct one.
    //  You then need to replace all imports starting with javax.servlet with javax replaced by jakarta

    protected void logException(RecordApiException e) {
        if (e.doLog()) {
            if (e.doLogStacktrace()) {
                LOG.error("Caught exception", e);
            } else {
                LOG.error("Caught exception: {}", e.getMessage());
            }
        }

    }


    @ExceptionHandler
    public ResponseEntity<ApiErrorResponse> handleBaseException(RecordApiException e, HttpServletRequest httpRequest) {
        this.logException(e);
        ApiErrorResponse response = (new ApiErrorResponse.Builder(httpRequest, e, false)).setStatus(e.getResponseStatus().value()).setError
                        (e.getResponseStatus().getReasonPhrase()).setMessage(e.doExposeMessage() ? e.getMessage() : null).setCode(e.getErrorCode()).build();
        return ResponseEntity.status(e.getResponseStatus()).headers(this.createHttpHeaders()).body(response);
    }

    protected HttpHeaders createHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}