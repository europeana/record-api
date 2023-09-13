package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import dev.morphia.annotations.Transient;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

// It is now possible to use @Entity everywhere. If a type is only for use as an embedded value, no @Id field is necessary.
@Entity(discriminator = "Proxy", discriminatorKey = "type")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({TYPE, TITLE, ALTERNATIVE_TITLE, CREATOR, DESCRIPTION, IDENTIFIER, PROXY_IN, PROXY_FOR})
public class Proxy extends ObjectMetadata {

    @Transient
    private ProvidedCHO proxyFor;

    @Property(PROXY_IN)
    private Aggregation proxyIn;

    @JsonGetter(PROXY_FOR)
    public ProvidedCHO getProxyFor()
    {
        return (ProvidedCHO) proxyFor;
    }

    public void setProxyFor(ProvidedCHO cho)
    {
        this.proxyFor = (ProvidedCHO) cho;
    }

    @JsonGetter(PROXY_IN)
    public Aggregation getProxyIn()
    {
        return (Aggregation) proxyIn;
    }

    public void setProxyIn(Aggregation aggr)
    {
        this.proxyIn = (Aggregation) aggr;
    }

//    @JsonGetter(TYPE)
//    public String getType() {
//        return "Proxy";
//    }

}
