package eu.europeana.api.record.impl;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.morphia.annotations.Entity;
import eu.europeana.api.record.datatypes.Literal;
import eu.europeana.api.record.model.EuropeanaAggregation;
import eu.europeana.api.record.deserialization.LiteralStringConverter;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

@Entity(useDiscriminator = false)
@JsonPropertyOrder({ID, TYPE})
public class EuropeanaAggregationImpl extends EdmEntityImpl implements EuropeanaAggregation {

    // we need the type in the input to identify which proxy has Europeana aggregation OR
    // figure out a way to identify this
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
