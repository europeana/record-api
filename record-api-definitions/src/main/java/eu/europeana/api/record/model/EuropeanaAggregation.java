package eu.europeana.api.record.model;

import eu.europeana.api.record.datatypes.Literal;

public interface EuropeanaAggregation {

    Literal<String> getType();

    void setType(Literal<String> type);
}
