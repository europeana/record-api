/**
 * 
 */
package eu.europeana.api.record.db.codec;

import org.bson.BsonReader;
import org.bson.BsonReaderMark;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.Parameterizable;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PropertyCodecRegistry;
import org.bson.codecs.pojo.TypeWithTypeParameters;

import eu.europeana.api.edm.RDF;
import eu.europeana.api.record.model.data.DataValue;
import eu.europeana.api.record.model.data.Datatype;
import eu.europeana.api.record.model.data.DatatypeLiteral;
import eu.europeana.api.record.model.data.LanguageLiteral;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.data.LocalReference;
import eu.europeana.api.record.model.data.SharedReference;

import java.lang.reflect.Type;
import java.util.List;

import static eu.europeana.api.record.model.ModelConstants.*;

/**
 * @author Hugo
 * @since 1 Sep 2023
 */
public abstract class AbsDataValueCodec<T extends DataValue> implements Codec<T>, Parameterizable {

    protected RecordApiCodecProvider provider;

    public AbsDataValueCodec(RecordApiCodecProvider provider) { 
        this.provider = provider; 
    }

    public Codec<?> parameterize(CodecRegistry codecRegistry, List<Type> types) {
        return this;
    }

    public Codec<T> parameterize(TypeWithTypeParameters<T> type,
                                 PropertyCodecRegistry registry) {
        return this;
    }

    /*
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
    */

    /*
     * Encoding methods
     */

    protected void encodeDataValue(BsonWriter writer, DataValue dv
                                 , EncoderContext ctxt) {
        Codec codec = provider.getCodec(dv.getClass());
        codec.encode(writer, dv, ctxt);
    }

    protected void encodeLiteral(BsonWriter writer, Literal literal
                               , EncoderContext ctxt) {
        Class clazz = literal.getClass();
        if ( Literal.class == clazz ) {
//            writer.writeStartDocument();
//            writer.writeName(value);
            encodeValue(writer, literal.getValue(), ctxt);
//            writer.writeEndDocument();
            return;
        }
        Codec codec = provider.getCodec(clazz);
        codec.encode(writer, literal, ctxt);
    }

    protected void encodeLanguageLiteral(BsonWriter writer
                                       , LanguageLiteral literal
                                       , EncoderContext ctxt) {
        writer.writeStartDocument();
        writer.writeString(lang , literal.getLanguage());
        writer.writeString(value, literal.getValue());
        writer.writeEndDocument();
    }
    
    protected void encodeDatatypeLiteral(BsonWriter writer
                                       , DatatypeLiteral literal
                                       , EncoderContext ctxt) {
        writer.writeStartDocument();
        writer.writeName(datatype);
        encodeDatatype(writer, literal.getDatatype(), ctxt);
        writer.writeName(value);
        encodeValue(writer, literal.getValue(), ctxt);
        writer.writeEndDocument();
    }

    protected void encodeValue(BsonWriter writer, Object obj
                             , EncoderContext ctxt) {
        Codec codec = provider.getCodec(obj.getClass());
        codec.encode(writer, obj, ctxt);
    }

    protected void encodeDatatype(BsonWriter writer, Datatype dt
                                , EncoderContext ctxt) {
        provider.getCodec(Datatype.class).encode(writer, dt, null);
    }


    /*
     * Decoding methods
     */

    protected DataValue decodeDataValue(BsonReader reader, DecoderContext ctxt) {
        Class clazz = decodeClass(reader);
        return (DataValue)provider.getCodec(clazz).decode(reader, ctxt);
    }

    protected Literal decodeLiteral(BsonReader reader, DecoderContext ctxt
                                  , Class<?> gtype) {
        Object   obj      = null;
        String   language = null;
        Datatype dt       = null;

        BsonType btype = reader.getCurrentBsonType();
        if ( btype != BsonType.DOCUMENT ) {
            obj = decodeValue(reader, ctxt, gtype);
            return (obj == null ? null : new Literal(obj) );
        }

        reader.readStartDocument();
        while ( reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = reader.readName();
            if ( name.equals(value ) ) {
                obj = decodeValue(reader, ctxt, gtype);
            }
            else if ( name.equals(lang) ) {
                language = reader.readString();
            }
            else if ( name.equals(datatype) ) {
                dt = decodeDatatype(reader, ctxt);
            }
            else {
                System.err.println("unknown field: " + name);
            }
        }
        reader.readEndDocument();
        if ( language != null ) { return new LanguageLiteral((String)obj, language); }
        if ( dt       != null ) { return new DatatypeLiteral(obj, dt); }
        return (obj == null ? null : new Literal(obj) );
    }

    protected LanguageLiteral decodeLanguageLiteral(BsonReader reader
                                                  , DecoderContext ctxt) {
        String str      = null;
        String language = null;
        reader.readStartDocument();
        while ( reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = reader.readName();
            if ( name.equals(value) ) {
                str = reader.readString();
            }
            else if ( name.equals(lang) ) {
                language = reader.readString();
            }
        }
        reader.readEndDocument();

        return new LanguageLiteral(str, language);
    }

    protected DatatypeLiteral decodeDatatypeLiteral(BsonReader reader
                                                  , DecoderContext ctxt
                                                  , Class<?> gtype) {
        Object   obj  = null;
        Datatype dt   = null;
        reader.readStartDocument();
        while ( reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = reader.readName();
            if ( name.equals(value ) ) {
                obj = decodeValue(reader, ctxt,  gtype);
            }
            else if ( name.equals(datatype) ) {
                dt = decodeDatatype(reader, ctxt);
            }
        }
        reader.readEndDocument();
        return new DatatypeLiteral(obj, dt);
    }

    protected Object decodeValue(BsonReader reader, DecoderContext ctxt
                               , Class<?> type) {
        if ( type == null ) { type = provider.getClassForBson(reader.getCurrentBsonType()); }
        return provider.getCodec(type).decode(reader, ctxt);
    }

    protected Datatype decodeDatatype(BsonReader reader, DecoderContext ctxt) {
        return (Datatype)provider.getCodec(Datatype.class).decode(reader, ctxt);
    }

    protected Class<? extends DataValue> decodeClass(BsonReader reader) {
        BsonReaderMark mark = reader.getMark();
        try     { return inspectDataForClass(reader); }
        finally { mark.reset();               }
    }

    protected Class<? extends DataValue> inspectDataForClass(BsonReader reader) {

        BsonType btype = reader.getCurrentBsonType();
        if ( btype != BsonType.DOCUMENT ) { return Literal.class; }

        reader.readStartDocument();
        boolean isValue = false;
        while ( reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            String name = reader.readName();
            if ( name.equals(value   ) ) { isValue = true; }
            else if ( name.equals(lang    ) ) { return LanguageLiteral.class;                }
            else if ( name.equals(datatype) ) { return DatatypeLiteral.class;                }
            else if ( name.equals(RDF.type) ) { 
                String type = reader.readString();
                if ( type.equals("Shared") ) { return SharedReference.class; }
                if ( type.equals("Local") ) { return LocalReference.class; }
                //return mapper.getClass(reader.readString()); 
            }
            reader.skipValue();
        }
        if ( isValue ) { return Literal.class; }
        return null;
    }
}
