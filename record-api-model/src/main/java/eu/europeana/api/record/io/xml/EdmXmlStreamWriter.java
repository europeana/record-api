/**
 * 
 */
package eu.europeana.api.record.io.xml;

import eu.europeana.api.record.model.media.WebResource;
import eu.europeana.jena.edm.*;
import eu.europeana.jena.edm.OA;
import eu.europeana.jena.utils.JenaUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.rdf.model.*;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.*;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.*;
import java.util.*;

/**
 * @author Hugo
 * @since 7 Nov 2023
 */
public class EdmXmlStreamWriter {
    private static final QName rdfRdf 
        = new QName(RDF.uri, eu.europeana.api.edm.RDF.PREFIX, eu.europeana.api.edm.RDF.RDF);

    private static final QName rdfAbout 
        = new QName(RDF.uri, eu.europeana.api.edm.RDF.PREFIX, eu.europeana.api.edm.RDF.about);

    private static final QName rdfResource 
        = new QName(RDF.uri, eu.europeana.api.edm.RDF.PREFIX, eu.europeana.api.edm.RDF.resource);

    private static final QName rdfDatatype 
        = new QName(RDF.uri, eu.europeana.api.edm.RDF.PREFIX, eu.europeana.api.edm.RDF.datatype);

    private static final QName xmlLang 
        = new QName(XMLConstants.XML_NS_URI, XMLConstants.XML_NS_PREFIX, "lang");

    private static final List<Resource> classOrder = Arrays.asList(
        EDM.ProvidedCHO, EDM.WebResource, EDM.Agent, EDM.Place, EDM.TimeSpan
      , SKOS.Concept, ORE.Aggregation, ORE.Proxy, EDM.EuropeanaAggregation
      , CC.License, FOAF.Organization, DCAT.Dataset, SVCS.Service);

