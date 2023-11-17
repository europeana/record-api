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

    public static final String xbyte              = NS + "byte";
    public static final String xshort             = NS + "short";
    public static final String xint               = NS + "int";
    public static final String xinteger           = NS + "integer";
    public static final String xlong              = NS + "long";
    public static final String xdecimal           = NS + "decimal";
    public static final String xfloat             = NS + "float";
    public static final String xdouble            = NS + "double";
    public static final String xstring            = NS + "string";

    public static final String unsignedByte       = NS + "unsignedByte";
    public static final String unsignedShort      = NS + "unsignedShort";
    public static final String unsignedInt        = NS + "unsignedInt";
    public static final String unsignedLong       = NS + "unsignedLong";

    public static final String negativeInteger    = NS + "negativeInteger";
    public static final String nonNegativeInteger = NS + "nonNegativeInteger";
    public static final String nonPositiveInteger = NS + "nonPositiveInteger";
    public static final String positiveInteger    = NS + "positiveInteger";

    public static final String hexBinary          = NS + "hexBinary";
    public static final String base64Binary       = NS + "base64Binary";
    public static final String date               = NS + "date";
    public static final String time               = NS + "time";
    public static final String dateTime           = NS + "dateTime";
    public static final String dateTimeStamp      = NS + "dateTimeStamp";
    public static final String duration           = NS + "duration";
}
