package eu.europeana.api.record.vocabulary;

/**
 * Class to contain all the record field values of Record API
 * @author srishti singh
 * @since 14 August 2023
 */
public interface RecordFields {

    public static final String CONTEXT = "@context";
    public static final String EDM_CONTEXT = "http://www.europeana.eu/schemas/context/edm.jsonld";

    public static final String NON_LANGUAGE_TAGGED = "def";
    public static final String NONE = "@none";


    public static final String ID = "id";
    public static final String TYPE = "type";
    public static final String PREF_LABEL = "prefLabel";
    public static final String IS_SHOWN_BY = "isShownBy";
    public static final String HAS_VIEWS = "hasViews";

    public static final String PROVIDED_CHO = "providedCHO";

    public static final String AGGREGATION = "Aggregation";
    public static final String IS_AGGREGATED_BY = "isAggregatedBy";




}
