package eu.europeana.jena.edm;

import org.apache.jena.rdf.model.Property;

import static org.apache.jena.rdf.model.ResourceFactory.*;
import static org.apache.jena.rdf.model.ResourceFactory.createProperty;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 14 Apr 2016
 */
public class WGS84 
{
    public static final String PREFIX = "wgs84_pos";
    public static final String NS     = "http://www.w3.org/2003/01/geo/wgs84_pos#";

    public static final Property latitude
        = createProperty(NS, "lat");
    public static final Property longitude
        = createProperty(NS, "long");
    public static final Property altitude
        = createProperty(NS, "alt");
}
