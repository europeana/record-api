package eu.europeana.api.edm;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 14 Apr 2016
 */
public interface DCTerms
{
    public static final String PREFIX = "dcterms";
    public static final String NS     = "http://purl.org/dc/terms/";
    public static final NamespaceDeclaration DECL = new NamespaceDeclaration(PREFIX, NS);


    public static final String alternative     = "alternative";
    public static final String conformsTo      = "conformsTo";
    public static final String created         = "created";
    public static final String modified        = "modified";
    public static final String extent          = "extent";
    public static final String hasFormat       = "hasFormat";
    public static final String hasPart         = "hasPart";
    public static final String hasVersion      = "hasVersion";
    public static final String isFormatOf      = "isFormatOf";
    public static final String isPartOf        = "isPartOf";
    public static final String isReferencedBy  = "isReferencedBy";
    public static final String isReplacedBy    = "isReplacedBy";
    public static final String isRequiredBy    = "isRequiredBy";
    public static final String issued          = "issued";
    public static final String isVersionOf     = "isVersionOf";
    public static final String medium          = "medium";
    public static final String provenance      = "provenance";
    public static final String references      = "references";
    public static final String replaces        = "replaces";
    public static final String requires        = "requires";
    public static final String spatial         = "spatial";
    public static final String tableOfContents = "tableOfContents";
    public static final String temporal        = "temporal";
}
