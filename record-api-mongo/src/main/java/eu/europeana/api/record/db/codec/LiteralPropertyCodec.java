/**
 * 
 */
package eu.europeana.api.record.db.codec;

import dev.morphia.mapping.Mapper;
import dev.morphia.mapping.codec.BaseReferenceCodec;
import dev.morphia.mapping.codec.pojo.PropertyModel;
import eu.europeana.api.record.model.data.Literal;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

/**
 * @author Hugo
 * @since 13 Sep 2023
 */
public class LiteralPropertyCodec extends BaseReferenceCodec<Literal> 
{

    /**
     * @param property
     */
    public LiteralPropertyCodec(PropertyModel property) {
        super(property);
    }

    public LiteralPropertyCodec(Mapper mapper, PropertyModel property) {
        super(property);
    }

    public Class<Literal> getEncoderClass() { return Literal.class; }

    public void encode(BsonWriter writer, Literal literal,
                       EncoderContext encoderContext) {

        if ( literal == null ) { return; }

        Object value = literal.getValue();
        final Codec codec = getDatastore().getCodecRegistry().get(value.getClass());
        codec.encode(writer, value, encoderContext);
    }

    public Literal decode(BsonReader reader, DecoderContext decoderContext) {
        return null;
    }

}
