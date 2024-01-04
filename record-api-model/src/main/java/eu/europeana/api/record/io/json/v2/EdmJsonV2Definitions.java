/**
 * 
 */
package eu.europeana.api.record.io.json.v2;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;

import eu.europeana.api.edm.NamespaceDeclaration;
import eu.europeana.api.edm.Namespaces;
import eu.europeana.api.record.io.xml.EdmXmlDefinitions;
import eu.europeana.jena.edm.CC;
import eu.europeana.jena.edm.DQV;
import eu.europeana.jena.edm.EBUCORE;
import eu.europeana.jena.edm.EDM;
import eu.europeana.jena.edm.OA;
import eu.europeana.jena.edm.ODRL;
import eu.europeana.jena.edm.ORE;
import eu.europeana.jena.edm.SVCS;
import eu.europeana.jena.edm.WGS84;

/**
 * @author Hugo
 * @since 15 Nov 2023
 */
public class EdmJsonV2Definitions {

    public static final String object            = "object";
    public static final String def               = "def";
    public static final String about             = "about";
    public static final String type              = "type";
    public static final String success           = "success";
    public static final String statsDuration     = "statsDuration";
    public static final String requestNumber     = "requestNumber";
    public static final String timestamp_update  = "timestamp_update";
    public static final String timestamp_created = "timestamp_created";
    public static final String eCollectionName   = "europeanaCollectionName";
    public static final String edmDatasetName    = "edmDatasetName";
    public static final String eCompleteness     = "europeanaCompleteness";
    
    public static final String DATA_ITEM            = "http://data.europeana.eu/item/";
    public static final String DATA_PROVIDER_AGGR   = "http://data.europeana.eu/aggregation/provider/";
    public static final String DATA_PROVIDER_PROXY  = "http://data.europeana.eu/proxy/provider/";
    public static final String DATA_EUROPEANA_AGGR  = "http://data.europeana.eu/aggregation/europeana/";
    public static final String DATA_EUROPEANA_PROXY = "http://data.europeana.eu/proxy/europeana/";

    public static final Map<String,String> uriPrefixes = new HashMap();

    static {
        uriPrefixes.put(DATA_ITEM           , "/item/");
        uriPrefixes.put(DATA_PROVIDER_AGGR  , "/aggregation/provider/");
        uriPrefixes.put(DATA_EUROPEANA_AGGR , "/aggregation/europeana/");
        uriPrefixes.put(DATA_PROVIDER_PROXY , "/proxy/provider/");
        uriPrefixes.put(DATA_EUROPEANA_PROXY, "/proxy/europeana/");
    }

    public static final Map<Resource,ClassDefinition> classes = new HashMap();

    static {
        define(EDM.ProvidedCHO         , "providedCHOs");
        define(ORE.Proxy               , "proxies");
        define(EDM.WebResource         , "webResources");
        define(SVCS.Service            , "services");
        define(ORE.Aggregation         , "aggregations");
        define(EDM.EuropeanaAggregation, "europeanaAggregation");
        define(DQV.QualityAnnotation   , "qualityAnnotations");
        define(EDM.Agent               , "agents");
        define(EDM.Place               , "places");
        define(EDM.TimeSpan            , "timespans");
        define(SKOS.Concept            , "concepts");
        define(FOAF.Organization       , "organizations");
        define(CC.License              , "licenses");

        remove(EDM.datasetName, EDM.completeness);

        changeType(FieldType.single
                   , ORE.proxyFor, EDM.type, EDM.europeanaProxy
                   , EDM.ugc, EDM.aggregatedCHO, EDM.preview, EDM.isShownAt
                   , EDM.isShownBy, EDM.object, EDM.landingPage, RDF.type
                   , EDM.codecName, EBUCORE.hasMimeType, EBUCORE.fileByteSize
                   , EBUCORE.duration, EBUCORE.width, EBUCORE.height
                   , EDM.spatialResolution, EBUCORE.sampleSize, EBUCORE.sampleRate
                   , EBUCORE.bitRate, EBUCORE.frameRate, EDM.hasColorSpace
                   , EBUCORE.orientation, EBUCORE.audioChannelNumber
                   , OA.hasBody, WGS84.latitude, WGS84.longitude, WGS84.altitude
                   , ODRL.inheritFrom, CC.deprecatedOn);
        
        changeType(FieldType.array
                , ORE.proxyIn, EDM.hasView, DQV.hasQualityAnnotation
                , EDM.componentColor, SKOS.broader, SKOS.narrower, SKOS.related
                , SKOS.exactMatch, SKOS.closeMatch
                , SKOS.broadMatch, SKOS.narrowMatch, SKOS.relatedMatch
                , OWL.sameAs, OA.hasTarget);

        changeName(EDM.hasView   , "hasView");

        changeName(EDM.begin              , "begin");
        changeName(EDM.end                , "end");
        changeName(DCTerms.tableOfContents, "dctermsTOC");
        changeName(EDM.year               , "year");
        changeName(ORE.proxyIn            , "proxyIn");
        changeName(ORE.proxyFor           , "proxyFor");
        changeName(EDM.europeanaProxy     , "europeanaProxy");
        changeName(OA.hasTarget           , "target");
        changeName(OA.hasBody             , "body");
        changeName(EDM.aggregatedCHO      , "aggregatedCHO");

        changeName(SKOS.prefLabel   , "prefLabel");
        changeName(SKOS.altLabel    , "altLabel");
        changeName(SKOS.hiddenLabel , "hiddenLabel");
        changeName(SKOS.note        , "note");
        changeName(SKOS.broader     , "broader");
        changeName(SKOS.narrower    , "narrower");
        changeName(SKOS.related     , "related");
        changeName(SKOS.exactMatch  , "exactMatch");
        changeName(SKOS.closeMatch  , "closeMatch");
        changeName(SKOS.broadMatch  , "broadMatch");
        changeName(SKOS.narrowMatch , "narrowMatch");
        changeName(SKOS.relatedMatch, "relatedMatch");
        changeName(SKOS.inScheme    , "inScheme");

        
        FormatFunction toNumber = (l) -> Double.parseDouble(l.getLexicalForm());

        getDefinition(ORE.Proxy)
            .changeFormat((l) -> Boolean.parseBoolean(l.getLexicalForm()), EDM.europeanaProxy);

        getDefinition(EDM.WebResource)
            .changeFormat((l) -> "#" + l.getLexicalForm(), EDM.componentColor);

        getDefinition(DQV.QualityAnnotation)
            .changeName(DCTerms.created, "created")
            .changeType(FieldType.single, DCTerms.created);

        getDefinition(SKOS.Concept)
            .changeName(SKOS.notation, "notation");

        getDefinition(EDM.TimeSpan)
            .changeName(DCTerms.isPartOf, "isPartOf");
        
        getDefinition(EDM.Agent)
            .changeName(DCTerms.isPartOf, "isPartOf");

        getDefinition(EDM.Place)
            .changeName(DCTerms.isPartOf, "isPartOf")
            .changeName(WGS84.altitude , "altitude")
            .changeName(WGS84.longitude, "longitude")
            .changeName(WGS84.latitude , "latitude")
            .changeFormat(toNumber, WGS84.altitude, WGS84.longitude, WGS84.latitude);
        

        getDefinition(CC.License)
            .changeFormat((l) -> LocalDate.parse(l.getLexicalForm()).atStartOfDay()
                                          .toInstant(ZoneOffset.UTC).toEpochMilli()
                        , CC.deprecatedOn);

    }

