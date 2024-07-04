/**
 * 
 */
package eu.europeana.api.record.migration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.RDF;

/**
 * @author Hugo
 * @since 18 Jun 2024
 */
public class JenaUtils extends eu.europeana.jena.encoder.utils.JenaUtils {

    public static boolean hasType(Resource r, Resource... types) {
        return hasType(r, Arrays.asList(types));
    }

    public static boolean hasType(Resource r, Collection<Resource> types) {
        StmtIterator iter = r.listProperties(RDF.type);
        while ( iter.hasNext() ) {
            if ( types.contains(iter.next().getObject()) ) { return true; }
        }
        return false;
    }


    public static Collection<Literal> getLanguageTaggedLiterals(StmtIterator iter) {
        return getLanguageTaggedLiterals(new ArrayList<>(), iter);
    }

    public static Collection<Literal> getLanguageTaggedLiterals(
            Collection<Literal> ret, StmtIterator iter) {
        try {
            while ( iter.hasNext() ) {
                RDFNode node = iter.next().getObject();
                if ( !node.isLiteral() ) { continue; }
                
                Literal l = node.asLiteral();
                if ( hasLanguage(l) ) { ret.add(l); }
            }
            return ret;
        }
        finally { iter.close(); }
    }

    
    public static boolean exists(StmtIterator iter) {
        try {
            return ( iter.hasNext() );
        }
        finally { iter.close(); }
    }

    public static List<Resource> getObjects(StmtIterator iter) {
        return getObjects(iter, new ArrayList<>());
    }

    public static List<Resource> getObjects(StmtIterator iter
                                          , List<Resource> list) {
        try {
            while ( iter.hasNext() ) { 
                RDFNode node = iter.next().getObject();
                if ( node.isResource() ) { list.add(node.asResource()); }
            }
            return list;
        }
        finally { iter.close(); }
    }

    public static List<Statement> asList(StmtIterator iter) {
        return asList(iter, new ArrayList<>());
    }

    public static List<Statement> asList(StmtIterator iter
                                          , List<Statement> list) {
        try {
            while ( iter.hasNext() ) { list.add(iter.next()); }
            return list;
        }
        finally { iter.close(); }
    }

    public static List<Resource> asList(ResIterator iter) {
        return asList(iter, new ArrayList<>());
    }

    public static List<Resource> asList(ResIterator iter
                                         , List<Resource> list) {
        List<Resource> ret = new ArrayList<>();
        try {
            while ( iter.hasNext() ) { ret.add(iter.next()); }
            return ret;
        }
        finally { iter.close(); }
    }

}
