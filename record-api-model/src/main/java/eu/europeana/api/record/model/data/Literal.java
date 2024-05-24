package eu.europeana.api.record.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.morphia.annotations.Property;
import eu.europeana.api.record.model.ModelConstants;

public class Literal<T> implements DataValue
{
    @JsonProperty(ModelConstants.value)
    @Property(ModelConstants.val)
    protected T value;


    public Literal() {}

    public Literal(T value) { this.value = value; }


    public T getValue() { return this.value; }


    public String toString() { return ('"' + this.value.toString() + '"'); }
}
