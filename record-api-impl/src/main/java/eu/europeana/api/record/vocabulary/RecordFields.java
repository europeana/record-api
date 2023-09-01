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
    public static final String PREF_LABEL = "prefLabel";
    public static final String IS_SHOWN_BY = "isShownBy";
    public static final String HAS_VIEWS = "hasViews";

    public static final String PROVIDED_CHO = "providedCHO";

    public static final String AGGREGATION = "Aggregation";

    // fields for display
    public static final String AGGREGATIONS = "aggregations";
    public static final String WEB_RESOURCES = "webResources";
    public static final String AGENTS = "agents";
    public static final String PROXIES = "proxies";
    public static final String IS_AGGREGATED_BY = "isAggregatedBy";

    // fields used to save multiple type values like craetor or description
    public static final String VALUE = "value";
    public static final String LANGUAGE = "language";
    public static final String REFERENCED_OBJECT = "referencedObject";





}