    private static final List<Property> PropOrderProvidedCHO = Arrays.asList( 
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
    
    private static final List<Property> PropOrderProxy = Arrays.asList(
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

    private static final List<Property> PropOrderWebResource = Arrays.asList(
        DC.creator, DC.description, DC.format, DC.rights, DC.source, DC.type
      , DCTerms.conformsTo, DCTerms.created, DCTerms.extent, DCTerms.hasPart
      , DCTerms.isFormatOf, DCTerms.isPartOf, DCTerms.issued, EDM.isNextInSequence
      , EDM.rights, OWL.sameAs, RDF.type, EDM.codecName, EBUCORE.hasMimeType
      , EBUCORE.fileByteSize, EBUCORE.duration, EBUCORE.width, EBUCORE.height
      , EDM.spatialResolution, EBUCORE.sampleSize, EBUCORE.sampleRate
      , EBUCORE.bitRate, EBUCORE.frameRate, EDM.hasColorSpace
      , EDM.componentColor, EBUCORE.orientation, EBUCORE.audioChannelNumber
      , DCTerms.isReferencedBy, EDM.preview, SVCS.has_service);

    private static final List<Property> PropOrderService = Arrays.asList(
        DCTerms.conformsTo, DOAP.impls);

    private static final List<Property> PropOrderAggregation = Arrays.asList(
        EDM.aggregatedCHO, EDM.dataProvider, EDM.hasView, EDM.isShownAt
      , EDM.isShownBy, EDM.object, EDM.provider, DC.rights, EDM.rights, EDM.ugc
      , EDM.intermediateProvider);

    private static final List<Property> PropOrderEuropeanaAggregation = Arrays.asList(
        DC.creator, EDM.aggregatedCHO, EDM.collectionName, EDM.datasetName
      , EDM.country, EDM.hasView, EDM.isShownBy, EDM.preview, EDM.landingPage
      , EDM.language, EDM.rights, ORE.aggregates);

    private static final List<Property> PropOrderQuality = Arrays.asList(
        DCTerms.created, OA.hasTarget, OA.hasBody);

    private static final List<Property> PropOrderAgent = Arrays.asList(
        SKOS.prefLabel, SKOS.altLabel, SKOS.note, DC.date, DC.identifier
      , DCTerms.hasPart, DCTerms.isPartOf, EDM.begin, EDM.end, EDM.hasMet
      , EDM.isRelatedTo, FOAF.name, RDAGR2.biographicalInformation
      , RDAGR2.dateOfBirth, RDAGR2.dateOfDeath, RDAGR2.dateOfEstablishment
      , RDAGR2.dateOfTermination, RDAGR2.gender, RDAGR2.placeOfBirth
      , RDAGR2.placeOfDeath, RDAGR2.professionOrOccupation, OWL.sameAs);

    private static final List<Property> PropOrderPlace = Arrays.asList(
        WGS84.latitude, WGS84.longitude, WGS84.altitude, SKOS.prefLabel
      , SKOS.altLabel, SKOS.note, DCTerms.hasPart, DCTerms.isPartOf
      , EDM.isNextInSequence, OWL.sameAs);

    private static final List<Property> PropOrderTimeSpan = Arrays.asList(
        SKOS.prefLabel, SKOS.altLabel, SKOS.note, DCTerms.hasPart
      , DCTerms.isPartOf, EDM.begin, EDM.end, EDM.isNextInSequence, OWL.sameAs);
            
    private static final List<Property> PropOrderConcept = Arrays.asList(
        SKOS.prefLabel, SKOS.altLabel, SKOS.broader, SKOS.narrower, SKOS.related
      , SKOS.broadMatch, SKOS.narrowMatch, SKOS.relatedMatch, SKOS.exactMatch
      , SKOS.closeMatch, SKOS.note, SKOS.notation, SKOS.inScheme);

    private static final List<Property> PropOrderOrganization = Arrays.asList(
        SKOS.prefLabel, SKOS.altLabel);

    private static final List<Property> PropOrderLicense = Arrays.asList(
        ODRL.inheritFrom, CC.deprecatedOn);

    private static final Map<Resource,List<Property>> propOrder = new HashMap();

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

    public void write(Model model, XMLStreamWriter xml) throws XMLStreamException {
        new SaxWriter(xml, model).writeDocument(model);
    }

    public void write(Model model, Writer writer) throws XMLStreamException {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        new SaxWriter(output.createXMLStreamWriter(writer), model).writeDocument(model);
    }

    public void write(Model model, OutputStream out) throws XMLStreamException {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        new SaxWriter(output.createXMLStreamWriter(out), model).writeDocument(model);
    }

    private class SaxWriter {

        private XMLStreamWriter xml;
        private Model           model;

        protected SaxWriter(XMLStreamWriter xml, Model model) { 
            this.xml   = xml; 
            this.model = model;
        }

        private void startElement(QName name) throws XMLStreamException {
            xml.writeStartElement(name.prefix, name.localName, name.ns);
        }

        private void endElement() throws XMLStreamException {
            xml.writeEndElement();
        }
        
        private void writeDocument(Model model) throws XMLStreamException {
            xml.writeStartDocument();

            writeResources(model);

            xml.writeEndDocument();
            xml.flush();
        }

        private void writeResources(Model model) throws XMLStreamException {
            startElement(rdfRdf);

            // add all prefixes
            for ( Map.Entry<String, String> entry : model.getNsPrefixMap()
                                                         .entrySet() ) {
                xml.writeNamespace(entry.getKey(), entry.getValue());
            }

            for ( Resource type : classOrder ) {
                List<Property> propOrder = EdmXmlStreamWriter.this.propOrder.get(type);

                ResIterator iter = model.listResourcesWithProperty(RDF.type, type);
                while ( iter.hasNext() ) {
                    Resource r = iter.next();
                    startElement(getQName(type));
                    writeAttribute(rdfAbout, r.getURI());
                    writeProperties(getProperties(r, propOrder), type);
                    endElement();
                }
            }
            endElement();
        }

        private void writeProperties(Collection<Statement> props, Resource type) 
                throws XMLStreamException {
            for ( Statement stmt : props ) {
                if ( stmt.getPredicate().equals(RDF.type) 
                  && stmt.getObject().equals(type) ) { continue; }
                writeStatement(stmt);
            }
        }

        private void writeStatement(Statement stmt) 
                throws XMLStreamException {
            RDFNode node = stmt.getObject();
            if ( node.isResource() ) {
                Resource r = node.asResource();
                startElement(getQName(stmt.getPredicate()));
                writeAttribute(rdfResource, r.getURI());
                endElement();
                return;
            }

            Literal literal = node.asLiteral();
            startElement(getQName(stmt.getPredicate()));
            writeLanguage(literal.getLanguage());
            writeDatatype(literal.getDatatype());
            xml.writeCharacters(literal.getLexicalForm());
            endElement();
        }

        private void writeLanguage(String language) throws XMLStreamException {
            if ( StringUtils.isBlank(language) ) { return; }
            writeAttribute(xmlLang, language);
        }

        private void writeDatatype(RDFDatatype datatype) throws XMLStreamException {
            if ( !JenaUtils.hasDatatype(datatype) ) { return; }
            writeAttribute(rdfDatatype, datatype.getURI());
        }

        private void writeAttribute(QName qname, String value) 
                throws XMLStreamException {
            xml.writeAttribute(qname.prefix, qname.ns, qname.localName, value);
        }

        private Collection<Statement> getProperties(Resource r, List<Property> propOrder) {
            return JenaUtils.copy(r.listProperties()
                                , new TreeSet(new StatementComparator(propOrder)));
        }

        private QName getQName(Resource r) {
            String ns        = r.getNameSpace();
            String localName = r.getLocalName();
            String prefix    = model.getNsURIPrefix(ns);
            return new QName(ns, prefix, localName);
        }

        private QName getQName(Property prop) {
            String ns        = prop.getNameSpace();
            String localName = prop.getLocalName();
            String prefix    = model.getNsURIPrefix(ns);
            return new QName(ns, prefix, localName);
        }
    }

    protected static class QName {
        protected String ns;
        protected String prefix;
        protected String localName;

        public QName(String ns, String prefix, String localName) {
            this.ns = ns;
            this.prefix = prefix;
            this.localName = localName;
        }
    }

    private static class StatementComparator implements Comparator<Statement> {
       
        private List<Property> propertyOrder; 

        public StatementComparator(List<Property> propertyOrder) {
            this.propertyOrder = propertyOrder;
        }

        @Override
        public int compare(Statement stmt1, Statement stmt2)
        {
            if ( stmt1.equals(stmt2) ) { return 0; }
            int order1 = getOrder(stmt1.getPredicate());
            int order2 = getOrder(stmt2.getPredicate());
            int diff = ( order1 - order2 );
            return ( diff != 0 ? diff : stmt1.hashCode() - stmt2.hashCode() );
        }

        private int getOrder(Property p) { return propertyOrder.indexOf(p); }
    }

    public static final void main(String[] args) throws Throwable {
        File file   = new File("C:\\Work\\incoming\\Record v3\\source\\https___1914_1918_europeana_eu_contributions_10263.xml");
        File target = new File("C:\\Work\\incoming\\Record v3\\target\\https___1914_1918_europeana_eu_contributions_10263.xml");
        Model m = ModelFactory.createDefaultModel();
        m.read(new FileReader(file), "", "RDF/XML");

        FileWriter fileWriter = new FileWriter(target);
        new EdmXmlStreamWriter().write(m, fileWriter);
        fileWriter.close();
    }
}
