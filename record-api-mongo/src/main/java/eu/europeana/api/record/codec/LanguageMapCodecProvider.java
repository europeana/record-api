package eu.europeana.api.record.codec;

import eu.europeana.api.record.model.data.LanguageTaggedLiteral;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.internal.LanguageMap;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import static eu.europeana.api.record.vocabulary.RecordFields.NONE;

import java.util.List;

/**
 * @author Hugo
 * @since 1 Sep 2023
 */
public class LanguageMapCodecProvider<T extends LanguageMap> 
       implements CodecProvider, Codec<T> {

    public LanguageMapCodecProvider() {}

    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry)
    {
        if ( clazz == this.getEncoderClass() ) { return (Codec<T>)this; }
        return null;
    }

    public Class<T> getEncoderClass()
    {
        return (Class<T>)LanguageMap.class;
    }

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
            writeArray(langMap.getNonLanguageValues(), NONE, bsonWriter);
        }
        bsonWriter.writeEndDocument();
    }

    public T decode(final BsonReader reader, final DecoderContext decoderContext)
    {
        BsonType bsonType = reader.getCurrentBsonType();
        if (bsonType != BsonType.DOCUMENT) { return null; }

        LanguageMap langMap = new LanguageMap();

        reader.readStartDocument();
        while(reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String lang = reader.readName();
            if (lang.equals(NONE)) {
                loadFromArray(langMap, reader);
                continue;
            }
            langMap.add(new LanguageTaggedLiteral(reader.readString(), lang));
        }
        reader.readEndDocument();
        return (T)langMap;
    }

    protected void writeArray(List<? extends Literal<String>> literals, String label, BsonWriter bsonWriter)
    {
        bsonWriter.writeStartArray(label);
        for ( Literal<String> literal : literals )
        {
            bsonWriter.writeString(literal.getValue());
        }
        bsonWriter.writeEndArray();
    }

    protected void loadFromArray(LanguageMap langMap, BsonReader reader)
    {
        if (reader.getCurrentBsonType() != BsonType.ARRAY) { return; }

        reader.readStartArray();

        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            langMap.add(new Literal<>(reader.readString()));
        }

        reader.readEndArray();
    }
}
