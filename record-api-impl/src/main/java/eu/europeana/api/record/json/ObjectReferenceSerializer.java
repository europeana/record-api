/**
 * 
 */
package eu.europeana.api.record.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import eu.europeana.api.record.model.data.ObjectReference;

import java.io.IOException;

/**
 * @author Hugo
 * @since 12 Sep 2023
 */
public class ObjectReferenceSerializer<T extends ObjectReference> 
       extends JsonSerializer<T>
{

    @Override
    public void serialize(T ref, JsonGenerator jgen,
                          SerializerProvider serializers) throws IOException
    {
        if (ref == null ) { return; }
        if ( ref.isDereferenced() )
        {
            jgen.writeObject(ref.getDereferencedObject());
            return;
        }
        jgen.writeStartObject();
        jgen.writeStringField("id", ref.getID());
        jgen.writeEndObject();
    }

    public void serializeWithType(T ref, JsonGenerator jgen
                                , SerializerProvider serializers
                                , TypeSerializer typeSer) throws IOException
    {
        serialize(ref, jgen, serializers);
    }
}
