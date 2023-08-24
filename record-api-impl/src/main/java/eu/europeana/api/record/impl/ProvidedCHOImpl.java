package eu.europeana.api.record.impl;

import com.fasterxml.jackson.annotation.*;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import eu.europeana.api.record.model.ProvidedCHO;
import eu.europeana.api.record.vocabulary.EntityTypes;

import static eu.europeana.api.record.vocabulary.RecordFields.TYPE;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProvidedCHOImpl extends EdmEntityImpl implements ProvidedCHO {

    private String type = EntityTypes.Agent.getEntityType();

    public ProvidedCHOImpl() {}

    @Override
    @JsonGetter(TYPE)
    public String getType() {
        return type;
    }

    @Override
    @JsonSetter(TYPE)
    public void setType(String type) {
        this.type = type;
    }
}
