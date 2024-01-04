package eu.europeana.api.record.io.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;
import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.format.FormatWriter;
import eu.europeana.api.record.model.ProvidedCHO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Stack;

import static eu.europeana.api.config.AppConfigConstants.BEAN_JSON_MAPPER;
import static eu.europeana.api.record.model.ModelConstants.context;

@Component(AppConfigConstants.BEAN_RECORD_JSONLD_SERIALIZER)
public class JsonLdWriter implements FormatWriter<ProvidedCHO> {

    @Resource( name = BEAN_JSON_MAPPER)
    private ObjectMapper mapper;

    public static ThreadLocal<Stack<String>> stack = new ThreadLocal();

    /**
     * Serialises the ProvidedCho into the output stream
     * @param providedCHO object to be serialised
     * @param out output stream
     * @throws IOException
     */
    @Override
    public void write(ProvidedCHO providedCHO, OutputStream out) throws IOException {
        ContextAttributes attrs = ContextAttributes.getEmpty()
                .withSharedAttribute(context, new Context(providedCHO.getID()));
        mapper.setDefaultAttributes(attrs);

        Stack stack = new Stack<String>();
        try {
            this.stack.set(stack);
            mapper.writerWithDefaultPrettyPrinter().writeValues(out).write(providedCHO);
        }
        finally {
            stack.clear();
            this.stack.remove();
        }
    }
}

