package eu.europeana.api.record.utils;

import eu.europeana.api.format.RdfFormat;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.function.BiFunction;

import static eu.europeana.api.record.utils.RecordConstants.BASE_URL;
public class RecordUtils {

    public static String buildRecordId(String collectionId, String recordId) {
        return BASE_URL + collectionId + "/" + recordId;
    }

    public static BiFunction<String, HttpServletRequest, RdfFormat> getRDFFormat = (localId, request) -> {
        RdfFormat format = null;
        if (StringUtils.contains(localId, ".")) {
            format = RdfFormat.getFormatByExtension(StringUtils.substringAfterLast(localId, "."));
        } else {
            // check for Accept Header
            format = RdfFormat.getFormatByMediaType(request.getHeader("Accept"));
        }
        // if format is still null -> no .extension and no Accept header, default it to JsonLD format
        if (format == null) {
            format = RdfFormat.JSONLD;
        }
        return format;
    };
}

