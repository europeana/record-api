/**
 * 
 */
package eu.europeana.api.edm;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 15 Apr 2016
 */
public interface DOAP
{
    public static final String PREFIX = "doap";
    public static final String NS     = "http://usefulinc.com/ns/doap#";
    public static final NamespaceDeclaration DECL = new NamespaceDeclaration(PREFIX, NS);

    public static final String impls  = "implements";
}
