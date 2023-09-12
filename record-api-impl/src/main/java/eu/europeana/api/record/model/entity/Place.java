package eu.europeana.api.record.model.entity;

import dev.morphia.annotations.Entity;

@Entity(value = "ContextualEntity", discriminator = "Place", discriminatorKey = "type")
public class Place extends ContextualEntity
{
    public Place() {}

    public Place(String id)
    {
        super(id);
    }
}