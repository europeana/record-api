package eu.europeana.api.record.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;

import static eu.europeana.api.record.vocabulary.RecordFields.DATATYPE;

@Entity(useDiscriminator = false)
public class DatatypeLiteral<T> extends Literal<T> {

    @JsonProperty(DATATYPE)
    @Property(DATATYPE)
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
