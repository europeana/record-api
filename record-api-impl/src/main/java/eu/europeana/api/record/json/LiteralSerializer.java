package eu.europeana.api.record.json;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import eu.europeana.api.record.model.data.Literal;

/**
 * @author Hugo
 * @since 13 Sep 2023
 */
public class LiteralSerializer extends JsonSerializer<Literal<?>> {

    @Override
    public void serialize(Literal<?> literal, JsonGenerator jgen,
                          SerializerProvider serializers) throws IOException
    {
        if (literal == null ) { return; }
        jgen.writeObject(literal.getValue());
    }

    public void serializeWithType(Literal<?> literal, JsonGenerator jgen
            , SerializerProvider serializers
            , TypeSerializer typeSer) throws IOException
    {
        serialize(literal, jgen, serializers);
    }
}