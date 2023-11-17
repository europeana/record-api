package eu.europeana.api.record.io;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.edm.NamespaceResolver;
import eu.europeana.api.edm.Namespaces;
import eu.europeana.api.record.io.json.*;
import eu.europeana.api.record.model.data.ObjectReference;
import eu.europeana.api.record.model.internal.LanguageMap;
import eu.europeana.api.record.model.internal.LanguageMapArray;
import eu.europeana.jena.encoder.codec.CodecRegistry;
import eu.europeana.jena.encoder.library.DefaultUriNormalizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.text.SimpleDateFormat;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/**
 * Configuration class for the IO module.
 * This class contains all the required beans for Serializers - json, xml, jena formats
 * @author Srishti Singh
 * @since 17 Nov 2023
 */
@Configuration
public class RecordIOConfig {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    // RecordApiTemplateLibrary beans
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

    // JsonLdWriter bean
    @Primary
    @Bean(AppConfigConstants.BEAN_JSON_MAPPER)
    public ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(ObjectReference.class, ObjectReferenceSerializer.INSTANCE);
        module.addSerializer(LanguageMap.class, LanguageMapSerializer.INSTANCE);
        module.addSerializer(LanguageMapArray.class, LanguageMapArraySerializer.INSTANCE);
        module.addSerializer(Context.class, ContextSerializer.INSTANCE);

        mapper.registerModule(module);
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT));


        mapper.setVisibility(
                mapper.getVisibilityChecker()
                        .withCreatorVisibility(JsonAutoDetect.Visibility.NONE)
                        .withFieldVisibility(JsonAutoDetect.Visibility.NONE)
                        .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withIsGetterVisibility(JsonAutoDetect.Visibility.NONE)
                        .withSetterVisibility(JsonAutoDetect.Visibility.NONE));

        mapper.setVisibility(
                mapper.getVisibilityChecker()
                        .withCreatorVisibility(NONE)
                        .withFieldVisibility(NONE)
                        .withGetterVisibility(NONE)
                        .withIsGetterVisibility(NONE)
                        .withSetterVisibility(NONE));

        mapper.findAndRegisterModules();
        return mapper;
    }

    // JenaBasedFormatWriter beans based on formats
    @Bean(AppConfigConstants.BEAN_JENA_FORAMAT_WRITER_TURTLE)
    public JenaBasedFormatWriter getJenaBasedTurtleWriter() {
        return new JenaBasedFormatWriter("TURTLE");
    }

    @Bean(AppConfigConstants.BEAN_JENA_FORAMAT_WRITER_N3)
    public JenaBasedFormatWriter getJenaBasedN3Writer() {
        return new JenaBasedFormatWriter("N3");
    }

    @Bean(AppConfigConstants.BEAN_JENA_FORAMAT_WRITER_NT)
    public JenaBasedFormatWriter getJenaBasedNTWriter() {
        return new JenaBasedFormatWriter("NT");
    }
}
