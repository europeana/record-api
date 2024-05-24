package eu.europeana.api.config;

// TODO move this to a common location used in mongo and web module
public class AppConfigConstants {

    // v3 beans
    public static final String BEAN_RECORD_DATA_STORE = "recordDataStore";
    public static final String BEAN_RECORD_REPO = "recordRepo";
    public static final String BEAN_RECORD_SERVICE = "recordService";

    // v2 beans
    public static final String BEAN_FORMAT_WRITER_V2_JSON = "recordJsonV2Writer";


    // serialiser beans
    public static final String BEAN_JSON_MAPPER = "recordJsonMapper";


    // jena beans
    public static final String BEAN_CODEC_REGISTRY = "codecRegistry";
    public static final String BEAN_NAMESPACE_RESOLVER= "namespaceResolver";
    public static final String BEAN_DEFAULT_URI_RESOLVER= "defaultUriResolver";
    public static final String BEAN_RECORD_TEMPLATE_LIBRARY = "recordApiTemplateLibrary";
    
    public static final String BEAN_FORMAT_WRITER_TURTLE = "recordWriterTurtle";
    public static final String BEAN_FORMAT_WRITER_N3     = "recordWriterN3";
    public static final String BEAN_FORMAT_WRITER_NT     = "recordWriterNt";
    public static final String BEAN_FORMAT_WRITER_JSONLD = "recordWriterJsonLD";
    public static final String BEAN_FORMAT_WRITER_XML    = "recordWriterXml";


    // media config beans
    public static final String BEAN_MEDIA_TYPES = "msMediaTypes";


}