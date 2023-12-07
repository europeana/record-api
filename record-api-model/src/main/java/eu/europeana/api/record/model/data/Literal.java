package eu.europeana.api.record.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import eu.europeana.api.edm.RDF;

import static eu.europeana.api.record.model.ModelConstants.VALUE;

//@Entity(discriminatorKey = RDF.type, useDiscriminator = false)
public class Literal<T> implements DataValue
{
    @JsonProperty(VALUE)
//    @Property(VALUE)
    protected T value;


    public Literal() {}

    public Literal(T value) { this.value = value; }


    public T getValue() { return this.value; }


    public String toString() { return ('"' + this.value.toString() + '"'); }
}
