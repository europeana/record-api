package eu.europeana.api.record.model;

public interface Proxy {

    ProvidedCHO getProxyFor();

    void setProxyFor(ProvidedCHO cho);

    Aggregation getProxyIn();

    void setProxyIn(Aggregation aggr);


}
