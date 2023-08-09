package eu.europeana.api.record.codec;

import eu.europeana.api.record.datatypes.Literal;
import eu.europeana.api.record.impl.LiteralImpl;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class LiteralCodec  implements Codec<Literal> {

//    @Override
//    public void encode(final BsonWriter writer, final Literal<String> value, final EncoderContext encoderContext) {
//        writer.writeString(value.getValue());
//    }

    @Override
    public void encode(BsonWriter bsonWriter, Literal literal, EncoderContext encoderContext) {
        bsonWriter.writeString(String.valueOf(literal.getValue()));

    }

    @Override
    public Class<Literal> getEncoderClass() {
        return Literal.class;
    }

    @Override
    public Literal<String> decode(final BsonReader reader, final DecoderContext decoderContext) {
        Literal<String>  literal = new LiteralImpl<>();
        literal.setValue(reader.readString());
        return literal;
    }

}