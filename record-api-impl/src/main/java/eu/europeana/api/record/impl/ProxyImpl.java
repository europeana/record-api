package eu.europeana.api.record.impl;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.morphia.annotations.Entity;
import eu.europeana.api.record.datatypes.Literal;
import eu.europeana.api.record.model.EuropeanaAggregation;
import eu.europeana.api.record.model.Proxy;
import eu.europeana.api.record.deserialization.LiteralStringConverter;
import eu.europeana.api.record.deserialization.LiteralMapConverter;
import static eu.europeana.api.record.vocabulary.RecordFields.NONE;

import java.util.Collections;
import java.util.List;
import java.util.Map;

// It is now possible to use @Entity everywhere. If a type is only for use as an embedded value, no @Id field is necessary.
@Entity(useDiscriminator = false)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProxyImpl extends EdmEntityImpl implements Proxy {

    @JsonDeserialize(converter = LiteralStringConverter.class)
    private Literal<String> type;

    @JsonDeserialize(converter = LiteralMapConverter.class)
    private Map<String, List<Literal<String>>> title;

    @JsonDeserialize(converter = LiteralMapConverter.class)
    private Map<String, List<Literal<String>>> alternative;

    @JsonDeserialize(converter = LiteralMapConverter.class)
    private Map<String, List<Literal<String>>> description;

    @JsonDeserialize(converter = LiteralMapConverter.class)
    private Map<String, List<Literal<String>>> creator;

    @JsonDeserialize(converter = LiteralMapConverter.class)
    private Map<String, List<Literal<String>>> identifier;

    @JsonDeserialize(as = EuropeanaAggregationImpl.class)
    private EuropeanaAggregationImpl proxyIn;

    @JsonDeserialize(converter = LiteralStringConverter.class)
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

    public Map<String, List<Literal<String>>> getTitle() {
        return title;
    }

    @Override
    public void setTitle(Map<String, List<Literal<String>>> title) {
        this.title = title;
    }

    public Map<String, List<Literal<String>>> getAlternative() {
        return alternative;
    }

    @Override
    public void setAlternative(Map<String, List<Literal<String>>> alternative) {
        this.alternative = alternative;
    }

    public Map<String, List<Literal<String>>> getDescription() {
        return description;
    }

    @Override
    public void setDescription(Map<String, List<Literal<String>>> description) {
        this.description = description;
    }

    public Map<String, List<Literal<String>>> getCreator() {
        return creator;
    }

    @Override
    public void setCreator(Map<String, List<Literal<String>>> creator) {
        this.creator = creator;
    }

    public Map<String, List<Literal<String>>> getIdentifier() {
        return identifier;
    }

    @Override
    public void setIdentifier(Map<String, List<Literal<String>>> identifier) {
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
    public void setProxyFor(Literal<String> proxyFor) {
        this.proxyFor = proxyFor;
    }

    // Getters to fetch the language value

    @Override
    public Literal<String>  getTitle(String language) {
        return title.get(language).get(0);
    }

    @Override
    public Literal<String> getCreator(String language) {
        return creator.get(language).get(0);
    }

    @Override
    public Literal<String> getAlternative(String language) {
        return alternative.get(language).get(0);
    }

    @Override
    public Literal<String> getDescription(String language) {
        return description.get(language).get(0);
    }

    @Override
    public Literal<String> getIdentifier(String language) {
        return identifier.get(language).get(0);
    }


    // Getters to fetch the non language @none values

    @Override
    public List<Literal<String>> getTitles() {
        if (this.title != null && this.title.containsKey(NONE)) {
            return title.get(NONE);

        }
        return Collections.emptyList();
    }

    @Override
    public List<Literal<String>> getAlternatives() {
        if (this.alternative != null && this.alternative.containsKey(NONE)) {
            return alternative.get(NONE);

        }
        return Collections.emptyList();
    }

    @Override
    public List<Literal<String>> getCreators() {
        if (this.creator != null && this.creator.containsKey(NONE)) {
            return creator.get(NONE);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Literal<String>> getDescriptions() {
        if (this.description != null && this.description.containsKey(NONE)) {
            return description.get(NONE);
        }
        return Collections.emptyList();
    }

    @Override
    public List<Literal<String>> getIdentifiers() {
        if (this.identifier != null && this.identifier.containsKey(NONE)) {
            return identifier.get(NONE);

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
