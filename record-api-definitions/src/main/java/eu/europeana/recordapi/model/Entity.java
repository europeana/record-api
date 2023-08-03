package eu.europeana.recordapi.model;

import eu.europeana.recordapi.datatypes.Literal;

import java.util.Map;

public abstract class Entity {

    // ID of entity
    protected String about;
    protected Map<Literal<String>, Literal<String>> prefLabel;
}
