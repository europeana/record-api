package eu.europeana.api.record.config;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import eu.europeana.api.config.AppConfigConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Configuration
public class JsonLdSerializationConfig {

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXX");
//
//    @Primary
//    @Bean(AppConfigConstants.BEAN_JSON_MAPPER)
//    public ObjectMapper mapper() {
//        ObjectMapper mapper =
//                new Jackson2ObjectMapperBuilder()
//                        .defaultUseWrapper(false)
//                        .dateFormat(dateFormat)
//                        .featuresToEnable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)
//                        .serializationInclusion(JsonInclude.Include.NON_NULL)
//                        .build();
//
//        mapper.setVisibility(
//                mapper.getVisibilityChecker()
//                        .withCreatorVisibility(NONE)
//                        .withFieldVisibility(NONE)
//                        .withGetterVisibility(NONE)
//                        .withIsGetterVisibility(NONE)
//                        .withSetterVisibility(NONE));
//        mapper.findAndRegisterModules();
//
//        return mapper;
//    }
//
//
//    @Bean
//    public com.fasterxml.jackson.databind.Module jaxbAnnotationModule() {
//        return new JaxbAnnotationModule();
//    }


}
