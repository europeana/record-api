package eu.europeana.api.record.impl;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.morphia.annotations.Id;
import eu.europeana.api.record.model.EdmEntity;

import static eu.europeana.api.record.vocabulary.RecordFields.ID;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class EdmEntityImpl implements EdmEntity {

    @Id
    private String about;

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
}
