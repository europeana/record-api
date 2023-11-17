/**
 * 
 */
package eu.europeana.api.record.io.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import eu.europeana.api.edm.EDM;

import java.io.IOException;

/**
 * @author Hugo
 * @since 13 Sep 2023
 */
public class FullTextResourceSerializer extends JsonSerializer<Boolean> {

    public boolean isEmpty(SerializerProvider provider, Boolean value) {
        return ( value == null || !value );
    }

    @Override
    public void serialize(Boolean value, JsonGenerator jgen,
                          SerializerProvider serializers) throws IOException {
        jgen.writeString(EDM.FullTextResource);
    }

    public void serializeWithType(Boolean value, JsonGenerator jgen
                                , SerializerProvider serializers
                                , TypeSerializer typeSer) throws IOException {
        serialize(value, jgen, serializers);
    }
}
