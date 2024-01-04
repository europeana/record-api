package eu.europeana.jena.edm;

import org.apache.jena.rdf.model.Property;

import static org.apache.jena.rdf.model.ResourceFactory.*;
import static org.apache.jena.rdf.model.ResourceFactory.createProperty;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 15 Apr 2016
 */
public class DOAP
{
    public static final String PREFIX = "doap";
    public static final String NS     = "http://usefulinc.com/ns/doap#";

    public static final Property impls = createProperty(NS + "implements");
}
