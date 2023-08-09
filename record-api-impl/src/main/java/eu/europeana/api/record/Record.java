package eu.europeana.api.record;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import eu.europeana.api.record.impl.*;
import eu.europeana.api.record.model.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
// TODO add custom validators later once we know field validations
public class Record {

    @Id
    private String id;

//    @JsonDeserialize(as = ProvidedCHOImpl.class)
//    private ProvidedCHO providedCHO;

    @JsonDeserialize(contentAs = ProxyImpl.class)
    private List<? extends Proxy> proxies;

    @JsonDeserialize(as = AggregationImpl.class)
    private Aggregation aggregation;

    @JsonDeserialize(contentAs = WebResourceImpl.class)
    private List<? extends WebResource> webResources;

    @JsonDeserialize(contentAs = AgentImpl.class)
    private List<? extends Agent> agents;

    protected  Record() {}


    @JsonGetter(CONTEXT)
    public String getContext() {
        return EDM_CONTEXT;
    }

    @JsonGetter(ID)
    public String getId() {
        return id;
    }

    @JsonSetter(ID)
    public void setId(String id) {
        this.id = id;
    }

    //    public ProvidedCHO getProvidedCHO() {
//        return providedCHO;
//    }
//
//    public void setProvidedCHO(ProvidedCHO providedCHO) {
//        this.providedCHO = providedCHO;
//    }

    public List<? extends Proxy> getProxies() {
        return proxies;
    }

    public void setProxies(List<? extends Proxy> proxies) {
        this.proxies =  proxies;
    }

    public Aggregation getAggregation() {
        return aggregation;
    }

    public void setAggregation(Aggregation aggregation) {
        this.aggregation =  aggregation;
    }

    public List<? extends WebResource> getWebResources() {
        return webResources;
    }

    public void setWebResources(List<? extends WebResource> webResources) {
        this.webResources =  webResources;
    }

    public List<? extends Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<? extends Agent> agents) {
        this.agents = agents;
    }
}
