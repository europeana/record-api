/**
 * 
 */
package eu.europeana.api.edm;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 15 Apr 2016
 */
public interface SVCS
{
    public static final String PREFIX = "svcs";
    public static final String NS     = "http://rdfs.org/sioc/services#";
    public static final NamespaceDeclaration DECL = new NamespaceDeclaration(PREFIX, NS);

    public static final String Service     = "Service";

    public static final String has_service = "has_service";
}
