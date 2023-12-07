/**
 * 
 */
package eu.europeana.api.record.db.codec;

import java.util.List;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.pojo.PropertyCodecRegistry;
import org.bson.codecs.pojo.TypeWithTypeParameters;

import eu.europeana.api.record.model.data.DataValue;
import eu.europeana.api.record.model.data.Literal;

/**
 * @author Hugo
 * @since 13 Sep 2023
 */
public class LiteralCodec extends AbsDataValueCodec<Literal> {

    private Class<?> type;

    /**
     * @param property
     */
    public LiteralCodec(RecordApiCodecProvider provider) {
        this(provider, null);
    }

    public LiteralCodec(RecordApiCodecProvider provider, Class<?> type) {
        super(provider);
        this.type = type;
    }

    public Class<Literal> getEncoderClass() { return Literal.class; }

    public void encode(BsonWriter writer, Literal literal,
                       EncoderContext ctxt) {
        this.encodeLiteral(writer, literal, ctxt);
    }

    public Literal decode(BsonReader reader, DecoderContext ctxt) {
        return this.decodeLiteral(reader, ctxt, type);
    }

    @Override
    public Codec<Literal> parameterize(TypeWithTypeParameters<Literal> type,
                                       PropertyCodecRegistry registry) {
        List<? extends TypeWithTypeParameters<?>> list = type.getTypeParameters();
        if ( list.isEmpty() ) { return this; }

        Class<?> clazz = list.get(0).getType();
        if ( provider.supportsClass(clazz) ) { return this; }

        return new LiteralCodec(provider, clazz);
    }
}
