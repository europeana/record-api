package eu.europeana.api.record.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.morphia.annotations.Entity;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true, property = "type")
@JsonPropertyOrder({ ID, TYPE, PREF_LABELS })
@Entity(value = "ContextualEntity", discriminator = "Agent", discriminatorKey = "type")
public class Agent extends ContextualEntity {

    public Agent() {}

    public Agent(String id)
    {
        super(id);
    }

    public String getType() { return "Agent"; }
}
