/**
 * 
 */
package eu.europeana.api.record.db.codec;

import dev.morphia.Datastore;
import dev.morphia.mapping.Mapper;
import eu.europeana.api.edm.RDF;
import eu.europeana.api.record.model.data.DataValue;
import eu.europeana.api.record.model.data.DatatypeLiteral;
import eu.europeana.api.record.model.data.LanguageLiteral;
import eu.europeana.api.record.model.data.Literal;
import org.bson.BsonReader;
import org.bson.BsonReaderMark;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import static eu.europeana.api.record.model.ModelConstants.*;

/**
 * @author Hugo
 * @since 1 Sep 2023
 */
public class DataValueCodec implements Codec<DataValue> {

    private Datastore datastore;

    public DataValueCodec(Datastore datastore) { this.datastore = datastore; }

    public void setDatastore(Datastore datastore) { this.datastore = datastore; }

    public Class<DataValue> getEncoderClass() { return DataValue.class; }

    public void encode(BsonWriter bsonWriter, DataValue dataValue
                     , EncoderContext encoderContext) {
        final Codec codec = datastore.getCodecRegistry().get(dataValue.getClass());
        codec.encode(bsonWriter, dataValue, encoderContext);
    }

    public DataValue decode(final BsonReader reader
                          , final DecoderContext decoderContext) {
        BsonType bsonType = reader.getCurrentBsonType();
        if (bsonType != BsonType.DOCUMENT) { return null; }

        Class<? extends DataValue> clazz = null;
        BsonReaderMark mark = reader.getMark();
        try     { clazz = decodeClass(reader); }
        finally { mark.reset();                }
        if ( clazz == null ) { return null; }

        return datastore.getCodecRegistry().get(clazz).decode(reader, decoderContext);
    }

    private Class<? extends DataValue> decodeClass(BsonReader reader) {
        Mapper mapper = datastore.getMapper();
        String dField = mapper.getConfig().discriminatorKey();
        reader.readStartDocument();
        boolean isValue = false;
        while ( reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = reader.readName();
            if ( name.equals(VALUE   ) ) { isValue = true; }
            else if ( name.equals(LANG    ) ) { return LanguageLiteral.class;                }
            else if ( name.equals(DATATYPE) ) { return DatatypeLiteral.class;                }
            else if ( name.equals(RDF.type) ) { return mapper.getClass(reader.readString()); }
            else if ( name.equals(dField  ) ) { return mapper.getClass(reader.readString()); }
            reader.skipValue();
        }
        if ( isValue ) { return Literal.class; }
        return null;
    }
}
