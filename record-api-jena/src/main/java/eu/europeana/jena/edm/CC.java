package eu.europeana.jena.edm;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import static org.apache.jena.rdf.model.ResourceFactory.*;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 14 Apr 2016
 */
public class CC
{
    public static final String PREFIX = "cc";
    public static final String NS     = "http://creativecommons.org/ns#";

    public static final Resource License      = createResource(NS + "License");
    public static final Property deprecatedOn = createProperty(NS, "deprecatedOn");
}
