package eu.europeana.api.record.io.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.internal.LanguageMap;

import java.io.IOException;
import java.util.List;

/**
 * @author Hugo
 * @since 12 Sep 2023
 */
public class LanguageMapSerializer<T extends LanguageMap> 
       extends JsonSerializer<T>
{
    public static final LanguageMapSerializer INSTANCE = new LanguageMapSerializer();

    @Override
    public void serialize(T langMap, JsonGenerator jgen,
                          SerializerProvider serializers) throws IOException
    {
        jgen.writeStartObject();
        for ( String lang : langMap.getLanguages() )
        {
            jgen.writeStringField(lang, langMap.getValue(lang).getValue());
        }
        if ( langMap.hasNonLanguageValues() )
        {
            jgen.writeFieldName("@none");
            writeArray(langMap.getNonLanguageValues(), jgen);
        }
        
        jgen.writeEndObject();
    }

    protected void writeArray(List<? extends Literal<String>> literals
                          , JsonGenerator jgen) throws IOException
    {
        jgen.writeStartArray();
        for ( Literal<String> literal : literals )
        {
            jgen.writeString(literal.getValue());
        }
        jgen.writeEndArray();
    }
}
