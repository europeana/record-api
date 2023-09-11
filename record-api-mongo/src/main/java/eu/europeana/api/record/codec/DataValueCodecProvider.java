/**
 * 
 */
package eu.europeana.api.record.codec;

import dev.morphia.Datastore;
import eu.europeana.api.record.model.data.*;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * @author Hugo
 * @since 7 Sep 2023
 */
public class DataValueCodecProvider implements CodecProvider
{
    private DataValueCodec codec;
    

    public DataValueCodecProvider()
    {
        codec = new DataValueCodec(null);
    }

    public void setDatastore(Datastore datastore)
    {
        codec.setDatastore(datastore);
    }

    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry)
    {
        if (clazz == DataValue.class            ) { return (Codec<T>)codec; }
        if (clazz == Literal.class              ) { return (Codec<T>)codec; }
        if (clazz == LanguageTaggedLiteral.class      ) { return (Codec<T>)codec; }
        if (clazz == DatatypeLiteral.class      ) { return (Codec<T>)codec; }

        return null;
    }

    /*
    private Codec<?> getCodec(CodecRegistry registry)
    {
        if ( codec == null ) { codec = new DataValueCodec(registry); }
        return codec;
    }
    */

}