    public static ClassDefinition getDefinition(Resource type) {
        return classes.get(type);
    }

    public static enum FieldType {
        single, array, map_array
    }

    public static interface FormatFunction {
        Object format(Literal l);
    }

    public static class ClassDefinition extends LinkedHashMap<Property,FieldDefinition> {

        public final Resource clazz;
        public final String   name;

        public ClassDefinition(Resource clazz, String name) {
            this.name  = name;
            this.clazz = clazz;
        }

        protected ClassDefinition remove(Property... props) {
            for ( Property prop : props ) { remove(prop); }
            return this;
        }

        protected ClassDefinition define(Property prop
                                       , FieldType type, FormatFunction dt) {
            FieldDefinition field = get(prop);
            if ( field != null ) { return this; }

            NamespaceDeclaration decl = Namespaces.getNamespaceResolver()
                    .getDeclarationByNamespace(prop.getNameSpace());
            String name = (decl.prefix == null ? "" : decl.prefix)
                        + StringUtils.capitalize(prop.getLocalName());
            put(prop, new FieldDefinition(name, type, dt));
            return this;
        }

        protected ClassDefinition changeName(Property prop, String name) {
            FieldDefinition def = get(prop);
            if ( def != null ) { def.name = name; }
            return this;
        }

        protected ClassDefinition changeType(FieldType type, Property... props) {
            for ( Property prop : props ) {
                FieldDefinition fdef = get(prop);
                if ( fdef != null ) { fdef.type = type; }
            }
            return this;
        }

        protected ClassDefinition changeFormat(FormatFunction formatter
                                             , Property... props) {
            for ( Property prop : props ) {
                FieldDefinition def = get(prop);
                if ( def != null ) { def.formatter = formatter; }
            }
            return this;
        }
    }

    public static class FieldDefinition {
        public String         name;
        public FieldType      type;
        public FormatFunction formatter;

        public FieldDefinition(String name, FieldType type, FormatFunction formatter) {
            this.name      = name;
            this.type      = type;
            this.formatter = formatter;
        }

        public Object toValue(Literal value) {
            return (formatter == null ? value.getValue() : formatter.format(value));
        }
    }

    private static void define(Resource type, String name) {
        ClassDefinition def = new ClassDefinition(type, name);
        for ( Property prop : EdmXmlDefinitions.propOrder.get(type) ) {
            def.define(prop, FieldType.map_array, null);
        }
        classes.put(def.clazz, def);
    }

    private static void remove(Property... props) {
        for ( ClassDefinition def : classes.values() ) {
            def.remove(props);
        }
    }

    private static void changeType(FieldType type, Property... props) {
        for ( ClassDefinition def : classes.values() ) {
            def.changeType(type, props);
        }
    }

    private static void changeName(Property prop, String name) {
        for ( ClassDefinition def : classes.values() ) {
            def.changeName(prop, name);
        }
    }

    private static void changeFormat(FormatFunction formatter
                                   , Property... props) {
        for ( ClassDefinition def : classes.values() ) {
            def.changeFormat(formatter, props);
        }
    }
}
