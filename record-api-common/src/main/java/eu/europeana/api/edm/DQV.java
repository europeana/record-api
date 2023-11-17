/**
 * 
 */
package eu.europeana.api.edm;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 15 Apr 2016
 */
public interface DQV
{
    public static final String PREFIX = "dqv";
    public static final String NS     = "http://www.w3.org/ns/dqv#";
    public static final NamespaceDeclaration DECL = new NamespaceDeclaration(PREFIX, NS);

    public static final String QualityAnnotation    = "QualityAnnotation";

    public static final String hasQualityAnnotation = "hasQualityAnnotation";
    public static final String inDimension          = "inDimension";
}
