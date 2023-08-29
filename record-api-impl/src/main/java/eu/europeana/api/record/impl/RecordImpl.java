package eu.europeana.api.record.impl;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexed;
import eu.europeana.api.record.model.*;

import java.util.List;

import eu.europeana.api.record.model.Record;
import org.bson.types.ObjectId;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

@JsonPropertyOrder({CONTEXT, ID, PROXIES, IS_AGGREGATED_BY, WEB_RESOURCES, AGENTS})
@Entity(value = "Record", useDiscriminator = false)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
// TODO add custom validators later once we know field validations
public class RecordImpl implements Record {

    @Id
    @JsonIgnore
    private ObjectId dbId;

    @Indexed(options = @IndexOptions(unique = true))
    private String about;

    @JsonDeserialize(contentAs = ProxyImpl.class)
    private List<ProxyImpl> proxies;

    @JsonDeserialize(as = AggregationImpl.class)
    private AggregationImpl aggregation;

    @JsonDeserialize(contentAs = WebResourceImpl.class)
    private List<WebResourceImpl> webResources;

    @JsonDeserialize(contentAs = AgentImpl.class)
    private List<AgentImpl> agents;

    protected RecordImpl() {}


    @Override
    @JsonGetter(CONTEXT)
    public String getContext() {
        return EDM_CONTEXT;
    }

    public ObjectId getDbId() {
        return dbId;
    }

    public void setDbId(ObjectId dbId) {
        this.dbId = dbId;
    }

    @Override
    @JsonGetter(ID)
    public String getAbout() {
        return about;
    }

    @Override
    @JsonSetter(ID)
    public void setAbout(String about) {
        this.about = about;
    }

    @Override
    public List<? extends Proxy> getProxies() {
        return proxies;
    }

    @Override
    public void setProxies(List<? extends Proxy> proxies) {
        this.proxies = (List<ProxyImpl>) proxies;
    }

    @Override
    @JsonGetter(IS_AGGREGATED_BY)
    public Aggregation getAggregation() {
        return aggregation;
    }

    @Override
    public void setAggregation(Aggregation aggregation) {
        this.aggregation = (AggregationImpl) aggregation;
    }

    @Override
    public List<? extends WebResource> getWebResources() {
        return webResources;
    }

    @Override
    public void setWebResources(List<? extends WebResource> webResources) {
        this.webResources = (List<WebResourceImpl>) webResources;
    }

    @Override
    public List<? extends Agent> getAgents() {
        return agents;
    }

    @Override
    public void setAgents(List<? extends Agent> agents) {
        this.agents = (List<AgentImpl>) agents;
    }
}
