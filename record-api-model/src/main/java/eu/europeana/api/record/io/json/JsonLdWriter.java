package eu.europeana.api.record.io.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.ContextAttributes;
import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.format.FormatWriter;
import eu.europeana.api.record.io.RecordIOConfig;
import eu.europeana.api.record.model.ProvidedCHO;

import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Stack;

import static eu.europeana.api.config.AppConfigConstants.BEAN_JSON_MAPPER;
import static eu.europeana.api.record.model.ModelConstants.*;

@Component(AppConfigConstants.BEAN_FORMAT_WRITER_JSONLD) 
@Import(RecordIOConfig.class)
public class JsonLdWriter implements FormatWriter<ProvidedCHO> {

    @Resource( name = BEAN_JSON_MAPPER)
    private ObjectMapper mapper;

    public static ThreadLocal<Stack<String>> stack = new ThreadLocal();

    public JsonLdWriter() {}

    /**
     * Serialises the ProvidedCho into the output stream
     * @param providedCHO object to be serialised
     * @param out output stream
     * @throws IOException
     */
    @Override
    public void write(ProvidedCHO providedCHO, OutputStream out) throws IOException {
        ContextAttributes attrs = ContextAttributes.getEmpty()
                .withSharedAttribute(context, new ResourceContext(providedCHO.getID() + "/"
                                                                , contextUri));
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
    public void write(Iterator<ProvidedCHO> providedCHOS, int size, OutputStream out) throws IOException {
        Stack lStack = new Stack<String>();
        stack.set(lStack);
        try {
            OutputStreamWriter writer = new OutputStreamWriter(out);
            writer.write("{");
            writer.write("\"@context\": \"" + contextUri + "\",");
            writer.write("\"type\": \"ResultPage\",");
            writer.write("\"total\":" + size + ",");
            writer.write("\"items\": [");
            int counter = 1;
            while (providedCHOS.hasNext()) {
                ProvidedCHO providedCHO = providedCHOS.next();
                ContextAttributes attrs = ContextAttributes.getEmpty()
                        .withSharedAttribute(context, new ResourceContext(providedCHO.getID() + "/"));
                mapper.setDefaultAttributes(attrs);
                try {
                    if (counter > size) { break; }
                    if (counter++ > 1) { writer.write(","); }
                    mapper.writerWithDefaultPrettyPrinter().writeValues(writer).write(providedCHO);
                }
                finally {
                    lStack.clear();
                }
            }
            writer.write("]}");
            writer.flush();
        }
        finally {
            stack.remove();
        }
    }
}

