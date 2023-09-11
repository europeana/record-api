package eu.europeana.api.record.model;

import java.util.List;

public interface ProvidedCHO {

    public String getContext();

    public String getId();

    public void setId(String id);

    public List<? extends Proxy> getProxies();

    public void setProxies(List<? extends Proxy> proxies);

    public Aggregation getIsAggregatedBy();

    public void setIsAggregatedBy(Aggregation aggregation);

    public List<? extends WebResource> getWebResources();

    public void setWebResources(List<? extends WebResource> webResources);

    public List<? extends ContextualEntity> getAgents();

    public void setAgents(List<? extends ContextualEntity> agents);
}
