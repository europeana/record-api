package eu.europeana.api.record.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import eu.europeana.api.record.vocabulary.AppConfigConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Configuration
public class SerializationConfig {

    private final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXX");

    @Primary
    @Bean(AppConfigConstants.BEAN_JSON_MAPPER)
    public ObjectMapper mapper() {
        ObjectMapper mapper =
                new Jackson2ObjectMapperBuilder()
                        .defaultUseWrapper(false)
                        .dateFormat(dateFormat)
                        .featuresToEnable(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)
                        .serializationInclusion(JsonInclude.Include.NON_NULL)
                        .build();
        mapper.findAndRegisterModules();
        return mapper;
    }


    @Bean
    public com.fasterxml.jackson.databind.Module jaxbAnnotationModule() {
        return new JaxbAnnotationModule();
    }


}
