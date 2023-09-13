package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.*;
import eu.europeana.api.commons.error.ResponseUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.List;

@JsonPropertyOrder({"success", "status", "error", "message", "timestamp", "path"})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApiErrorResponse {

    @JsonProperty("success")
    private final boolean success = false;

    @JsonProperty("status")
    private final int status;

    @JsonProperty("error")
    private final String error;

    @JsonProperty("trace")
    private final String trace;

    @JsonProperty("message")
    private final String message;

    @JsonFormat(
            pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'"
    )
    private final OffsetDateTime timestamp = OffsetDateTime.now();
    private final String path;
    private final String code;

    private ApiErrorResponse(int status, String error, String message, String trace, String path, String code) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.trace = trace;
        this.path = path;
        this.code = code;
    }

    public String getError() {
        return this.error;
    }

    public boolean isSuccess() {
        return false;
    }

    public int getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

    public OffsetDateTime getTimestamp() {
        return this.timestamp;
    }

    public String getPath() {
        return this.path;
    }

    public String getTrace() {
        return this.trace;
    }

    public String getCode() {
        return this.code;
    }

    public static class Builder {
        private int status;
        private String message;
        private String error;
        private String trace;
        private final String path;
        private String code;

        public Builder(HttpServletRequest httpRequest, Exception e, boolean stacktraceEnabled) {
            this.path = getRequestPath(httpRequest);
            boolean includeErrorStack = false;
            String profileParamString = httpRequest.getParameter("profile");
            if (StringUtils.hasLength(profileParamString)) {
                includeErrorStack = List.of(profileParamString.split(",")).contains("debug");
            }

            if (stacktraceEnabled && includeErrorStack) {
                this.trace = ResponseUtils.getExceptionStackTrace(e);
            }

        }

        public ApiErrorResponse.Builder setStatus(int status) {
            this.status = status;
            return this;
        }

        public ApiErrorResponse.Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public ApiErrorResponse.Builder setError(String error) {
            this.error = error;
            return this;
        }

        public ApiErrorResponse.Builder setCode(String code) {
            this.code = code;
            return this;
        }

        public ApiErrorResponse build() {
            return new ApiErrorResponse(this.status, this.error, this.message, this.trace, this.path, this.code);
        }
    }

    public static String getRequestPath(HttpServletRequest httpRequest) {
        return httpRequest.getQueryString() == null ? String.valueOf(httpRequest.getRequestURL()) : String.valueOf(httpRequest.getRequestURL().append("?").append(httpRequest.getQueryString()));
    }
}

