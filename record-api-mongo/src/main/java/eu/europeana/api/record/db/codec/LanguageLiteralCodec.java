/**
 * 
 */
package eu.europeana.api.record.db.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import eu.europeana.api.record.model.data.LanguageLiteral;

/**
 * @author Hugo
 * @since 13 Sep 2023
 */
public class LanguageLiteralCodec extends AbsDataValueCodec<LanguageLiteral> {

    /**
     * @param property
     */
    public LanguageLiteralCodec(RecordApiCodecProvider provider) {
        super(provider);
    }

    public Class<LanguageLiteral> getEncoderClass() { return LanguageLiteral.class; }

    public void encode(BsonWriter writer, LanguageLiteral literal,
                       EncoderContext ctxt) {
        this.encodeLanguageLiteral(writer, literal, ctxt);
    }

    public LanguageLiteral decode(BsonReader reader, DecoderContext ctxt) {
        return this.decodeLanguageLiteral(reader, ctxt);
    }
}
