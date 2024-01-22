/**
 * 
 */
package eu.europeana.api.record.io.xml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.vocabulary.RDF;


import eu.europeana.jena.encoder.utils.JenaUtils;

/**
 * @author Hugo
 * @since 7 Nov 2023
 */
public class EdmXmlStreamWriter extends EdmXmlDefinitions
{
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

    /*
    ///////////////////////////////////////////////////////////////////////
    // Order definitions
    ///////////////////////////////////////////////////////////////////////
     */


    /*
    ///////////////////////////////////////////////////////////////////////
    // Public methods
    ///////////////////////////////////////////////////////////////////////
     */

    public void write(Model model, XMLStreamWriter xml) throws XMLStreamException {
        new StreamWriter(xml, model).writeDocument();
    }

    public void write(Model model, Writer writer) throws XMLStreamException {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        new StreamWriter(output.createXMLStreamWriter(writer), model).writeDocument();
    }

    public void write(Model model, OutputStream out) throws XMLStreamException {
        XMLOutputFactory output = XMLOutputFactory.newInstance();
        new StreamWriter(output.createXMLStreamWriter(out), model).writeDocument();
    }

    /*
    ///////////////////////////////////////////////////////////////////////
    // Internal methods
    ///////////////////////////////////////////////////////////////////////
     */

    private class StreamWriter {

        private XMLStreamWriter xml;
        private Model           model;

        protected StreamWriter(XMLStreamWriter xml, Model model) { 
            this.xml   = xml; 
            this.model = model;
        }

        private void startElement(QName name) throws XMLStreamException {
            xml.writeStartElement(name.prefix, name.localName, name.ns);
        }

        private void endElement() throws XMLStreamException {
            xml.writeEndElement();
        }
        
        private void writeDocument() throws XMLStreamException {
            xml.writeStartDocument();
//            xml.writeDTD("<!DOCTYPE RDF [ <!ENTITY xsd \"" + XSD.NS + "\" > ]>\n");

            writeResources();

            xml.writeEndDocument();
            xml.flush();
        }

        private void writeResources() throws XMLStreamException {
            startElement(rdfRdf);

            // add all prefixes
            for ( Map.Entry<String, String> entry : model.getNsPrefixMap()
                                                         .entrySet() ) {
                xml.writeNamespace(entry.getKey(), entry.getValue());
            }

            Resource       prevType  = null;
            List<Property> propOrder = null;
            for ( Statement stmt : getResources(model) ) {
                Resource r    = stmt.getSubject();
                Resource type = stmt.getResource();
                if ( !type.equals(prevType) ) {
                    propOrder = EdmXmlStreamWriter.this.propOrder.get(type);
                    prevType = type;
                }
                if ( propOrder == null ) { continue; }

                startElement(getQName(type));
                writeAttribute(rdfAbout, r.getURI());
                writeProperties(getProperties(r, propOrder), type);
                endElement();
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

            String uri = datatype.getURI();
//            if ( uri.startsWith(XSD.NS) ) { uri = uri.replace(XSD.NS, "&xsd;"); }
            writeAttribute(rdfDatatype, uri);
        }

        private void writeAttribute(QName qname, String value) 
                throws XMLStreamException {
            xml.writeAttribute(qname.prefix, qname.ns, qname.localName, value);
        }

        private Collection<Statement> getResources(Model model) {
            return JenaUtils.copy(model.listStatements(null, RDF.type, (Resource)null)
                                , new TreeSet(StatementResourceComparator.INSTANCE));
        }

        private Collection<Statement> getProperties(Resource r, List<Property> propOrder) {
            return JenaUtils.copy(r.listProperties()
                                , new TreeSet(new StatementPropertyComparator(propOrder)));
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

    private static class StatementResourceComparator implements Comparator<Statement> {

        private static StatementResourceComparator INSTANCE 
            = new StatementResourceComparator(EdmXmlStreamWriter.classOrder);

        private List<Resource> classOrder;

        public StatementResourceComparator(List<Resource> classOrder) {
            this.classOrder = classOrder;
        }

        @Override
        public int compare(Statement stmt1, Statement stmt2)
        {
            if ( stmt1.equals(stmt2) ) { return 0; }

            int order1 = getOrder(stmt1.getObject());
            int order2 = getOrder(stmt2.getObject());
            int diff = ( order1 - order2 );
            return ( diff == 0 ? stmt1.getSubject().getURI()
                                      .compareTo(stmt2.getSubject().getURI())
                               : diff );
        }

        private int getOrder(RDFNode node) { return classOrder.indexOf(node); }
    }


    private static class StatementPropertyComparator implements Comparator<Statement> {
       
        private List<Property> propertyOrder;

        public StatementPropertyComparator(List<Property> propertyOrder) {
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
        File file   = new File("C:\\Work\\incoming\\Record v3\\source\\bib_BVPB20160008661.xml");
        File target = new File("C:\\Work\\incoming\\Record v3\\target\\bib_BVPB20160008661.xml");
        Model m = ModelFactory.createDefaultModel();
        m.read(new FileReader(file), "", "RDF/XML");

        FileWriter fileWriter = new FileWriter(target);
        new EdmXmlStreamWriter().write(m, fileWriter);
        fileWriter.close();
    }
}
