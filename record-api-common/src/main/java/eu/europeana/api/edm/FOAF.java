package eu.europeana.api.edm;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 14 Apr 2016
 */
public interface FOAF
{
    public static final String PREFIX = "foaf";
    public static final String NS     = "http://xmlns.com/foaf/0.1/";
    public static final NamespaceDeclaration DECL = new NamespaceDeclaration(PREFIX, NS);

    public static final String Organization = "Organization";
    public static final String depiction    = "depiction";
    public static final String name         = "name";
}
