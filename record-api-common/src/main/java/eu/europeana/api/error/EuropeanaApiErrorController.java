package eu.europeana.api.error;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import jakarta.servlet.http.HttpServletRequest;

import javax.annotation.PostConstruct;;

@RestController
public class EuropeanaApiErrorController extends AbstractErrorController {

    private final EuropeanaApiErrorAttributes errorAttributes;
    private ErrorAttributeOptions errorAttributeOptions = ErrorAttributeOptions.defaults();

    @Value("${server.error.include-message}")
    private ErrorProperties.IncludeAttribute includeMessage;
    @Value("${server.error.include-exception}")
    private Boolean includeException;
    @Value("${server.error.include-stacktrace}")
    private ErrorProperties.IncludeStacktrace includeStacktrace;

    /**
     * Initialize a new controller to handle error output
     * @param errorAttributes auto-wired ApiErrorAttributes (error fields)
     */
    @Autowired
    public EuropeanaApiErrorController(EuropeanaApiErrorAttributes errorAttributes) {
        super(errorAttributes);
        this.errorAttributes = errorAttributes;
    }

    @PostConstruct
    private void init() {
        if (ErrorProperties.IncludeAttribute.ALWAYS.equals(includeMessage)) {
            errorAttributeOptions = ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE);
        }
        if (includeException) {
            errorAttributeOptions = ErrorAttributeOptions.of(ErrorAttributeOptions.Include.EXCEPTION);
        }
        if (ErrorProperties.IncludeStacktrace.ALWAYS.equals(includeStacktrace)) {
            errorAttributeOptions = ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE);
        }
    }

//    @Override
//    public String getErrorPath() {
//        return "/error";
//    }

    /**
     * Override default Spring-Boot error endpoint
     * @param request incoming request
     * @return error object to serialize
     */
    @GetMapping("/error")
    public Map<String, Object> error(HttpServletRequest request) {
        ErrorAttributeOptions options = errorAttributeOptions;
        if (ErrorProperties.IncludeAttribute.ON_PARAM.equals(includeMessage) && this.getMessageParameter(request)) {
            options = errorAttributeOptions.including(ErrorAttributeOptions.Include.MESSAGE);
        }
        if (ErrorProperties.IncludeStacktrace.ON_PARAM.equals(includeStacktrace) && this.getTraceParameter(request)) {
            options = errorAttributeOptions.including(ErrorAttributeOptions.Include.STACK_TRACE);
        }
        WebRequest webRequest = new ServletWebRequest(request);
        return this.errorAttributes.getErrorAttributes(webRequest, options);
    }

}