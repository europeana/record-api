package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.morphia.annotations.*;
import dev.morphia.annotations.Entity;
import eu.europeana.api.record.model.entity.Agent;

import java.util.List;

import org.bson.types.ObjectId;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

@JsonPropertyOrder({CONTEXT, ID, PROXIES, IS_AGGREGATED_BY, WEB_RESOURCES, AGENTS})
@Entity(value = "Record", discriminator = "ProvidedCHO", discriminatorKey = "type")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
// TODO add custom validators later once we know field validations
public class ProvidedCHO extends ObjectMetadata {

    @Id
    @JsonIgnore
    private ObjectId dbId;

    @Indexed(options = @IndexOptions(unique = true))
    @Property("id")
    private String id;

    @Property("proxies")
    private List<Proxy> proxies;

    @Property("isAggregatedBy")
    private Aggregation isAggregatedBy;


    @Property("agents")
    private List<Agent> agents;

    public ProvidedCHO() {
    }

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

    @JsonGetter(ID)
    public String getId() {
        return id;
    }

    @JsonSetter(ID)
    public void setId(String id) {
        this.id = id;
    }

    public List<Proxy> getProxies() {
        return proxies;
    }

    public void setProxies(List<Proxy> proxies) {
        this.proxies = proxies;
    }

    public Aggregation getIsAggregatedBy() {
        return this.isAggregatedBy;
    }

    public void setIsAggregatedBy(Aggregation isAggregatedBy) {
        this.isAggregatedBy = isAggregatedBy;
    }

    public List<? extends ContextualEntity> getAgents() {
        return agents;
    }

    public void setAgents(List<? extends ContextualEntity> agents) {
        this.agents = (List<Agent>) agents;
    }
}
