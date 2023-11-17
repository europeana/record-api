package eu.europeana.jena.encoder.codec;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hugo
 * @since 12 Oct 2023
 */
public class CodecRegistry {
    private final Map<Class<?>, JenaCodec<?>> codecs = new HashMap<>();

//    public CodecRegistry() {
//        addCodecs();
//    }

    public void addCodec(final JenaCodec<?> codec) {
        codecs.put(codec.getSupportedClass(), codec);
    }

    public <T> JenaCodec<T> get(final Class<T> clazz)
    {
        return (JenaCodec<T>)codecs.get(clazz);
    }

    public <T> JenaCodec<T> getRecursively(final Class<T> clazz)
    {
        JenaCodec codec = get(clazz);
        if ( codec != null ) { return codec; }

        for ( Class c : clazz.getInterfaces() )
        {
            codec = get(c);
            if ( codec != null ) { return codec; }
        }

        Class parent = clazz.getSuperclass();
        return ( parent == null ? null : getRecursively(parent) );
    }

    public void addCodecs() {
        addCodec(JenaArrayCodec.INSTANCE);
        addCodec(JenaCollectionCodec.INSTANCE);
        addCodec(JenaEnumCodec.INSTANCE);
        addCodec(JenaBooleanCodec.INSTANCE);
        JenaNumericCodec.addToRegistry(this);
        addCodec(JenaDateCodec.INSTANCE);
        JenaTimeCodec.addToRegistry(this);
    }
}