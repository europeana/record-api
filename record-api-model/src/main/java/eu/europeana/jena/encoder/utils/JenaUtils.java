/**
 * 
 */
package eu.europeana.jena.encoder.utils;

import eu.europeana.api.edm.NamespaceDeclaration;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.datatypes.RDFDatatype;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.lang.ReaderRIOTRDFXML;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.XSD;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * @author Hugo
 * @since 19 Oct 2023
 */
public class JenaUtils {

    public static final String allowBadURIs       = "allowBadURIs";
    public static final String errorForSpaceInURI = "errorForSpaceInURI";

    private static HashSet<String> ignoredDatatypes = new HashSet<String>();

    static {
        ignoredDatatypes.add(RDF.langString.getURI());
        ignoredDatatypes.add(XSD.xstring.getURI());
    }

    public static void setNamespace(Model m, NamespaceDeclaration decl) {
        m.setNsPrefix(decl.prefix, decl.ns);
    }

    /*
     * This is the only to disable the space validation in Jena Riot parser
     */
    public static void disableRiotValidation() {
        try {
            Field f = ReaderRIOTRDFXML.class.getDeclaredField(errorForSpaceInURI);
            f.setAccessible(true);
            f.set(null, false);
        }
        catch (NoSuchFieldException | SecurityException
             | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasDatatype(RDFDatatype dt) {
        return ( dt != null && !ignoredDatatypes.contains(dt.getURI())); 
    }

    public static boolean hasLanguage(Literal literal) {
        return ( !StringUtils.isBlank(literal.getLanguage()) );
    }

    public static <T extends Collection<Statement>> T copy(StmtIterator iter
                                                         , T col) {
        try {
            while ( iter.hasNext() ) { col.add(iter.next()); }
            return col;
        }
        finally { iter.close(); }
    }

    public static <T extends Collection<Resource>> T copy(ResIterator iter
                                                        , T col) {
        try {
            while ( iter.hasNext() ) { col.add(iter.next()); }
            return col;
        }
        finally { iter.close(); }
    }

    public static List<Statement> toList(StmtIterator iter) {
        return copy(iter, new ArrayList<Statement>());
    }

    public static List<Resource> toList(ResIterator iter) {
        return copy(iter, new ArrayList<Resource>());
    }
}
