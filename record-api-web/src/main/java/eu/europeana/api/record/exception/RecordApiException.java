package eu.europeana.api.record.exception;

import org.springframework.http.HttpStatus;

public class RecordApiException extends Exception {

    private static final long serialVersionUID = -1354471712894853562L;
    private final String errorCode;

    public RecordApiException(String msg, Throwable t) {
        this(msg, (String)null, t);
    }

    public RecordApiException(String msg, String errorCode, Throwable t) {
        super(msg, t);
        this.errorCode = errorCode;
    }

    public RecordApiException(String msg) {
        super(msg);
        this.errorCode = null;
    }

    public RecordApiException(String msg, String errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    public boolean doLog() {
        return true;
    }

    public boolean doLogStacktrace() {
        return true;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public boolean doExposeMessage() {
        return true;
    }

    public HttpStatus getResponseStatus() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
