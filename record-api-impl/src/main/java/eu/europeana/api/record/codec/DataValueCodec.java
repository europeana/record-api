package eu.europeana.api.record.codec;

import eu.europeana.api.record.datatypes.DataValue;
import eu.europeana.api.record.impl.LanguageTaggedLiteralImpl;
import eu.europeana.api.record.impl.LiteralImpl;
import eu.europeana.api.record.impl.ObjectReferenceImpl;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import static eu.europeana.api.record.vocabulary.RecordFields.ID;
import static eu.europeana.api.record.vocabulary.RecordFields.VALUE;
import static eu.europeana.api.record.vocabulary.RecordFields.LANGUAGE;

public class DataValueCodec implements Codec<DataValue> {

    @Override
    public DataValue decode(BsonReader bsonReader, DecoderContext decoderContext) {
        // reading from mongo works without any decoder implementation.
        // As the DataValues Encoder class is already defined and saved well in Mongo
        return null;
    }

    @Override
    public void encode(BsonWriter bsonWriter, DataValue dataValue, EncoderContext encoderContext) {

        if (LanguageTaggedLiteralImpl.class.isAssignableFrom(dataValue.getClass())) {

            LanguageTaggedLiteralImpl<String> value = (LanguageTaggedLiteralImpl<String>) dataValue;

            bsonWriter.writeStartDocument();
            bsonWriter.writeName(VALUE);
            bsonWriter.writeString(value.getValue());

            bsonWriter.writeName(LANGUAGE);
            bsonWriter.writeString(value.getLanguage());

            bsonWriter.writeEndDocument();
        }

        if (LiteralImpl.class.isAssignableFrom(dataValue.getClass())) {
            LiteralImpl<String> literal = (LiteralImpl<String>) dataValue;
            bsonWriter.writeStartDocument();
            bsonWriter.writeName(VALUE);
            bsonWriter.writeString(literal.getValue());
            bsonWriter.writeEndDocument();

        }

        if (ObjectReferenceImpl.class.isAssignableFrom(dataValue.getClass())) {
            ObjectReferenceImpl objectReference = (ObjectReferenceImpl) dataValue;
            bsonWriter.writeStartDocument();
            bsonWriter.writeName(ID);
            bsonWriter.writeString(objectReference.getId());

            if (objectReference.getReferencedObject() != null) {
                Object referencedObject = objectReference.getReferencedObject();

                // TODO yet to do
               // bsonWriter.writeName("referencedObject");
              //  bsonWriter.writeString(objectReference.getReferencedObject());

            }
            bsonWriter.writeEndDocument();
        }
    }

    @Override
    public Class<DataValue> getEncoderClass() {
        return DataValue.class;
    }



}
