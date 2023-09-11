package eu.europeana.api.record.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.europeana.api.record.model.ProvidedCHO;
import eu.europeana.api.record.vocabulary.AppConfigConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Locale;
import static eu.europeana.api.record.vocabulary.AppConfigConstants.BEAN_JSON_MAPPER;

@Component(AppConfigConstants.BEAN_RECORD_JSONLD_SERIALIZER)
public class JsonLdSerializer {

    private final ObjectMapper mapper;

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    @Autowired
    public JsonLdSerializer(@Qualifier(BEAN_JSON_MAPPER) ObjectMapper objectMapper) {
        mapper = objectMapper.copy();
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        mapper.setDateFormat(df);
    }

    public String serialize(ProvidedCHO providedCHO) throws IOException {
        return serializeToJson(mapper, providedCHO);
    }


    public String serializeObject(Object obj) throws IOException {
        return mapper.writeValueAsString(obj);
    }

    /**
     * Serialises the List of Entity Records
     *
     * @param mapper
     * @param providedCHO :  record
     * @return serialised string results
     * @throws IOException
     */
    public static String serializeToJson(ObjectMapper mapper, ProvidedCHO providedCHO)
            throws IOException {
        final StringWriter buffer = new StringWriter();
        mapper.writeValue(buffer, mapper.valueToTree(providedCHO));
        return buffer.toString();
    }
}

