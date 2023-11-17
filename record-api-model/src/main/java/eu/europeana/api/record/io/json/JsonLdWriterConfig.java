/**
 *
 */
package eu.europeana.api.record.io.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.record.model.data.ObjectReference;
import eu.europeana.api.record.model.internal.LanguageMap;
import eu.europeana.api.record.model.internal.LanguageMapArray;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.text.SimpleDateFormat;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

/**
 * @author Hugo
 * @since 14 Oct 2023
 * @Refractored Srishti Singh 17 November 2023
 */
@Configuration
public class JsonLdWriterConfig {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

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
                        .withCreatorVisibility(Visibility.NONE)
                        .withFieldVisibility(Visibility.NONE)
                        .withGetterVisibility(Visibility.NONE)
                        .withIsGetterVisibility(Visibility.NONE)
                        .withSetterVisibility(Visibility.NONE));

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

}
