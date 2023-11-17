package eu.europeana.api.edm;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 14 Apr 2016
 */
public interface SKOS
{
    public static final String PREFIX = "skos";
    public static final String NS     = "http://www.w3.org/2004/02/skos/core#";
    public static final NamespaceDeclaration DECL = new NamespaceDeclaration(PREFIX, NS);

    public static final String Concept      = "Concept";

    public static final String prefLabel    = "prefLabel";
    public static final String altLabel     = "altLabel";
    public static final String hiddenLabel  = "hiddenLabel";

    public static final String note         = "note";
    public static final String notation     = "notation";

    public static final String broader      = "broader";
    public static final String narrower     = "narrower";
    public static final String related      = "related";

    public static final String exactMatch   = "exactMatch";
    public static final String closeMatch   = "closeMatch";
    public static final String broadMatch   = "broadMatch";
    public static final String narrowMatch  = "narrowMatch";
    public static final String relatedMatch = "relatedMatch";

    public static final String inScheme     = "inScheme";
}
