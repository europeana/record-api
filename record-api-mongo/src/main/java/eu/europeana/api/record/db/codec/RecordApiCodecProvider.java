/**
 * 
 */
package eu.europeana.api.record.db.codec;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.BsonType;
import org.bson.codecs.Codec;
import org.bson.codecs.CustomTypeClassMap;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PropertyCodecProvider;
import org.bson.codecs.pojo.PropertyCodecRegistry;
import org.bson.codecs.pojo.TypeWithTypeParameters;

import dev.morphia.Datastore;
import dev.morphia.internal.DatastoreHolder;
import eu.europeana.api.record.model.data.DataValue;
import eu.europeana.api.record.model.data.DatatypeLiteral;
import eu.europeana.api.record.model.data.LanguageLiteral;
import eu.europeana.api.record.model.data.Literal;

/**
 * @author Hugo
 * @since 7 Sep 2023
 */
public class RecordApiCodecProvider implements CodecProvider, PropertyCodecProvider {

    private static final CustomTypeClassMap bsonTypeClassMap = new CustomTypeClassMap();

    private Map<Class,Codec> codecs;

    public RecordApiCodecProvider() {
        codecs = new HashMap();
        codecs.put(DataValue.class      , new DataValueCodec(this));
        codecs.put(Literal.class        , new LiteralCodec(this));
        codecs.put(LanguageLiteral.class, new LanguageLiteralCodec(this));
        codecs.put(DatatypeLiteral.class, new DatatypeLiteralCodec(this));
        codecs.put(LanguageLiteral.class, new LanguageLiteralCodec(this));
        addCodecs(EdmTypeCodec.INSTANCE, DatatypeCodec.INSTANCE
                , LanguageMapCodecProvider.INSTANCE
                , LanguageMapArrayCodecProvider.INSTANCE);
    }

    public void addCodecs(Codec<?>... codecs) {
        for ( Codec<?> codec : codecs ) {
            this.codecs.put(codec.getEncoderClass(), codec);
        }
    }
    

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        return codecs.get(clazz);
    }

    //@Override
    public <T> Codec<T> get(Class<T> clazz, List<Type> typeArguments
                          , CodecRegistry registry) {
        return codecs.get(clazz);
    }

    @Override
    public <T> Codec<T> get(TypeWithTypeParameters<T> type,
                            PropertyCodecRegistry registry) {
        Codec<T> codec = codecs.get(type.getType());
        return ( codec == null ? null 
                               : ((AbsDataValueCodec)codec).parameterize(type, registry) );
    }

    protected <T> Codec<T> getCodec(Class<T> clazz) {
        Codec<T> codec = codecs.get(clazz);
        return ( codec != null ? codec 
                               : getDatastore().getCodecRegistry().get(clazz) );
    }

    protected Class<?> getClassForBson(BsonType bsonType) {
        return bsonTypeClassMap.get(bsonType);
    }

    protected Datastore getDatastore() {
        return DatastoreHolder.holder.get();
    }

    protected boolean supportsClass(Class<?> clazz) {
        return bsonTypeClassMap.containsValue(clazz);
    }
}
