package eu.europeana.jena.edm;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;

import static org.apache.jena.rdf.model.ResourceFactory.*;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 14 Apr 2016
 */
public class ORE
{
    public static final String PREFIX = "ore";
    public static final String NS     = "http://www.openarchives.org/ore/terms/";

    public static final Resource Aggregation = createResource(NS + "Aggregation");
    public static final Resource Proxy       = createResource(NS + "Proxy");

    public static final Property aggregates  = createProperty(NS, "aggregates");
    public static final Property proxyFor    = createProperty(NS, "proxyFor");
    public static final Property proxyIn     = createProperty(NS, "proxyIn");
    public static final Property lineage     = createProperty(NS, "lineage");
}
