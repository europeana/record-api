/**
 * 
 */
package eu.europeana.api.record.db.codec;

import java.util.List;

import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import eu.europeana.api.record.model.data.LanguageLiteral;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.internal.LanguageMap;

/**
 * @author Hugo
 * @since 1 Sep 2023
 */
public class LanguageMapCodecProvider<T extends LanguageMap> implements Codec<T> {

    public static LanguageMapCodecProvider INSTANCE 
        = new LanguageMapCodecProvider();

    public LanguageMapCodecProvider() {}

    @Override
    public Class<T> getEncoderClass() { return (Class<T>)LanguageMap.class; }

    @Override
    public void encode(BsonWriter bsonWriter, LanguageMap langMap, EncoderContext encoderContext)
    {
        if ( langMap == null ) { return; }

        bsonWriter.writeStartDocument();
        for ( String lang : langMap.getLanguages() )
        {
            bsonWriter.writeString(lang, langMap.getValue(lang).getValue());
        }
        if ( langMap.hasNonLanguageValues() )
        {
            writeArray(langMap.getNonLanguageValues(), "@none", bsonWriter);
        }
        bsonWriter.writeEndDocument();
    }

    @Override
    public T decode(BsonReader reader, DecoderContext decoderContext) {

        BsonType bsonType = reader.getCurrentBsonType();
        if (bsonType != BsonType.DOCUMENT) { return null; }

        LanguageMap langMap = new LanguageMap();

        reader.readStartDocument();
        while(reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String lang = reader.readName();
            if (lang.equals("@none")) {
                loadFromArray(langMap, reader);
                continue;
            }
            langMap.add(new LanguageLiteral(reader.readString(), lang));
        }
        reader.readEndDocument();
        return (T)langMap;
    }

    protected void writeArray(List<? extends Literal<String>> literals
                            , String label, BsonWriter bsonWriter) {
        bsonWriter.writeStartArray(label);
        for ( Literal<String> literal : literals )
        {
            bsonWriter.writeString(literal.getValue());
        }
        bsonWriter.writeEndArray();
    }

    protected void loadFromArray(LanguageMap langMap, BsonReader reader) {

        if (reader.getCurrentBsonType() != BsonType.ARRAY) { return; }

        reader.readStartArray();

        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            langMap.add(new Literal(reader.readString()));
        }

        reader.readEndArray();
    }
}
