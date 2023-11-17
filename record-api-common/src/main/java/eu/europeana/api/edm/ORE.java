package eu.europeana.api.edm;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 14 Apr 2016
 */
public interface ORE
{
    public static final String PREFIX = "ore";
    public static final String NS     = "http://www.openarchives.org/ore/terms/";
    public static final NamespaceDeclaration DECL = new NamespaceDeclaration(PREFIX, NS);

    public static final String Aggregation    = "Aggregation";
    public static final String Proxy          = "Proxy";

    public static final String aggregates     = "aggregates";
    public static final String proxyFor       = "proxyFor";
    public static final String proxyIn        = "proxyIn";
    public static final String lineage        = "lineage";
    public static final String isAggregatedBy = "isAggregatedBy";
}
