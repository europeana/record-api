/**
 * 
 */
package eu.europeana.api.record.migration;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;

import eu.europeana.jena.edm.EDM;
import eu.europeana.jena.edm.ORE;

import static eu.europeana.api.record.migration.JenaUtils.*;

/**
 * @author Hugo
 * @since 18 Jun 2024
 */
public class EdmExternalGenerator
{
    private static List<Property> SINGLE_VALUE
        = Arrays.asList(EDM.currentLocation, EDM.isRepresentationOf);

    private static List<Property> DISMISS 
        = Arrays.asList(ORE.proxyFor, ORE.lineage, ORE.proxyIn, RDF.type);

    public Resource generateExternal(Resource cho) {
        Model m = cho.getModel();

        Collection<Property> props   = new HashSet();

        List<Resource> proxies = asList(m.listResourcesWithProperty(ORE.proxyFor, cho));
        for ( Resource proxy : proxies ) {
            StmtIterator iter = proxy.listProperties();
            while ( iter.hasNext() ) {
                Statement stmt = iter.next();
                Property  prop = stmt.getPredicate();
                if ( DISMISS.contains(prop) ) { continue; }

                props.add(prop);
                cho.addProperty(prop, stmt.getObject());
            }
        }

        Collection<Literal> values = new HashSet();
        for ( Property p : props ) {
            List<Statement> stmts = asList(cho.listProperties(p));
            values = getValues(cho, stmts, values);

            for ( Statement stmt : stmts ) {
                RDFNode node = stmt.getObject();
                if ( !isDuplicate(node, values, stmts) ) { continue; }
                m.remove(stmt);
            }

            values.clear();
        }

        //take care of single valued properties
        //Example: /293/item_2D7OVZT2VJRFZH5CXHZMHWCAXZESTNU5
        for ( Property p : SINGLE_VALUE ) {
            StmtIterator iter = cho.listProperties(p);
            if ( !iter.hasNext() ) { continue; }

            List<Statement> stmts = asList(iter);
            for ( int i = stmts.size() - 1; i > 0; i--) {
                m.remove(stmts.get(1));
            }
        }

        // add aggregation
        String uri     = cho.getURI();
        String aggrURI = uri.replace("http://data.europeana.eu/item/"
                                   , "http://data.europeana.eu/aggregation/");
        Resource aggr = m.getResource(aggrURI);
        aggr.addProperty(RDF.type, ORE.Aggregation);
        
        
        String  dt  = RecordJenaProcessor.DATETIME_FORMAT.format(OffsetDateTime.now());
        Literal now = m.createLiteral(dt/*, XSD.dateTime.getURI()*/);
        aggr.addProperty(DCTerms.created, now);
        aggr.addProperty(DCTerms.modified, now);
        cho.addProperty(ORE.isAggregatedBy, aggr);

        return cho;
    }

    private Collection<Literal> getValues(Resource cho
                                        , Collection<Statement> stmts
                                        , Collection<Literal> values) {
        for ( Statement stmt : stmts ) {
            RDFNode node = stmt.getObject();
            if ( !node.isResource() ) { continue; }
            
            Resource r = node.asResource();
            getLanguageTaggedLiterals(values, r.listProperties(SKOS.prefLabel));
            getLanguageTaggedLiterals(values, r.listProperties(SKOS.altLabel));
            getLanguageTaggedLiterals(values, r.listProperties(SKOS.hiddenLabel));
        }
        return values;
    }


    private boolean isDuplicate(RDFNode node, Collection<Literal> values
                              , List<Statement> stmts) {
        if ( node.isLiteral() && isDuplicateLiteral(node.asLiteral(), values) ) { 
            return true;
        }

        if ( node.isURIResource() && isDuplicate(node.asResource(), stmts) ) {
            return true;
        }
        return false;
    }

    private boolean isDuplicateLiteral(Literal l, Collection<Literal> values) {
        if ( hasDatatype(l) ) { return false; }

        return ( hasLanguage(l) ? isDuplicate(l, values)
                                : isDupLangIgnore(l, values) );
    }

    private boolean isDupLangIgnore(Literal l1, Collection<Literal> list) {
        for ( Literal l2 : list ) {
            if ( isDupLangIgnore(l1, l2) ) { return true; }
        }
        return false;
    }

    private boolean isDupLangIgnore(Literal l1, Literal l2) {
        return l1.getString().equalsIgnoreCase(l2.getString());
    }

    //Language aware duplicate check
    private boolean isDuplicate(Literal l1, Collection<Literal> list) {
        for ( Literal l2 : list ) {
            if ( isDuplicate(l1, l2) ) { return true; }
        }
        return false;
    }

    //Language aware duplicate check
    private boolean isDuplicate(Literal l1, Literal l2) {
        if ( l1.getLanguage().equals(l2.getLanguage()) ) {
            return l1.getString().equalsIgnoreCase(l2.getString());
        }
        return false;
    }


    private boolean isDuplicate(Resource r, List<Statement> stmts) {
        if ( r.getURI().startsWith("http://data.europeana.eu/") ) { return false; }

        for ( Statement stmt : stmts ) {
            RDFNode node = stmt.getObject();
            if ( r == null || !node.isURIResource() ) { continue; }

            Resource r2 = node.asResource();
            if ( r2.hasProperty(OWL.sameAs, r) 
              || r2.hasProperty(org.apache.jena.vocabulary.SKOS.exactMatch, r) ) { return true; }
        }
        return false;
    }

    public boolean isSingleValue(Property prop) {
        return SINGLE_VALUE.contains(prop);
    }
}
