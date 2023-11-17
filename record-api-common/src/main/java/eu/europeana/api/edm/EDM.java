package eu.europeana.api.edm;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 14 Apr 2016
 */
public interface EDM 
{
    public static final String PREFIX = "edm";
    public static final String NS
        = "http://www.europeana.eu/schemas/edm/";
    public static final NamespaceDeclaration DECL = new NamespaceDeclaration(PREFIX, NS);

    public static final String ProvidedCHO            = "ProvidedCHO";
    public static final String EuropeanaAggregation   = "EuropeanaAggregation";
    public static final String WebResource            = "WebResource";
    public static final String Agent                  = "Agent";
    public static final String Place                  = "Place";
    public static final String TimeSpan               = "TimeSpan";
    public static final String Event                  = "Event";
    public static final String PhysicalThing          = "PhysicalThing";
    public static final String NonInformationResource = "NonInformationResource";
    public static final String FullTextResource       = "FullTextResource";

    public static final String aggregatedCHO        = "aggregatedCHO";
    public static final String begin                = "begin";
    public static final String country              = "country";
    public static final String datasetName          = "datasetName";
    public static final String completeness         = "completeness";
    public static final String currentLocation      = "currentLocation";
    public static final String dataProvider         = "dataProvider";
    public static final String end                  = "end";
    @Deprecated
    public static final String europeanaProxy       = "europeanaProxy";
    public static final String hasMet               = "hasMet";
    public static final String hasType              = "hasType";
    public static final String hasView              = "hasView";
    public static final String incorporates         = "incorporates";
    public static final String intermediateProvider = "intermediateProvider";
    public static final String isDerivativeOf       = "isDerivativeOf";
    public static final String isNextInSequence     = "isNextInSequence";
    public static final String isRelatedTo          = "isRelatedTo";
    public static final String isRepresentationOf   = "isRepresentationOf";
    public static final String isShownBy            = "isShownBy";
    public static final String isShownAt            = "isShownAt";
    public static final String isSimilarTo          = "isSimilarTo";
    public static final String isSuccessorOf        = "isSuccessorOf";
    public static final String landingPage          = "landingPage";
    public static final String realizes             = "realizes";
    public static final String rights               = "rights";
    public static final String language             = "language";
    public static final String object               = "object";
    public static final String preview              = "preview";
    public static final String provider             = "provider";
    public static final String type                 = "type";
    public static final String ugc                  = "ugc";
    @Deprecated
    public static final String unstored             = "unstored";
    public static final String wasPresentAt         = "wasPresentAt";
    public static final String year                 = "year";

    //Technical Metadata
    public static final String codecName            = "codecName";
    public static final String componentColor       = "componentColor";
    public static final String hasColorSpace        = "hasColorSpace";
    public static final String spatialResolution    = "spatialResolution";
}
