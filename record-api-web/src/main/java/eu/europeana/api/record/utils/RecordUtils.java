package eu.europeana.api.record.utils;

import eu.europeana.api.format.RdfFormat;
import eu.europeana.api.record.model.RecordRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static eu.europeana.api.record.utils.RecordConstants.BASE_URL;
public class RecordUtils {

    private RecordUtils(){
    }

    public static String buildRecordId(String collectionId, String recordId) {
        return BASE_URL + collectionId + "/" + recordId;
    }

    public static RecordRequest getRecordRequest(String datasetId, String localIdWithExtension, HttpServletRequest request) {
       RecordRequest recordRequest = new RecordRequest();
       RdfFormat format = null;
       if (idHasExtension(localIdWithExtension)) {
           format = RdfFormat.getFormatByExtension(StringUtils.substringAfterLast(localIdWithExtension, "."));
           recordRequest.setHasExtension(true);
           recordRequest.setLocalId(StringUtils.substringBeforeLast(localIdWithExtension, ".")); // remove the extension from the id
       }
       // if format is still empty check for Accept header.
       if (format == null && request.getHeader(HttpHeaders.ACCEPT) != null) {
           format = RdfFormat.getFormatByMediaType(request.getHeader(HttpHeaders.ACCEPT));
       }
       // if format is still null -> no .extension and no Accept header, default it to JsonLD format
       if (format == null) {
           format = RdfFormat.JSONLD;
       }
       // create complete request
       recordRequest.setDatasetId(datasetId);
       if (recordRequest.getLocalId() == null) {
           recordRequest.setLocalId(localIdWithExtension);
       }
       recordRequest.setAbout(buildRecordId(datasetId, recordRequest.getLocalId()));
       recordRequest.setRdfFormat(format);
       return recordRequest;
    }

    /**
     * Retuns true if local Id contains valid extension like .json or .rdf .n3
     * @param localId
     * @return
     */
    private static  boolean idHasExtension(String localId) {
        return StringUtils.contains(localId, ".") &&
                RdfFormat.getFormatByExtension(StringUtils.substringAfterLast(localId, ".")) != null;
    }

    /**
     * Gets the Content-type of the response
     * if we no extension and Accept header value is present. Get Content-type with accept header value
     * Otherwise get it by the RDF Format value
     *
     * @param request
     * @param recordRequest
     * @return
     */
    public static HttpHeaders getHeaders(HttpServletRequest request, RecordRequest recordRequest) {
        HttpHeaders httpHeaders= new HttpHeaders();
        if (!recordRequest.hasExtension() && isValidMediaType(request)) {
            httpHeaders.setContentType(MediaType.valueOf(request.getHeader(HttpHeaders.ACCEPT)));
        } else { // default content-type by default RDF Format
            httpHeaders.setContentType(MediaType.valueOf(recordRequest.getRdfFormat().getMediaType()));
        }
        return  httpHeaders;
    }

    private static boolean isValidMediaType(HttpServletRequest request) {
        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
        return acceptHeader != null && RdfFormat.getFormatByMediaType(acceptHeader) != null;
    }
}

