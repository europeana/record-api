package eu.europeana.api.record.io.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.format.FormatWriter;
import eu.europeana.api.record.model.ProvidedCHO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
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

        Stack lStack = new Stack<String>();
        try {
            stack.set(lStack);
            mapper.writerWithDefaultPrettyPrinter().writeValues(out).write(providedCHO);
        }
        finally {
            lStack.clear();
            stack.remove();
        }
    }

    @Override
    public void write(List<ProvidedCHO> providedCHOS, OutputStream out) throws IOException {
        ArrayNode records = mapper.createArrayNode();
        providedCHOS.stream()
                .forEach(
                        providedCHO -> {
                            Stack lStack = new Stack<String>();
                            stack.set(lStack);
                            try {
                                ContextAttributes attrs = ContextAttributes.getEmpty().withSharedAttribute(context, new Context(providedCHO.getID()));
                                mapper.setDefaultAttributes(attrs);
                                records.add(mapper.valueToTree(providedCHO));
                            } finally {
                                lStack.clear();
                                stack.remove();
                            }
                        });

        ObjectNode result = mapper.createObjectNode();
        result.set("type", mapper.valueToTree("ResultPage"));
        result.set("total", mapper.valueToTree(records.size()));
        result.set("items", records);

        mapper.writerWithDefaultPrettyPrinter().writeValues(out).write(result);
    }
}

