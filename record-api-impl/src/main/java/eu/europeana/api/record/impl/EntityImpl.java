package eu.europeana.api.record.impl;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.europeana.api.record.datatypes.Literal;
import eu.europeana.api.record.model.Entity;
import eu.europeana.api.record.deserialization.LiteralListConverter;

import java.util.Map;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ID, TYPE, PREF_LABEL})
public abstract class EntityImpl implements Entity {

    // ID of entity
    protected String about;

    @JsonDeserialize(converter = LiteralListConverter.class)
    protected Map<String, Literal<String>> prefLabel;

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

    public Map<String, Literal<String>> getPrefLabel() {
        return prefLabel;
    }

    @Override
    public Literal<String> getPrefLabel(String language) {
        return prefLabel.get(language);
    }

    @Override
    public void setPrefLabel(Map<String, Literal<String>> prefLabel) {
        this.prefLabel = prefLabel;
    }

    @Override
    public String toString() {
        return "EntityImpl{" +
                "about='" + about + '\'' +
                ", prefLabel=" + prefLabel +
                '}';
    }
}
