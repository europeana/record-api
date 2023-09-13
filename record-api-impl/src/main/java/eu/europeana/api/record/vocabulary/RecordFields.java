package eu.europeana.api.record.vocabulary;

/**
 * Class to contain all the record field values of Record API
 * @author srishti singh
 * @since 14 August 2023
 */
public interface RecordFields {

    public static final String CONTEXT = "@context";
    public static final String EDM_CONTEXT = "https://www.europeana.eu/schemas/context/edm.jsonld";

    public static final String NON_LANGUAGE_TAGGED = "def";
    public static final String NONE = "@none";

    public static final String ID = "id";
    public static final String TYPE = "type";
    public static final String PREF_LABELS = "prefLabels";
    public static final String IS_SHOWN_BY = "isShownBy";
    public static final String IS_SHOWN_AT = "isShownAt";
    public static final String VIEWS = "views";
    public static final String MIME_TYPE = "mimetype";
    public static final String FILE_BYTE_SIZE = "fileByteSize";

    public static final String IS_AGGREGATED_BY = "isAggregatedBy";

    public static final String PROXIES = "proxies";
    public static final String PROXY_IN = "proxyIn";
    public static final String PROXY_FOR = "proxyFor";

    public static final String TITLE = "title";
    public static final String ALTERNATIVE_TITLE = "alternativeTitle";
    public static final String DESCRIPTION = "description";
    public static final String CREATOR = "creator";
    public static final String IDENTIFIER = "identifier";

    public static final String MONGO_TECHMETA        = "techMeta";
    public static final String MONGO_OBJECT        = "object";

}
