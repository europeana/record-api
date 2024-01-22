package eu.europeana.api.edm;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 14 Apr 2016
 */
public interface DC
{
    public static final String PREFIX = "dc";
    public static final String NS     = "http://purl.org/dc/elements/1.1/";
    public static final NamespaceDeclaration DECL = new NamespaceDeclaration(PREFIX, NS);

    public static final String contributor   = "contributor";
    public static final String coverage      = "coverage";
    public static final String creator       = "creator";
    public static final String date          = "date";
    public static final String description   = "description";
    public static final String format        = "format";
    public static final String identifier    = "identifier";
    public static final String language      = "language";
    public static final String publisher     = "publisher";
    public static final String relation      = "relation";
    public static final String rights        = "rights";
    public static final String source        = "source";
    public static final String subject       = "subject";
    public static final String title         = "title";
    public static final String type          = "type";
}
