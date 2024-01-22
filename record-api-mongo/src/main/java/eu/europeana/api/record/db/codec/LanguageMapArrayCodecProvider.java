/**
 * 
 */
package eu.europeana.api.record.db.codec;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import eu.europeana.api.record.model.data.LanguageLiteral;
import eu.europeana.api.record.model.internal.LanguageMap;
import eu.europeana.api.record.model.internal.LanguageMapArray;

/**
 * @author Hugo
 * @since 1 Sep 2023
 */
public class LanguageMapArrayCodecProvider 
       extends LanguageMapCodecProvider<LanguageMapArray>  {

    public static LanguageMapArrayCodecProvider INSTANCE 
        = new LanguageMapArrayCodecProvider();

    public LanguageMapArrayCodecProvider() {}

    public Class<LanguageMapArray> getEncoderClass() {
        return LanguageMapArray.class;
    }

    public void encode(BsonWriter bsonWriter, LanguageMapArray langMap
                     , EncoderContext encoderContext) {
        if ( langMap == null ) { return; }

        bsonWriter.writeStartDocument();
        for ( String lang : langMap.getLanguages() )
        {
            writeArray(langMap.getValues(lang), lang, bsonWriter);
        }
        if ( langMap.hasNonLanguageValues() )
        {
            writeArray(langMap.getNonLanguageValues(), "@none", bsonWriter);
        }
        bsonWriter.writeEndDocument();
    }

    public LanguageMapArray decode(BsonReader reader
                                 , DecoderContext decoderContext) {

        BsonType bsonType = reader.getCurrentBsonType();
        if (bsonType != BsonType.DOCUMENT) { return null; }

        LanguageMapArray langMap = new LanguageMapArray();

        reader.readStartDocument();
        while(reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String lang = reader.readName();
            if (lang.equals("@none")) {
                loadFromArray(langMap, reader);
                continue;
            }
            loadFromArray(lang, langMap, reader);
        }
        reader.readEndDocument();
        return langMap;
    }

    protected void loadFromArray(String lang, LanguageMap langMap
                               , BsonReader reader) {

        if (reader.getCurrentBsonType() != BsonType.ARRAY) { return; }

        reader.readStartArray();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            langMap.add(new LanguageLiteral(reader.readString(), lang));
        }
        reader.readEndArray();
    }
}
