package eu.europeana.api.record.codec;

import eu.europeana.api.record.datatypes.Literal;
import eu.europeana.api.record.impl.LiteralImpl;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiteralMapCodec implements Codec <Map<String, List<Literal<String>>>> {

//    @Override
//    public void encode(final BsonWriter writer, final Literal<String> value, final EncoderContext encoderContext) {
//        writer.writeString(value.getValue());
//    }
//
//    @Override
//    public void encode(BsonWriter bsonWriter, Literal literal, EncoderContext encoderContext) {
//        bsonWriter.write(String.valueOf(literal.getValue()));
//
//    }

    @Override
    public void encode(BsonWriter bsonWriter, Map<String, List<Literal<String>>> stringLiteralMap, EncoderContext encoderContext) {

        for (Map.Entry<String, List<Literal<String>>> entry : stringLiteralMap.entrySet()) {
            System.out.println(entry.getKey() + "/" + entry.getValue());
            bsonWriter.writeStartArray(entry.getKey());
            entry.getValue().stream().forEach(value -> {
                bsonWriter.writeString(value.getValue());
            });
            bsonWriter.writeEndArray();

        }


    }

    @Override
    public Class<Map<String, List<Literal<String>>>> getEncoderClass() {
        Map<String, List<Literal<String>>> map = new HashMap<>();
        return (Class<Map<String, List<Literal<String>>>>) map.getClass();
    }

    @Override
    public Map<String, List<Literal<String>>> decode(final BsonReader reader, final DecoderContext decoderContext) {
        Map<String, List<Literal<String>>> resultMap = new HashMap<>();
        // TODO yet to implment
        return resultMap;
    }

}
