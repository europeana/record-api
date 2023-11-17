/**
 * 
 */
package eu.europeana.api.record.io.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import eu.europeana.api.record.model.EDMClass;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.data.ObjectReference;

import java.io.IOException;
import java.util.Collection;

/**
 * @author Hugo
 * @since 12 Sep 2023
 */
public class CompactSerializer extends JsonSerializer<Object> {

    public static final CompactSerializer INSTANCE = new CompactSerializer();

    public boolean isEmpty(SerializerProvider provider, Object value) {
        if (value == null ) { return true; }
        if ( value instanceof Collection ) { return ((Collection)value).isEmpty(); }
        return false;
    }

    @Override
    public void serialize(Object obj, JsonGenerator jgen,
                          SerializerProvider provider) throws IOException
    {
        if ( obj instanceof ObjectReference ) {
            ObjectReference ref = (ObjectReference)obj;
//            if ( ref.isDereferenced() ) {
//                jgen.writeObject(ref.getDereferencedObject());
//                return;
//            }
            jgen.writeString(ref.getID());
            return;
        }
        
        if ( obj instanceof Literal ) {
            jgen.writeObject(((Literal)obj).getValue());
            return;
        }

        if ( obj instanceof Collection ) {
            jgen.writeStartArray();
            for ( Object o : (Collection)obj ) { serialize(o, jgen, provider); }
            jgen.writeEndArray();
            return;
        }

        if ( obj instanceof EDMClass ) {
            jgen.writeString(((EDMClass)obj).getID());
            return;
        }
    }

    public void serializeWithType(Object ref, JsonGenerator jgen
                                , SerializerProvider serializers
                                , TypeSerializer typeSer) throws IOException {
        serialize(ref, jgen, serializers);
    }
}
