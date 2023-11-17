package eu.europeana.api.record.model.data;


import eu.europeana.api.edm.XSD;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Hugo
 * @since 9 Nov 2023
 */
public class DatatypeUtils {

    private static Map<String,Datatype> abbrv2dt = new HashMap();
    private static Map<String,Datatype> uri2dt   = new HashMap();

    public static final Datatype xbyte              = createDatatype(XSD.NS, XSD.PREFIX, XSD.xbyte);
    public static final Datatype xshort             = createDatatype(XSD.NS, XSD.PREFIX, XSD.xshort);
    public static final Datatype xint               = createDatatype(XSD.NS, XSD.PREFIX, XSD.xint);
    public static final Datatype xinteger           = createDatatype(XSD.NS, XSD.PREFIX, XSD.xinteger);
    public static final Datatype xlong              = createDatatype(XSD.NS, XSD.PREFIX, XSD.xlong);
    public static final Datatype xdecimal           = createDatatype(XSD.NS, XSD.PREFIX, XSD.xdecimal);
    public static final Datatype xfloat             = createDatatype(XSD.NS, XSD.PREFIX, XSD.xfloat);
    public static final Datatype xdouble            = createDatatype(XSD.NS, XSD.PREFIX, XSD.xdouble);
    public static final Datatype xstring            = createDatatype(XSD.NS, XSD.PREFIX, XSD.xstring);

    public static final Datatype unsignedByte       = createDatatype(XSD.NS, XSD.PREFIX, XSD.unsignedByte);
    public static final Datatype unsignedShort      = createDatatype(XSD.NS, XSD.PREFIX, XSD.unsignedShort);
    public static final Datatype unsignedInt        = createDatatype(XSD.NS, XSD.PREFIX, XSD.unsignedInt);
    public static final Datatype unsignedLong       = createDatatype(XSD.NS, XSD.PREFIX, XSD.unsignedLong);

    public static final Datatype negativeInteger    = createDatatype(XSD.NS, XSD.PREFIX, XSD.negativeInteger);
    public static final Datatype nonNegativeInteger = createDatatype(XSD.NS, XSD.PREFIX, XSD.nonNegativeInteger);
    public static final Datatype nonPositiveInteger = createDatatype(XSD.NS, XSD.PREFIX, XSD.nonPositiveInteger);
    public static final Datatype positiveInteger    = createDatatype(XSD.NS, XSD.PREFIX, XSD.positiveInteger);

    public static final Datatype hexBinary          = createDatatype(XSD.NS, XSD.PREFIX, XSD.hexBinary);
    public static final Datatype base64Binary       = createDatatype(XSD.NS, XSD.PREFIX, XSD.base64Binary);
    public static final Datatype date               = createDatatype(XSD.NS, XSD.PREFIX, XSD.date);
    public static final Datatype time               = createDatatype(XSD.NS, XSD.PREFIX, XSD.time);
    public static final Datatype dateTime           = createDatatype(XSD.NS, XSD.PREFIX, XSD.dateTime);
    public static final Datatype dateTimeStamp      = createDatatype(XSD.NS, XSD.PREFIX, XSD.dateTimeStamp);
    public static final Datatype duration           = createDatatype(XSD.NS, XSD.PREFIX, XSD.duration);

    private static Datatype createDatatype(String ns, String prefix, String localName) {
        Datatype dt = new Datatype(ns + localName, prefix + ":" +  localName);
        abbrv2dt.put(dt.getQName(), dt);
        uri2dt.put(dt.getURI(), dt);
        return dt;
    }

    public static Datatype getDatatypeByAbbrv(String abbrv) {
        return abbrv2dt.get(abbrv);
    }

    public static Datatype getDatatypeByUri(String uri) {
        return uri2dt.get(uri);
    }

    public static Datatype resolveDatatype(String str) {
        Datatype dt;
        if ( StringUtils.isEmpty(str) ) { return null; }

        dt = DatatypeUtils.getDatatypeByAbbrv(str);
        if ( dt != null ) { return dt; }

        dt = DatatypeUtils.getDatatypeByUri(str);
        if ( dt != null ) { return dt; }

        return new Datatype(str, null);
    }
}
