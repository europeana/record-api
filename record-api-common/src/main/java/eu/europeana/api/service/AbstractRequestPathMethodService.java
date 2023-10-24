package eu.europeana.api.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 *  This class collates all the HTTP methods that are implemented for all unique request
 *  patterns within a Spring Boot application.
 *
 *  This is useful for populating the HTTP Allow header, when generating API responses.
 *
 *  To use:
 *   - extend this class within your project;
 *   - instantiate the subclass with the Spring WebApplicationContext;
 *   - pass the instance as an argument to BaseRestController.createAllowHeader()
 * */
public abstract class AbstractRequestPathMethodService implements InitializingBean {
    /**
     * Map request urls to Http request methods (implemented across the application) with the url
     * pattern.
     */
    private final Map<String, Set<String>> requestPathMethodMap = new HashMap<>();

    protected final WebApplicationContext applicationContext;

    protected AbstractRequestPathMethodService(
            WebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /** Populate request url pattern - request methods map */
    @Override
    public void afterPropertiesSet() {
        RequestMappingHandlerMapping mapping =
                applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();

        for (RequestMappingInfo info : handlerMethods.keySet()) {
            PatternsRequestCondition p = info.getPatternsCondition();

            // get all request methods for this pattern
            final Set<String> requestMethods =
                    info.getMethodsCondition().getMethods().stream()
                            .map(Enum::toString)
                            .collect(Collectors.toSet());

            for (String url : p.getPatterns()) {
                addToMap(requestPathMethodMap, url, requestMethods);
            }
        }
    }

    /**
     * Gets request methods that are implemented across the application for this request's URL
     * pattern. The return value from this method is used when setting the Allow header in API
     * responses.
     *
     * @param request {@link HttpServletRequest} instance
     * @return Optional containing matching request methods, or empty optional if no match could be
     *     determined.
     */
    public Optional<String> getMethodsForRequestPattern(HttpServletRequest request) {
        Object patternAttribute = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);

        if (patternAttribute == null) {
            return Optional.empty();
        }

        Set<String> requestMethods = requestPathMethodMap.get(patternAttribute.toString());
        String methods = String.join(",", requestMethods);
        return Optional.of(methods);
    }

    /** This method adds url patterns and their matching request methods to the map. */
    private void addToMap(
            Map<String, Set<String>> map, String urlPattern, Set<String> requestMethods) {
        if (!map.containsKey(urlPattern)) {
            map.put(urlPattern, requestMethods);
            return;
        }

        // Each pattern can be used across multiple request handlers, so we append here.
        Set<String> existing = map.get(urlPattern);
        existing.addAll(requestMethods);
    }
}
