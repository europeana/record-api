/**
 * 
 */
package eu.europeana.api.record.db.codec;

import eu.europeana.api.edm.EDM;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import static eu.europeana.api.record.model.ModelConstants.ADDITIONALTYPE;


/**
 * @author Hugo
 * @since 1 Sep 2023
 * 
 * NOT BEING USED
 */
public class FullTextResourceCodec implements Codec<Boolean> {

    public FullTextResourceCodec() { }

    public Class<Boolean> getEncoderClass() { return Boolean.class; }

    public void encode(BsonWriter bsonWriter, Boolean dataValue
                     , EncoderContext encoderContext) {
        if ( dataValue != null && dataValue ) {
            bsonWriter.writeString(ADDITIONALTYPE, EDM.FullTextResource);
        }
    }

    public Boolean decode(final BsonReader reader
                          , final DecoderContext decoderContext) {
        return true;
    }
}
