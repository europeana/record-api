package eu.europeana.api.record.impl;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import dev.morphia.annotations.Entity;
import eu.europeana.api.record.datatypes.Literal;
import eu.europeana.api.record.datatypes.ObjectReference;
import eu.europeana.api.record.model.Aggregation;
import eu.europeana.api.record.deserialization.ObjectReferenceConverter;
import eu.europeana.api.record.deserialization.LiteralStringConverter;
import eu.europeana.api.record.vocabulary.FieldTypes;

import java.util.List;

import static eu.europeana.api.record.vocabulary.RecordFields.ID;
import static eu.europeana.api.record.vocabulary.RecordFields.TYPE;

@Entity(useDiscriminator = false)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ID, TYPE})
public class AggregationImpl extends EdmEntityImpl implements Aggregation {

    private Literal<String> type = new LiteralImpl<>(FieldTypes.Aggregation.getFieldType());

    @JsonDeserialize(converter = LiteralStringConverter.class)
    private Literal<String> isShownBy;

    @JsonDeserialize(converter = ObjectReferenceConverter.class)
    private List<ObjectReference> hasView;


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
    public List<ObjectReference> getHasView() {
        return hasView;
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
    public void setHasView(List<ObjectReference> hasView) {
        this.hasView = hasView;
    }

    @Override
    public String toString() {
        return "AggregationImpl{" +
                "type=" + type +
                ", isShownBy=" + isShownBy +
                ", hasView=" + hasView +
                '}';
    }
}
