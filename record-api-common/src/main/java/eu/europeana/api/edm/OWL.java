package eu.europeana.api.edm;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 14 Apr 2016
 */
public interface OWL
{
    public static final String PREFIX = "owl";
    public static final String NS     = "http://www.w3.org/2002/07/owl#";
    public static final NamespaceDeclaration DECL = new NamespaceDeclaration(PREFIX, NS);

    public static final String sameAs = "sameAs";
}
