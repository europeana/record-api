package eu.europeana.jena.encoder.codec;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Hugo
 * @since 16 Oct 2023
 */
public class CodecUtils {

    public static Class<?> getGenericClass(Type type) {
        if ( !(type instanceof ParameterizedType) ) { return null; }

        ParameterizedType ptype = (ParameterizedType)type;
        type = ptype.getActualTypeArguments()[0];
        if ( type instanceof ParameterizedType ) {
            return (Class<?>)((ParameterizedType)type).getRawType();
        }
        return (Class<?>)type;
    }
}
