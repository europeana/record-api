package eu.europeana.api.record.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;

import static eu.europeana.api.record.vocabulary.RecordFields.VALUE;
@Entity(discriminatorKey = "type", useDiscriminator = false)
public class Literal<T> extends DataValue {

    @JsonProperty(VALUE)
    @Property(VALUE)
    protected T value;


    public Literal() {
    }

    public Literal(T value) {
        this.value = value;
    }


    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
