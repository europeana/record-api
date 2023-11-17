/**
 * 
 */
package eu.europeana.api.record.io.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import eu.europeana.api.record.model.EDMClass;

import java.io.IOException;
import java.util.Collection;

/**
 * @author Hugo
 * @since 12 Sep 2023
 */
public class EDMClassReferenceSerializer extends JsonSerializer<Object> {

    public boolean isEmpty(SerializerProvider provider, Object value) {
        if (value == null ) { return true; }
        if ( value instanceof Collection ) { return ((Collection)value).isEmpty(); }
        return false;
    }

    @Override
    public void serialize(Object obj, JsonGenerator jgen,
                          SerializerProvider provider) throws IOException {
        if ( obj instanceof EDMClass ) { 
            jgen.writeString(((EDMClass)obj).getID());
            return;
        }

        if ( obj instanceof Collection ) {
            jgen.writeStartArray();
            for ( Object o : (Collection)obj ) { serialize(o, jgen, provider); }
            jgen.writeEndArray();
        }
    }

    public void serializeWithType(Object obj, JsonGenerator jgen
                                , SerializerProvider serializers
                                , TypeSerializer typeSer) throws IOException {
        serialize(obj, jgen, serializers);
    }
}
