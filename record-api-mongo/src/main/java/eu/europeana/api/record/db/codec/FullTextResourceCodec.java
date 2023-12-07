/**
 * 
 */
package eu.europeana.api.record.db.codec;

import org.bson.BsonReader;
import org.bson.BsonReaderMark;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.configuration.CodecRegistry;

import com.mongodb.DBObjectCodecProvider;

import dev.morphia.Datastore;
import dev.morphia.mapping.Mapper;
import dev.morphia.mapping.MappingException;
import dev.morphia.mapping.codec.ObjectCodec;
import eu.europeana.api.edm.EDM;
import eu.europeana.api.edm.RDF;
import eu.europeana.api.record.db.MongoServiceManager;
import eu.europeana.api.record.model.ModelConstants;
import eu.europeana.api.record.model.data.DataValue;
import eu.europeana.api.record.model.data.DatatypeLiteral;
import eu.europeana.api.record.model.data.LanguageLiteral;
import eu.europeana.api.record.model.data.Literal;

import static eu.europeana.api.record.model.ModelConstants.*;

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
            bsonWriter.writeString(ModelConstants.additionalType, EDM.FullTextResource);
        }
    }

    public Boolean decode(final BsonReader reader
                          , final DecoderContext decoderContext) {
        return true;
    }
}
