package eu.europeana.api.record.io.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import eu.europeana.api.record.model.data.Datatype;

import java.io.IOException;

/**
 * @author Hugo
 * @since 13 Sep 2023
 */
public class DatatypeSerializer extends JsonSerializer<Datatype> {

    public boolean isEmpty(SerializerProvider provider, Datatype value) {
        return ( value == null );
    }

    @Override
    public void serialize(Datatype value, JsonGenerator jgen,
                          SerializerProvider serializers) throws IOException {
        jgen.writeString(value.getURI());
    }

    public void serializeWithType(Datatype value, JsonGenerator jgen
                                , SerializerProvider serializers
                                , TypeSerializer typeSer) throws IOException {
        serialize(value, jgen, serializers);
    }
}
