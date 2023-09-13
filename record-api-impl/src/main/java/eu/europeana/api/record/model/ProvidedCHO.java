package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import dev.morphia.Datastore;
import dev.morphia.annotations.*;
import dev.morphia.annotations.Entity;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

@Entity(value = "Record", discriminator = "ProvidedCHO", discriminatorKey = "type")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonAppend(prepend = true, attrs = { @JsonAppend.Attr(value = CONTEXT) })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true, property = "type")
@JsonPropertyOrder({CONTEXT, ID, TYPE, PROXIES, IS_AGGREGATED_BY})
// TODO add custom validators later once we know field validations
public class ProvidedCHO extends ObjectMetadata implements EDMClass {

    @Id
    private ObjectId dbId;

    @JsonProperty(ID)
    @Indexed(options = @IndexOptions(unique = true))
    @Property(ID)
    private String id;

    @JsonProperty(PROXIES)
    @Property(PROXIES)
    private List<Proxy> proxies;

    @JsonProperty(IS_AGGREGATED_BY)
    @Property(IS_AGGREGATED_BY)
    private Aggregation isAggregatedBy;


    public ProvidedCHO() {
    }

    public String getType() { return "ProvidedCHO"; }

    public String getID()
    {
        return id;
    }

    public void setID(String id)
    {
        this.id = id;
    }

    public List<Proxy> getProxies() {
        return this.proxies;
    }

    public Aggregation getIsAggregatedBy()
    {
        return isAggregatedBy;
    }

    public void setIsAggregatedBy(Aggregation aggr)
    {
        isAggregatedBy = aggr;
    }

    public void addProxy(Proxy proxy) {
        if (this.proxies == null) {
            this.proxies = new ArrayList<>();
        }
        this.proxies.add(proxy);
        proxy.setProxyFor(this);
    }

    @PostLoad
    public void postLoad(Document document, Datastore ds) {
        for ( Proxy proxy : getProxies() ) { proxy.setProxyFor(this); }
    }
}
