package eu.europeana.api.record.codec;

import eu.europeana.api.record.datatypes.ObjectReference;
import eu.europeana.api.record.impl.ObjectReferenceImpl;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public class ObjectReferenceCodec implements Codec<ObjectReference> {

    @Override
    public ObjectReference decode(BsonReader bsonReader, DecoderContext decoderContext) {
        return new ObjectReferenceImpl(bsonReader.readString());
    }

    // TODO For now writing as a String only the id's.
    //  We still need to figure out how we will store the referenced object for the id
    @Override
    public void encode(BsonWriter bsonWriter, ObjectReference objectReference, EncoderContext encoderContext) {
        bsonWriter.writeString(objectReference.getId());
    }

    @Override
    public Class<ObjectReference> getEncoderClass() {
        return ObjectReference.class;
    }
}
