package eu.europeana.api.error;

import jakarta.servlet.http.HttpServletRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ResponseUtils {

    private ResponseUtils() {
        // hide implicit public constructor
    }

    /**
     * Gets a String representation of an Exception's stacktrace
     *
     * @param throwable Throwable instance
     * @return String representation of stacktrace
     */
    public static String getExceptionStackTrace(Throwable throwable) {
        Writer result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        throwable.printStackTrace(printWriter);
        return result.toString();
    }


    /**
     * Gets the URI path in the request, appending  any query parameters
     *
     * @param httpRequest Http request
     * @return String containing request URI and query parameterss
     */
    public static String getRequestPath(HttpServletRequest httpRequest) {
        return
                httpRequest.getQueryString() == null ? String.valueOf(httpRequest.getRequestURL()) :
                        String.valueOf(httpRequest.getRequestURL().append("?").append(httpRequest.getQueryString()));
    }
}
