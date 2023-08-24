package eu.europeana.api.record.model;

import java.util.List;

public interface Record {

    public String getContext();

    public String getAbout();

    public void setAbout(String about);

    public List<? extends Proxy> getProxies();

    public void setProxies(List<? extends Proxy> proxies);

    public Aggregation getAggregation();

    public void setAggregation(Aggregation aggregation);

    public List<? extends WebResource> getWebResources();

    public void setWebResources(List<? extends WebResource> webResources);

    public List<? extends Agent> getAgents();

    public void setAgents(List<? extends Agent> agents);
}
