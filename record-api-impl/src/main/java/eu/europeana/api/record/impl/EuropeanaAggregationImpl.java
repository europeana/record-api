package eu.europeana.api.record.impl;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.morphia.annotations.Entity;
import eu.europeana.api.record.datatypes.Literal;
import eu.europeana.api.record.model.EuropeanaAggregation;
import eu.europeana.api.record.deserialization.LiteralStringConverter;

@Entity(useDiscriminator = false)
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
