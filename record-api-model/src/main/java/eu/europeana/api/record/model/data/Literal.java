package eu.europeana.api.record.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu.europeana.api.record.model.ModelConstants;

public class Literal<T> implements DataValue
{
    @JsonProperty(ModelConstants.value)
//    @Property(VALUE)
    protected T value;


    public Literal() {}

    public Literal(T value) { this.value = value; }


    public T getValue() { return this.value; }


    public String toString() { return ('"' + this.value.toString() + '"'); }
}
