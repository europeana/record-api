package eu.europeana.api.edm;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 14 Apr 2016
 */
public interface XSD
{
    public static final String PREFIX  = "xsd";
    public static final String NS      = "http://www.w3.org/2001/XMLSchema#";
    public static final NamespaceDeclaration DECL = new NamespaceDeclaration(PREFIX, NS);

    public static final String xbyte              = "byte";
    public static final String xshort             = "short";
    public static final String xint               = "int";
    public static final String xinteger           = "integer";
    public static final String xlong              = "long";
    public static final String xdecimal           = "decimal";
    public static final String xfloat             = "float";
    public static final String xdouble            = "double";
    public static final String xstring            = "string";

    public static final String unsignedByte       = "unsignedByte";
    public static final String unsignedShort      = "unsignedShort";
    public static final String unsignedInt        = "unsignedInt";
    public static final String unsignedLong       = "unsignedLong";

    public static final String negativeInteger    = "negativeInteger";
    public static final String nonNegativeInteger = "nonNegativeInteger";
    public static final String nonPositiveInteger = "nonPositiveInteger";
    public static final String positiveInteger    = "positiveInteger";

    public static final String hexBinary          = "hexBinary";
    public static final String base64Binary       = "base64Binary";
    public static final String date               = "date";
    public static final String time               = "time";
    public static final String dateTime           = "dateTime";
    public static final String dateTimeStamp      = "dateTimeStamp";
    public static final String duration           = "duration";
}
