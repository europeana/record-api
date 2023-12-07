/**
 * 
 */
package eu.europeana.api.record.db.codec;

import org.apache.commons.lang3.StringUtils;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

import eu.europeana.api.record.model.ModelConstants;
import eu.europeana.api.record.model.data.Datatype;
import eu.europeana.api.record.model.data.DatatypeLiteral;
import eu.europeana.api.record.model.data.DatatypeUtils;
import eu.europeana.api.record.model.data.EdmType;

/**
 * @author Hugo
 * @since 27 Oct 2023

 * NOT BEING USED
*/
public class DatatypeCodec implements Codec<Datatype> {

    public static DatatypeCodec INSTANCE = new DatatypeCodec();

    public DatatypeCodec() {}

    @Override
    public Class<Datatype> getEncoderClass() { return Datatype.class; }

    @Override
    public Datatype decode(final BsonReader reader
                         , final DecoderContext decoderContext) {
        return DatatypeUtils.resolveDatatype(reader.readString());
    }

    @Override
    public void encode(final BsonWriter writer, final Datatype dt
                     , final EncoderContext encoderContext) {
        String abbrv = dt.getQName();
        writer.writeString(abbrv == null ?  dt.getURI() : abbrv);
    }
}
