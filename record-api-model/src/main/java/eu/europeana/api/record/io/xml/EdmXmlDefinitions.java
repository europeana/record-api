/**
 * 
 */
package eu.europeana.api.record.io.xml;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;

import eu.europeana.jena.edm.CC;
import eu.europeana.jena.edm.DOAP;
import eu.europeana.jena.edm.DQV;
import eu.europeana.jena.edm.EBUCORE;
import eu.europeana.jena.edm.EDM;
import eu.europeana.jena.edm.OA;
import eu.europeana.jena.edm.ODRL;
import eu.europeana.jena.edm.ORE;
import eu.europeana.jena.edm.RDAGR2;
import eu.europeana.jena.edm.SVCS;
import eu.europeana.jena.edm.WGS84;

/**
 * @author Hugo
 * @since 15 Nov 2023
 */
public class EdmXmlDefinitions {

    public static final List<Resource> classOrder = Arrays.asList(
        EDM.ProvidedCHO, EDM.WebResource, EDM.Agent, EDM.Place, EDM.TimeSpan
      , SKOS.Concept, ORE.Aggregation, ORE.Proxy, EDM.EuropeanaAggregation
      , CC.License, FOAF.Organization, DCAT.Dataset, SVCS.Service, DQV.QualityAnnotation);

    public static final List<Property> PropOrderProvidedCHO = Arrays.asList( 
        DC.contributor, DC.coverage, DC.creator, DC.date, DC.description
      , DC.format, DC.identifier, DC.language, DC.publisher, DC.relation
      , DC.rights, DC.source, DC.subject, DC.title, DC.type, DCTerms.alternative
      , DCTerms.conformsTo, DCTerms.created, DCTerms.extent, DCTerms.hasFormat
      , DCTerms.hasPart, DCTerms.hasVersion, DCTerms.isFormatOf
      , DCTerms.isPartOf, DCTerms.isReferencedBy, DCTerms.isReplacedBy
      , DCTerms.isRequiredBy, DCTerms.issued, DCTerms.isVersionOf
      , DCTerms.medium, DCTerms.provenance, DCTerms.references, DCTerms.replaces
      , DCTerms.requires, DCTerms.spatial, DCTerms.tableOfContents
      , DCTerms.temporal, EDM.currentLocation, EDM.hasMet, EDM.hasType
      , EDM.incorporates, EDM.isDerivativeOf, EDM.isNextInSequence
      , EDM.isRelatedTo, EDM.isRepresentationOf, EDM.isSimilarTo
      , EDM.isSuccessorOf, EDM.realizes, EDM.type, OWL.sameAs);
    
    public static final List<Property> PropOrderProxy = Arrays.asList(
        DC.contributor, DC.coverage, DC.creator, DC.date, DC.description
      , DC.format, DC.identifier, DC.language, DC.publisher, DC.relation
      , DC.rights, DC.source, DC.subject, DC.title, DC.type, DCTerms.alternative
      , DCTerms.conformsTo, DCTerms.created, DCTerms.extent, DCTerms.hasFormat
      , DCTerms.hasPart, DCTerms.hasVersion, DCTerms.isFormatOf
      , DCTerms.isPartOf, DCTerms.isReferencedBy, DCTerms.isReplacedBy
      , DCTerms.isRequiredBy, DCTerms.issued, DCTerms.isVersionOf
      , DCTerms.medium, DCTerms.provenance, DCTerms.references, DCTerms.replaces
      , DCTerms.requires, DCTerms.spatial, DCTerms.tableOfContents
      , DCTerms.temporal, EDM.currentLocation, EDM.hasMet, EDM.hasType
      , EDM.incorporates, EDM.isDerivativeOf, EDM.isNextInSequence
      , EDM.isRelatedTo, EDM.isRepresentationOf, EDM.isSimilarTo
      , EDM.isSuccessorOf, EDM.realizes, EDM.europeanaProxy, EDM.userTag
      , EDM.year, ORE.proxyFor, ORE.proxyIn, EDM.type, OWL.sameAs);

    public static final List<Property> PropOrderWebResource = Arrays.asList(
        DC.creator, DC.description, DC.format, DC.rights, DC.source, DC.type
      , DCTerms.conformsTo, DCTerms.created, DCTerms.extent, DCTerms.hasPart
      , DCTerms.isFormatOf, DCTerms.isPartOf, DCTerms.issued, EDM.isNextInSequence
      , EDM.rights, OWL.sameAs, RDF.type, EDM.codecName, EBUCORE.hasMimeType
      , EBUCORE.fileByteSize, EBUCORE.duration, EBUCORE.width, EBUCORE.height
      , EDM.spatialResolution, EBUCORE.sampleSize, EBUCORE.sampleRate
      , EBUCORE.bitRate, EBUCORE.frameRate, EDM.hasColorSpace
      , EDM.componentColor, EBUCORE.orientation, EBUCORE.audioChannelNumber
      , DCTerms.isReferencedBy, EDM.preview, SVCS.has_service);

