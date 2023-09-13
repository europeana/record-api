package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import dev.morphia.annotations.Transient;
import eu.europeana.api.record.json.EDMClassReferenceSerializer;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

// It is now possible to use @Entity everywhere. If a type is only for use as an embedded value, no @Id field is necessary.
@Entity(discriminator = "Proxy", discriminatorKey = "type")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true, property = "type")
@JsonPropertyOrder({ID, TYPE, TITLE, ALTERNATIVE_TITLE, CREATOR, DESCRIPTION, IDENTIFIER, PROXY_IN, PROXY_FOR})
public class Proxy extends ObjectMetadata implements EDMClass {

    @JsonProperty(ID)
    @Property(ID)
    private String  id;

    @JsonSerialize(using = EDMClassReferenceSerializer.class)
    @Transient
    private ProvidedCHO proxyFor;

    @JsonProperty(PROXY_IN)
    @Property(PROXY_IN)
    private Aggregation proxyIn;

    public Proxy(String id)
    {
        this.id = id;
    }

    public Proxy() {}

    @Override
    public String getID() {
        return this.id;
    }

    public String getType() { return "Proxy"; }

    public ProvidedCHO getProxyFor() {
        return (ProvidedCHO) proxyFor;
    }

    public void setProxyFor(ProvidedCHO cho) {
        this.proxyFor = (ProvidedCHO) cho;
    }

    public Aggregation getProxyIn() {
        return (Aggregation) proxyIn;
    }

    public void setProxyIn(Aggregation aggr) {
        this.proxyIn = (Aggregation) aggr;
    }


}
