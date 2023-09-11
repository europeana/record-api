package eu.europeana.api.record.model.entity;

import dev.morphia.annotations.Entity;
import eu.europeana.api.record.model.ContextualEntity;

@Entity(value = "ContextualEntity", discriminator = "Agent", discriminatorKey = "type")
public class Agent extends ContextualEntity {

    public Agent() {
    }

    public Agent(String id)
    {
        super(id);
    }

}
