package eu.europeana.api.edm;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 14 Apr 2016
 */
public interface WGS84 
{
    public static final String PREFIX = "wgs84_pos";
    public static final String NS     = "http://www.w3.org/2003/01/geo/wgs84_pos#";
    public static final NamespaceDeclaration DECL = new NamespaceDeclaration(PREFIX, NS);

    public static final String latitude  = "lat";
    public static final String longitude = "long";
    public static final String altitude  = "alt";
}
