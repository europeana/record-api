
package eu.europeana.api.record.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import eu.europeana.api.record.model.EDMClass;

import java.io.IOException;

/**
 * @author Hugo
 * @since 12 Sep 2023
 */
public class EDMClassReferenceSerializer<T extends EDMClass> 
       extends JsonSerializer<T>
{

    @Override
    public void serialize(T obj, JsonGenerator jgen,
                          SerializerProvider serializers) throws IOException
    {
        if (obj == null ) { return; }
        jgen.writeObject(obj.getID());
    }

    public void serializeWithType(T obj, JsonGenerator jgen
                                , SerializerProvider serializers
                                , TypeSerializer typeSer) throws IOException
    {
        serialize(obj, jgen, serializers);
    }
}
