package eu.europeana.api.error;


import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.WebRequest;

import java.time.OffsetDateTime;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class EuropeanaApiErrorAttributes extends DefaultErrorAttributes {

    /**
     * Used by Spring to display errors with no custom handler.
     * Since we explicitly return {@link EuropeanaApiErrorResponse} on errors within controllers, this method is only invoked when
     * a request isn't handled by any controller.
     */
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions sbOptions) {
        final Map<String, Object> defaultErrorAttributes = super.getErrorAttributes(webRequest, sbOptions);

        // use LinkedHashMap to guarantee display order
        LinkedHashMap<String, Object> europeanaErrorAttributes = new LinkedHashMap<>();
        europeanaErrorAttributes.put("success", false);
        europeanaErrorAttributes.put("status", defaultErrorAttributes.get("status"));
        europeanaErrorAttributes.put("error", defaultErrorAttributes.get("error"));
        // message not shown
        europeanaErrorAttributes.put("timestamp", OffsetDateTime.now());
        addPathRequestParameters(europeanaErrorAttributes, webRequest);
        return europeanaErrorAttributes;
    }


    /**
     * Spring errors only return the error path and not the parameters, so we add those ourselves.
     * The original parameter string is not available in WebRequest so we rebuild it.
     */
    private void addPathRequestParameters(Map<String, Object> errorAttributes, WebRequest webRequest) {
        Iterator<String> it = webRequest.getParameterNames();
        StringBuilder s = new StringBuilder();
        while (it.hasNext()) {
            if (s.length() == 0) {
                s.append('?');
            } else {
                s.append("&");
            }
            String paramName = it.next();
            s.append(paramName);
            String paramValue = webRequest.getParameter(paramName);
            if (StringUtils.hasText(paramValue)) {
                s.append("=").append(paramValue);
            }
        }

        if (s.length() > 0) {
            errorAttributes.put("path", errorAttributes.get("path") + s.toString());
        }
    }
}
