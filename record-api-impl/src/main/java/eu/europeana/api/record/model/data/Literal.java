package eu.europeana.api.record.model.data;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;

@Entity(discriminatorKey = "type", useDiscriminator = false)
public class Literal<T> extends DataValue {

    @Property("value")
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
