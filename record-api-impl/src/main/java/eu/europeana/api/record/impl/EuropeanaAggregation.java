package eu.europeana.api.record.impl;

import eu.europeana.api.record.datatypes.Literal;

public class EuropeanaAggregation extends EdmEntity {

    private Literal<String> type;

    public Literal<String> getType() {
        return type;
    }
}
