package eu.europeana.api.record.impl;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.morphia.annotations.Entity;
import eu.europeana.api.record.datatypes.Literal;
import eu.europeana.api.record.datatypes.ObjectReference;
import eu.europeana.api.record.model.Aggregation;
import eu.europeana.api.record.serialization.LiteralStringConverter;

import java.util.List;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AggregationImpl extends EdmEntityImpl implements Aggregation {

    @JsonDeserialize(converter = LiteralStringConverter.class)
    private Literal<String> type;

    @JsonDeserialize(converter = LiteralStringConverter.class)
    private Literal<String> isShownBy;

    /**
     * Multiple object reference.
     * For now, we can deserialize as List<Literal<string>>
     * As has Views only contains strings and
     * not the referenced object yet when registering a record.
     */
    @JsonDeserialize(converter = LiteralStringConverter.class)
    private List<ObjectReference> hasViews;


    public AggregationImpl() {
    }

    @Override
    public Literal<String> getType() {
        return type;
    }

    @Override
    public Literal<String> getIsShownBy() {
        return isShownBy;
    }

    @Override
    public List<ObjectReference> getHasViews() {
        return hasViews;
    }

    @Override
    public void setType(Literal<String> type) {
        this.type = type;
    }

    @Override
    public void setIsShownBy(Literal<String> isShownBy) {
        this.isShownBy = isShownBy;
    }

    @Override
    public void setHasViews(List<ObjectReference> hasViews) {
        this.hasViews = hasViews;
    }
}
