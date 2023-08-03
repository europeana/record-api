package eu.europeana.recordapi.model;

import eu.europeana.recordapi.datatypes.Literal;

public class EuropeanaAggregation extends EdmEntity {

    private Literal<String> type;

    public Literal<String> getType() {
        return type;
    }
}
