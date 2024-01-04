package dev.morphia.mapping.codec;

import eu.europeana.api.record.db.codec.RecordApiCodecProvider;

/**
 * @author Hugo
 * @since 27 Nov 2023
 */
public class CodecUtils {

    public static void register(MorphiaCodecProvider provider
                     , RecordApiCodecProvider dvProvider) {
        // this can only be accessed via this package , the method is protected
        provider.getPropertyCodecProviders().add(0, dvProvider);
    }
}
