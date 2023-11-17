package eu.europeana.api.record.io.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import eu.europeana.api.record.model.internal.LanguageMapArray;

import java.io.IOException;

/**
 * @author Hugo
 * @since 12 Sep 2023
 */
public class LanguageMapArraySerializer 
       extends LanguageMapSerializer<LanguageMapArray> {

    public static final LanguageMapArraySerializer INSTANCE = new LanguageMapArraySerializer();

    @Override
    public void serialize(LanguageMapArray langMap, JsonGenerator jgen,
                          SerializerProvider serializers) throws IOException
    {
        jgen.writeStartObject();
        for ( String lang : langMap.getLanguages() )
        {
            jgen.writeFieldName(lang);
            writeArray(langMap.getValues(lang), jgen);
        }
        if ( langMap.hasNonLanguageValues() )
        {
            jgen.writeFieldName("@none");
            writeArray(langMap.getNonLanguageValues(), jgen);
        }
        
        jgen.writeEndObject();
    }
}
