package eu.europeana.api.record.io.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;
import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.format.FormatWriter;
import eu.europeana.api.record.io.json.Context;
import eu.europeana.api.record.model.ProvidedCHO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Stack;

import static eu.europeana.api.config.AppConfigConstants.BEAN_JSON_MAPPER;
import static eu.europeana.api.record.model.ModelConstants.CONTEXT;

@Component(AppConfigConstants.BEAN_RECORD_JSONLD_SERIALIZER)
public class JsonLdSerializer implements FormatWriter<ProvidedCHO> {

    @Resource( name = BEAN_JSON_MAPPER)
    private ObjectMapper mapper;

    public static ThreadLocal<Stack<String>> stack = new ThreadLocal();

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
        ContextAttributes attrs = ContextAttributes.getEmpty().withSharedAttribute(CONTEXT, "EDM_CONTEXT");
        mapper.setDefaultAttributes(attrs);
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(providedCHO);
    }

    @Override
    public void write(ProvidedCHO cho, OutputStream out) throws IOException {
        ContextAttributes attrs = ContextAttributes.getEmpty()
                .withSharedAttribute("@context", new Context(cho.getID()));
        mapper.setDefaultAttributes(attrs);

        Stack stack = new Stack<String>();
        try {
            this.stack.set(stack);
            mapper.writerWithDefaultPrettyPrinter().writeValues(out).write(cho);
        }
        finally {
            stack.clear();
            this.stack.remove();
        }
    }
}

