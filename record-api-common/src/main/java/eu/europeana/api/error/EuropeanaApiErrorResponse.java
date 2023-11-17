package eu.europeana.api.error;

import com.fasterxml.jackson.annotation.*;

import java.util.List;
import java.time.OffsetDateTime;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import static eu.europeana.api.error.EuropeanaErrorConstants.*;

/**
 * This class contains fields to be returned by APIs when an error occurs within the application.
 *
 */
@JsonPropertyOrder({CONTEXT, TYPE, SUCCESS, STATUS, CODE, ERROR, MESSAGE, SEE_ALSO, TIMESTAMP, PATH, TRACE})
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class EuropeanaApiErrorResponse {

    @JsonProperty(CONTEXT)
    private final String context = ERROR_CONTEXT;

    @JsonProperty(TYPE)
    private final  String type= ERROR_TYPE;

    @JsonProperty(SUCCESS)
    private final boolean success = false;

    @JsonProperty(STATUS)
    private final int status;

    @JsonProperty(ERROR)
    private final String error;

    @JsonProperty(MESSAGE)
    private final String message;

    @JsonProperty(SEE_ALSO)
    private final String seeAlso = SEE_ALSO_VALUE;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    private final OffsetDateTime timestamp = OffsetDateTime.now();

    @JsonProperty(TRACE)
    private final String trace;

    @JsonProperty(PATH)
    private final String path;

    @JsonProperty(CODE)
    private final String code;

    private EuropeanaApiErrorResponse(int status, String error, String message, String trace, String path, String code) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.trace = trace;
        this.path = path;
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }

    public String getTrace() {
        return trace;
    }

    public String getPath() {
        return path;
    }

    public String getCode() {
        return code;
    }

    public String getContext() {
        return context;
    }

    public String getType() {
        return type;
    }

    public String getSeeAlso() {
        return seeAlso;
    }

    public static class Builder {
        private int status;
        private String message;
        private String error;
        private String trace;
        private final String path;
        private String code;

        public Builder(HttpServletRequest httpRequest, Exception e, boolean stacktraceEnabled) {
            this.path = ResponseUtils.getRequestPath(httpRequest);
            boolean includeErrorStack = false;
            String profileParamString = httpRequest.getParameter(QUERY_PARAM_PROFILE);
            // check if profile contains debug
            if (StringUtils.hasLength(profileParamString)) {
                includeErrorStack = List.of(profileParamString.split(QUERY_PARAM_PROFILE_SEPARATOR))
                        .contains(PROFILE_DEBUG);
            }
            if (stacktraceEnabled && includeErrorStack) {
                this.trace = ResponseUtils.getExceptionStackTrace(e);
            }
        }

        public Builder setStatus(int status) {
            this.status = status;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setError(String error) {
            this.error = error;
            return this;
        }

        public Builder setCode(String code) {
            this.code = code;
            return this;
        }

        public EuropeanaApiErrorResponse build() {
            return new EuropeanaApiErrorResponse(status, error, message, trace, path, code);
        }
    }
}
