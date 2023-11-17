package eu.europeana.jena.edm;

import org.apache.jena.rdf.model.Property;

import static org.apache.jena.rdf.model.ResourceFactory.*;
import static org.apache.jena.rdf.model.ResourceFactory.createProperty;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 14 Apr 2016
 */
public class ODRL
{
    public static final String PREFIX = "odrl";
    public static final String NS     = "http://www.w3.org/ns/odrl/2/";

    public static final Property inheritFrom = createProperty(NS, "inheritFrom");
}
