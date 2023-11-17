/**
 * 
 */
package eu.europeana.api.record.io.json;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.format.FormatWriter;
import eu.europeana.api.record.model.ProvidedCHO;
import eu.europeana.api.record.model.data.ObjectReference;
import eu.europeana.api.record.model.internal.LanguageMap;
import eu.europeana.api.record.model.internal.LanguageMapArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Stack;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static eu.europeana.api.config.AppConfigConstants.BEAN_JSON_MAPPER;

/**
 * @author Hugo
 * @since 14 Oct 2023
 */
@Configuration
public class JsonLdWriter {

   public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

//    public static ThreadLocal<Stack<String>> stack = new ThreadLocal();
//
//    @Resource(name = AppConfigConstants.BEAN_JSON_MAPPER)
//    private ObjectMapper mapper;


    @Primary
    @Bean(AppConfigConstants.BEAN_JSON_MAPPER)
    public ObjectMapper mapper() {
        ObjectMapper mapper = new ObjectMapper();

        SimpleModule module = new SimpleModule();
        module.addSerializer(ObjectReference.class , ObjectReferenceSerializer.INSTANCE);
        module.addSerializer(LanguageMap.class     , LanguageMapSerializer.INSTANCE);
        module.addSerializer(LanguageMapArray.class, LanguageMapArraySerializer.INSTANCE);
        module.addSerializer(Context.class         , ContextSerializer.INSTANCE);

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
