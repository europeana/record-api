/**
 * 
 */
package eu.europeana.api.record.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import eu.europeana.api.record.io.json.DatatypeSerializer;
import static eu.europeana.api.record.model.ModelConstants.DATATYPE;

/**
 * @author Hugo
 * @since 7 Aug 2023
 */
//@Entity(useDiscriminator = false)
public class DatatypeLiteral<T> extends Literal<T> {

    @JsonProperty(DATATYPE)
    @JsonSerialize(using = DatatypeSerializer.class)
    @Property(DATATYPE)
    protected Datatype datatype;

    public DatatypeLiteral() {}

    public DatatypeLiteral(T value, Datatype datatype) {
        this.value    = value;
        this.datatype = datatype;
    }

    public Datatype getDatatype() { return datatype; }

    public String toString() { return ('"' + this.value.toString() + "\"^^<"
                                     + this.datatype + ">"); }
}