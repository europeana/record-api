/**
 * 
 */
package eu.europeana.api.record.db.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import eu.europeana.api.record.model.data.EdmType;

/**
 * @author Hugo
 * @since 27 Oct 2023

 * NOT BEING USED
*/
public class EdmTypeCodec implements Codec<EdmType> {

    public static EdmTypeCodec INSTANCE = new EdmTypeCodec();

    public EdmTypeCodec() {}

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
