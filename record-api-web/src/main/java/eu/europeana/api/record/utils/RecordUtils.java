package eu.europeana.api.record.utils;

import eu.europeana.api.format.RdfFormat;
import eu.europeana.api.record.model.RecordRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static eu.europeana.api.record.utils.RecordConstants.BASE_URL;
public class RecordUtils {

    private RecordUtils(){
    }


    /**
     * Build record id with collectionId and localId
     * @param collectionId collection id
     * @param localId local Id
     * @return
     */
    public static String buildRecordId(String collectionId, String localId) {
        return BASE_URL + "/" + collectionId + "/" + localId;
    }

    /**
     * Return list of record ids after processing the urls
     * @param urls list of urls. Can contain - '/DATASET_ID/LOCAL_ID' OR 'http://data.europeana.eu/item//DATASET_ID/LOCAL_ID'
     * @return list of correct record id urls
     */
    public static List<String> buildRecordIds(List<String> urls) {
        return  urls.stream().map(RecordUtils :: buildRecordId).collect(Collectors.toList());
    }

    /**
     * Build record id with url -
     *                 "/DATASET_ID/LOCAL_ID" or "DATASET_ID/LOCAL_ID"
     * @param url url passed
     * @return record id url
     */
    private static String buildRecordId(String url) {
        if (!StringUtils.startsWith(url, BASE_URL)) {
            return StringUtils.startsWith(url, "/") ? BASE_URL + url : BASE_URL + "/" + url;
        }
        return url;
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

