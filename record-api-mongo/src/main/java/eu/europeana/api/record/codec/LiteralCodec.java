package eu.europeana.api.record.codec;

import eu.europeana.api.record.model.data.Literal;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class LiteralCodec  implements Codec<Literal> {

    @Override
    public void encode(BsonWriter bsonWriter, Literal literal, EncoderContext encoderContext) {
//        if (LanguageTaggedLiteral.class.isAssignableFrom(literal.getClass())) {
//            bsonWriter.writeString(String.valueOf(((LanguageTaggedLiteral<?>) literal).getLanguage()));
//        }
        bsonWriter.writeString(String.valueOf(literal.getValue()));
    }

    @Override
    public Class<Literal> getEncoderClass() {
        return Literal.class;
    }

    @Override
    public Literal<String> decode(final BsonReader reader, final DecoderContext decoderContext) {
        //return new LiteralImpl(reader.readString());
        return null;
    }

}