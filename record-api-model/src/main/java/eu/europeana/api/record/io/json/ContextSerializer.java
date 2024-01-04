package eu.europeana.api.record.io.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author Hugo
 * @since 12 Sep 2023
 */
public class ContextSerializer extends JsonSerializer<Context> {

    public static final ContextSerializer INSTANCE = new ContextSerializer();

    @Override
    public void serialize(Context context, JsonGenerator jgen,
                          SerializerProvider serializers) throws IOException {
        jgen.writeStartArray();
        jgen.writeString(context.getURI());
        jgen.writeStartObject();
        jgen.writeStringField("@base", context.getBase());
        jgen.writeEndObject();
        jgen.writeEndArray();
    }
}
