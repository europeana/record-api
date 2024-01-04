package eu.europeana.api.edm;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 14 Apr 2016
 */
public interface ODRL
{
    public static final String PREFIX = "odrl";
    public static final String NS     = "http://www.w3.org/ns/odrl/2/";
    public static final NamespaceDeclaration DECL = new NamespaceDeclaration(PREFIX, NS);

    public static final String inheritFrom = "inheritFrom";
}
