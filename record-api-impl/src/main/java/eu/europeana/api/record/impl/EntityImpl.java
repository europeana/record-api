package eu.europeana.api.record.impl;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.europeana.api.record.datatypes.Literal;
import eu.europeana.api.record.model.Entity;
import eu.europeana.api.record.serialization.LiteralMapConverter;

import static eu.europeana.api.record.vocabulary.RecordFields.ID;

import java.util.Map;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class EntityImpl implements Entity {

    // ID of entity
    protected String about;

    @JsonDeserialize(converter = LiteralMapConverter.class)
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
}
