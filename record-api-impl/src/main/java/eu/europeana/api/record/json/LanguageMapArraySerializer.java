/**
 *
 */
package eu.europeana.api.record.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import eu.europeana.api.record.model.internal.LanguageMapArray;

import java.io.IOException;
import static eu.europeana.api.record.vocabulary.RecordFields.NONE;

/**
 * @author Hugo
 * @since 12 Sep 2023
 */
public class LanguageMapArraySerializer
        extends LanguageMapSerializer<LanguageMapArray> {

    @Override
    public void serialize(LanguageMapArray langMap, JsonGenerator jgen,
                          SerializerProvider serializers) throws IOException {
        jgen.writeStartObject();
        for (String lang : langMap.getLanguages()) {
            jgen.writeFieldName(lang);
            writeArray(langMap.getValues(lang), jgen);
        }
        if (langMap.hasNonLanguageValues()) {
            jgen.writeFieldName(NONE);
            writeArray(langMap.getNonLanguageValues(), jgen);
        }

        jgen.writeEndObject();
    }
}
