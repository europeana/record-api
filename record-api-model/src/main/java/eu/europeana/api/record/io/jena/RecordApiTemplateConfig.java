package eu.europeana.api.record.io.jena;

import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.edm.NamespaceResolver;
import eu.europeana.api.edm.Namespaces;
import eu.europeana.jena.encoder.codec.CodecRegistry;
import eu.europeana.jena.encoder.library.DefaultUriNormalizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Srishti Singh
 * @since 17 Nov 2023
 */
@Configuration
public class RecordApiTemplateConfig {

    @Bean(name = AppConfigConstants.BEAN_CODEC_REGISTRY)
    public CodecRegistry getCodecRegistry() {
        return new CodecRegistry();
    }

    @Bean(name = AppConfigConstants.BEAN_NAMESPACE_RESOLVER)
    public NamespaceResolver getNamespaceResolver() {
        return new Namespaces();
    }

    @Bean(name = AppConfigConstants.BEAN_DEFAULT_URI_RESOLVER)
    public DefaultUriNormalizer getDefaultUriNormalizer() {
        return new DefaultUriNormalizer();
    }
}
