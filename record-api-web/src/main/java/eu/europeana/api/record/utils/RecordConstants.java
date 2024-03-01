package eu.europeana.api.record.utils;

import eu.europeana.api.commons.web.http.HttpHeaders;
import org.springframework.http.MediaType;

/**
 * Class to contains all the constants value used in the Record API
 * @author srishti singh
 * @since 27 August 2023
 */
public class RecordConstants {

    public static final String BASE_URL = "http://data.europeana.eu/item";
    public static final String ACCEPT  = "Accept=";

    // TTL headers
    public static final String MEDIA_TYPE_TURTLE_TEXT  = "text/turtle";
    public static final String MEDIA_TYPE_TURTLE       = "application/turtle";
    public static final String MEDIA_TYPE_TURTLE_X     = "application/x-turtle";

    // Accept Headers for retrieval endpoint
    public static final String ACCEPT_HEADER_JSONLD = ACCEPT + HttpHeaders.CONTENT_TYPE_JSONLD;
    public static final String ACCEPT_HEADER_JSON = ACCEPT + MediaType.APPLICATION_JSON_VALUE;

    public static final String ACCEPT_HEADER_APPLICATION_RDF_XML = ACCEPT + HttpHeaders.CONTENT_TYPE_APPLICATION_RDF_XML;
    public static final String ACCEPT_HEADER_RDF_XML = ACCEPT + HttpHeaders.CONTENT_TYPE_RDF_XML;
    public static final String ACCEPT_HEADER_APPLICATION_XML = ACCEPT + MediaType.APPLICATION_XML_VALUE;
    public static final String ACCEPT_HEADER_APPLICATION_TEXT_XML = ACCEPT + MediaType.TEXT_XML_VALUE;

    public static final String ACCEPT_HEADER_APPLICATION_TURTLE_TEXT = ACCEPT + MEDIA_TYPE_TURTLE_TEXT;
    public static final String ACCEPT_HEADER_APPLICATION_TURTLE = ACCEPT + MEDIA_TYPE_TURTLE;
    public static final String ACCEPT_HEADER_APPLICATION_TURTLE_X  = ACCEPT + MEDIA_TYPE_TURTLE_X;
}
