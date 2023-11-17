package eu.europeana.api.record.exception;

import eu.europeana.api.service.EuropeanaGlobalExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler that catches all errors and logs the interesting ones
 *
 * @author Srishti Singh
 * Created on 24-08-2023
 */
@RestControllerAdvice
class GlobalExceptionHandler extends EuropeanaGlobalExceptionHandler {

}