/**
 * 
 */
package eu.europeana.api.record.db.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import eu.europeana.api.record.model.data.DataValue;
import eu.europeana.api.record.model.data.Literal;

/**
 * @author Hugo
 * @since 13 Sep 2023
 */
public class DataValueCodec extends AbsDataValueCodec<DataValue> {

    /**
     * @param property
     */
    public DataValueCodec(RecordApiCodecProvider provider) {
        super(provider);
    }

    public Class<DataValue> getEncoderClass() { return DataValue.class; }

    public void encode(BsonWriter writer, DataValue value,
                       EncoderContext ctxt) {
        this.encodeDataValue(writer, value, ctxt);
    }

    public DataValue decode(BsonReader reader, DecoderContext ctxt) {
        return this.decodeDataValue(reader, ctxt);
    }
}
