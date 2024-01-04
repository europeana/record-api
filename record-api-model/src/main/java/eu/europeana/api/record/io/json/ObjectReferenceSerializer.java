package eu.europeana.api.record.io.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import eu.europeana.api.record.model.ModelConstants;
import eu.europeana.api.record.model.data.ObjectReference;

import java.io.IOException;
import java.util.Stack;

/**
 * @author Hugo
 * @since 12 Sep 2023
 */
public class ObjectReferenceSerializer<T extends ObjectReference> 
       extends JsonSerializer<T> {

    public static final ObjectReferenceSerializer INSTANCE = new ObjectReferenceSerializer();

    @Override
    public void serialize(T ref, JsonGenerator jgen,
                          SerializerProvider serializers) throws IOException
    {
        String id = ref.getID();
        if ( ref.isDereferenced() ) {
            Stack<String> stack = JsonLdWriter.stack.get();
            if ( !stack.contains(id) ) {
                stack.push(id);
                jgen.writeObject(ref.getDereferencedObject());
                stack.pop();
                return;
            }
        }
        jgen.writeStartObject();
        jgen.writeStringField(ModelConstants.id, id);
        jgen.writeEndObject();
    }

    public void serializeWithType(T ref, JsonGenerator jgen
                                , SerializerProvider serializers
                                , TypeSerializer typeSer) throws IOException {
        serialize(ref, jgen, serializers);
    }
}
