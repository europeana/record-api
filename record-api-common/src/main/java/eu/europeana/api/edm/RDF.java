package eu.europeana.api.edm;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 14 Apr 2016
 */
public interface RDF
{
    public static final String PREFIX = "rdf";
    public static final String NS     = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    public static final NamespaceDeclaration DECL = new NamespaceDeclaration(PREFIX, NS);

    public static final String RDF      = "RDF";
    public static final String type     = "type";
    public static final String datatype = "datatype";
    public static final String about    = "about";
    public static final String resource = "resource";
}
