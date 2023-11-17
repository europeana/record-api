package eu.europeana.api.config;

// TODO move this to a common location used in mongo and web module
public class AppConfigConstants {
    public static final String BEAN_RECORD_DATA_STORE = "recordDataStore";
    public static final String BEAN_RECORD_REPO = "recordRepo";
    public static final String BEAN_RECORD_SERVICE = "recordService";
    public static final String BEAN_RECORD_JSONLD_SERIALIZER = "recordJsonldWriter";
    public static final String BEAN_RECORD_XML_SERIALIZER = "recordXmlWriter";
    public static final String BEAN_RECORD_JENA_FORMAT_SERIALIZER = "recordJenaFormatWriter";


    // serialiser beans
    public static final String BEAN_JSON_MAPPER = "recordJsonMapper";


    // jena beans
    public static final String BEAN_CODEC_REGISTRY = "codecRegistry";
    public static final String BEAN_NAMESPACE_RESOLVER= "namespaceResolver";
    public static final String BEAN_DEFAULT_URI_RESOLVER= "defaultUriResolver";
    public static final String BEAN_RECORD_TEMPLATE_LIBRARY = "recordApiTemplateLibrary";

    // media config beans
    public static final String BEAN_MEDIA_TYPES = "msMediaTypes";


}