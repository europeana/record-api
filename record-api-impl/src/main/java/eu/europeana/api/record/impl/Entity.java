package eu.europeana.api.record.impl;

import eu.europeana.api.record.datatypes.Literal;

import java.util.Map;

public abstract class Entity {

    // ID of entity
    protected String about;
    protected Map<Literal<String>, Literal<String>> prefLabel;
}
