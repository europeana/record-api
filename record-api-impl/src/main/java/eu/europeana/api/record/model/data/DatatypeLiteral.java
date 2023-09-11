package eu.europeana.api.record.model.data;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;

@Entity(useDiscriminator = false)
public class DatatypeLiteral<T> extends Literal<T> {

    @Property("datatype")
    protected String datatype;

    public DatatypeLiteral() {
    }

    public DatatypeLiteral(T value, String datatype) {
        this.value = value;
        this.datatype = datatype;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }
}
