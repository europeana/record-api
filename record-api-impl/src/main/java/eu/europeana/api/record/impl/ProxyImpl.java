package eu.europeana.api.record.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.morphia.annotations.Entity;
import eu.europeana.api.record.datatypes.DataValue;
import eu.europeana.api.record.datatypes.Literal;
import eu.europeana.api.record.deserialization.*;
import eu.europeana.api.record.model.EuropeanaAggregation;
import eu.europeana.api.record.model.Proxy;
import eu.europeana.api.record.vocabulary.FieldTypes;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

// It is now possible to use @Entity everywhere. If a type is only for use as an embedded value, no @Id field is necessary.
@Entity(useDiscriminator = false)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ID, TYPE})
public class ProxyImpl extends EdmEntityImpl implements Proxy {

    private Literal<String> type = new LiteralImpl<>(FieldTypes.Proxy.getFieldType());

    @JsonDeserialize(converter = LiteralMapConverter.class)
    private Map<String, Literal<String>> title;

    @JsonDeserialize(converter = LiteralMultiValueMapConverter.class)
    private Map<String, List<Literal<String>>> alternative;

    @JsonDeserialize(converter = DataValueConverter.class)
    private List<? extends DataValue> description;

    @JsonDeserialize(converter = DataValueConverter.class)
    private List<? extends DataValue> creator;

    @JsonDeserialize(converter = LiteralListConverter.class)
    private List<Literal<String>> identifier;

    @JsonDeserialize(as = EuropeanaAggregationImpl.class)
    private EuropeanaAggregationImpl proxyIn;

    @JsonDeserialize(converter = LiteralConverter.class)
    private Literal<String> proxyFor;


    public ProxyImpl() {
    }

    @Override
    public Literal<String> getType() {
        return type;
    }

    @Override
    public void setType(Literal<String> type) {
        this.type = type;
    }

    public Map<String, Literal<String>> getTitle() {
        return title;
    }

    @Override
    public void setTitle(Map<String, Literal<String>> title) {
        this.title = title;
    }

    public Map<String, List<Literal<String>>> getAlternative() {
        return alternative;
    }

    @Override
    public void setAlternative(Map<String, List<Literal<String>>> alternative) {
        this.alternative = alternative;
    }

    @Override
    public List<? extends DataValue> getDescription() {
        return description;
    }

    @Override
    public void setDescription(List<? extends DataValue> description) {
        this.description =  description;
    }

    @Override
    public List<? extends DataValue> getCreator() {
        return creator;
    }

    @Override
    public void setCreator(List<? extends DataValue> creator) {
        this.creator =  creator;
    }

    @Override
    public List<Literal<String>> getIdentifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(List<Literal<String>> identifier) {
        this.identifier = identifier;
    }

    @Override
    public EuropeanaAggregation getProxyIn() {
        return proxyIn;
    }

    public void setProxyIn(EuropeanaAggregation proxyIn) {
        this.proxyIn = (EuropeanaAggregationImpl) proxyIn;
    }

    @Override
    public Literal<String> getProxyFor() {
        return proxyFor;
    }

    @Override
    public Literal<String> getTitle(String language) {
        return this.title.get(language);
    }

    @Override
    public void setProxyFor(Literal<String> proxyFor) {
        this.proxyFor = proxyFor;
    }

    @Override
    public Literal<String> getAlternative(String language) {
        return alternative.get(language).get(0);
    }


    // Getters to fetch the non language @none values

    @Override
    public List<Literal<String>> getAlternatives() {
        if (this.alternative != null && this.alternative.containsKey(NONE)) {
            return alternative.get(NONE);

        }
        return Collections.emptyList();
    }


    @Override
    public String toString() {
        return "ProxyImpl{" +
                "type=" + type +
                ", title=" + title +
                ", alternative=" + alternative +
                ", description=" + description +
                ", creator=" + creator +
                ", identifier=" + identifier +
                ", proxyIn=" + proxyIn +
                ", proxyFor=" + proxyFor +
                '}';
    }
}
