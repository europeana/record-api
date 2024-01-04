package eu.europeana.api.edm;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 14 Apr 2016
 */
public interface VCARD
{
    public static final String PREFIX = "vcard";
    public static final String NS     = "http://www.w3.org/2006/vcard/ns#";
    public static final NamespaceDeclaration DECL = new NamespaceDeclaration(PREFIX, NS);

    public static final String Address       = "Address";
    public static final String streetAddress = "streetAddress";
    public static final String postalCode    = "postalCode";
    public static final String postOfficeBox = "postOfficeBox";
    public static final String locality      = "locality";
    public static final String countryName   = "countryName";
    public static final String hasGeo        = "hasGeo";

    public static final String rdfStreetAddress = "street-address";
    public static final String rdfPostalCode    = "postal-code";
    public static final String rdfPostOfficeBox = "post-office-box";
    public static final String rdfCountryName   = "country-name";
}
