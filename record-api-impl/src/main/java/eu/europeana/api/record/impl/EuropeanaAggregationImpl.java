package eu.europeana.api.record.impl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import eu.europeana.api.record.datatypes.Literal;
import eu.europeana.api.record.model.EuropeanaAggregation;
import eu.europeana.api.record.serialization.LiteralStringConverter;

public class EuropeanaAggregationImpl extends EdmEntityImpl implements EuropeanaAggregation {

    @JsonDeserialize(converter = LiteralStringConverter.class)
    private Literal<String> type;

    public EuropeanaAggregationImpl() {
    }

    @Override
    public Literal<String> getType() {
        return type;
    }

    @Override
    public void setType(Literal<String> type) {
        this.type = type;
    }

}