    public static final List<Property> PropOrderService = Arrays.asList(
        DCTerms.conformsTo, DOAP.impls);

    public static final List<Property> PropOrderAggregation = Arrays.asList(
        EDM.aggregatedCHO, EDM.dataProvider, EDM.hasView, EDM.isShownAt
      , EDM.isShownBy, EDM.object, EDM.provider, DC.rights, EDM.rights, EDM.ugc
      , EDM.intermediateProvider, DCTerms.created, DCTerms.modified
      , DQV.hasQualityAnnotation);

    public static final List<Property> PropOrderEuropeanaAggregation = Arrays.asList(
        DC.creator, EDM.aggregatedCHO, EDM.collectionName, EDM.datasetName
      , EDM.country, EDM.hasView, EDM.isShownBy, EDM.preview, EDM.landingPage
      , EDM.language, EDM.rights, ORE.aggregates, EDM.completeness
      , DCTerms.created, DCTerms.modified, DQV.hasQualityAnnotation);

    public static final List<Property> PropOrderQuality = Arrays.asList(
        DCTerms.created, OA.hasTarget, OA.hasBody);

    public static final List<Property> PropOrderAgent = Arrays.asList(
        SKOS.prefLabel, SKOS.altLabel, SKOS.note, DC.date, DC.identifier
      , DCTerms.hasPart, DCTerms.isPartOf, EDM.begin, EDM.end, EDM.hasMet
      , EDM.isRelatedTo, FOAF.name, RDAGR2.biographicalInformation
      , RDAGR2.dateOfBirth, RDAGR2.dateOfDeath, RDAGR2.dateOfEstablishment
      , RDAGR2.dateOfTermination, RDAGR2.gender, RDAGR2.placeOfBirth
      , RDAGR2.placeOfDeath, RDAGR2.professionOrOccupation, OWL.sameAs);

    public static final List<Property> PropOrderPlace = Arrays.asList(
        WGS84.latitude, WGS84.longitude, WGS84.altitude, SKOS.prefLabel
      , SKOS.altLabel, SKOS.note, DCTerms.hasPart, DCTerms.isPartOf
      , EDM.isNextInSequence, OWL.sameAs);

    public static final List<Property> PropOrderTimeSpan = Arrays.asList(
        SKOS.prefLabel, SKOS.altLabel, SKOS.note, SKOS.notation, DCTerms.hasPart
      , DCTerms.isPartOf, EDM.begin, EDM.end, EDM.isNextInSequence, OWL.sameAs);
            
    public static final List<Property> PropOrderConcept = Arrays.asList(
        SKOS.prefLabel, SKOS.altLabel, SKOS.broader, SKOS.narrower, SKOS.related
      , SKOS.broadMatch, SKOS.narrowMatch, SKOS.relatedMatch, SKOS.exactMatch
      , SKOS.closeMatch, SKOS.note, SKOS.notation, SKOS.inScheme);

    public static final List<Property> PropOrderOrganization = Arrays.asList(
        SKOS.prefLabel, SKOS.altLabel);

    public static final List<Property> PropOrderLicense = Arrays.asList(
        ODRL.inheritFrom, CC.deprecatedOn);

    public static final Map<Resource,List<Property>> propOrder = new HashMap();

    static {
        propOrder.put(EDM.ProvidedCHO         , PropOrderProvidedCHO);
        propOrder.put(ORE.Proxy               , PropOrderProxy);
        propOrder.put(EDM.WebResource         , PropOrderWebResource);
        propOrder.put(SVCS.Service            , PropOrderService);
        propOrder.put(ORE.Aggregation         , PropOrderAggregation);
        propOrder.put(EDM.EuropeanaAggregation, PropOrderEuropeanaAggregation);
        propOrder.put(DQV.QualityAnnotation   , PropOrderQuality);
        propOrder.put(EDM.Agent               , PropOrderAgent);
        propOrder.put(EDM.Place               , PropOrderPlace);
        propOrder.put(EDM.TimeSpan            , PropOrderTimeSpan);
        propOrder.put(SKOS.Concept            , PropOrderConcept);
        propOrder.put(FOAF.Organization       , PropOrderOrganization);
        propOrder.put(CC.License              , PropOrderLicense);
    }
}
