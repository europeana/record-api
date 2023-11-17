/**
 * 
 */
package eu.europeana.api.record.db.codec;

import eu.europeana.api.record.model.data.EdmType;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * @author Hugo
 * @since 27 Oct 2023

 * NOT BEING USED
*/
public class EdmTypeCodec implements Codec<EdmType>, CodecProvider {

    public EdmTypeCodec() {}

    @Override
    public Codec<EdmType> get(Class clazz, CodecRegistry registry) {
        return ( clazz == EdmType.class ? this : null );
    }

    @Override
    public Class<EdmType> getEncoderClass() { return EdmType.class; }

    @Override
    public EdmType decode(final BsonReader reader, final DecoderContext decoderContext) {
        return EdmType.decode(reader.readString());
    }

    @Override
    public void encode(final BsonWriter writer, final EdmType value
                     , final EncoderContext encoderContext) {
        writer.writeString(value.toString());
    }
}
