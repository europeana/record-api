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

import eu.europeana.api.record.model.data.DatatypeLiteral;

/**
 * @author Hugo
 * @since 13 Sep 2023
 */
public class DatatypeLiteralCodec extends AbsDataValueCodec<DatatypeLiteral> {

    private Class<?> type;

    /**
     * @param property
     */
    public DatatypeLiteralCodec(RecordApiCodecProvider provider) {
        this(provider, null);
    }

    public DatatypeLiteralCodec(RecordApiCodecProvider provider, Class<?> type) {
        super(provider);
        this.type = type;
    }

    public Class<DatatypeLiteral> getEncoderClass() { return DatatypeLiteral.class; }

    public void encode(BsonWriter writer, DatatypeLiteral literal,
                       EncoderContext ctxt) {
        this.encodeDatatypeLiteral(writer, literal, ctxt);
    }

    public DatatypeLiteral decode(BsonReader reader, DecoderContext ctxt) {
        return this.decodeDatatypeLiteral(reader, ctxt, null);
    }

    @Override
    public Codec<DatatypeLiteral> parameterize(TypeWithTypeParameters<DatatypeLiteral> type,
                                               PropertyCodecRegistry registry) {
        List<? extends TypeWithTypeParameters<?>> list = type.getTypeParameters();
        if ( list.isEmpty() ) { return this; }

        Class<?> clazz = list.get(0).getType();
        if ( provider.supportsClass(clazz) ) { return this; }

        return new DatatypeLiteralCodec(provider, clazz);
    }
}
