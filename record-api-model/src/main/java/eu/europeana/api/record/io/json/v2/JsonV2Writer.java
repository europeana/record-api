package eu.europeana.api.record.io.json.v2;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.jena.encoder.library.TemplateLibrary;
import org.apache.jena.rdf.model.Model;

import eu.europeana.api.format.FormatWriter;
import eu.europeana.api.record.model.ProvidedCHO;
import eu.europeana.jena.encoder.JenaObjectEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Hugo
 * @since 14 Oct 2023
 */
@Component(AppConfigConstants.BEAN_FORMAT_WRITER_V2_JSON)
public class JsonV2Writer extends RecordV2JsonWriter
                          implements FormatWriter<ProvidedCHO> {

    @Resource(name = AppConfigConstants.BEAN_RECORD_TEMPLATE_LIBRARY)
    private TemplateLibrary library;

    @Override
    public void write(ProvidedCHO cho, OutputStream out) throws IOException {
        Model m = new JenaObjectEncoder(library).encode(cho, cho.getID());
        super.write(m, out);
    }

    @Override
    public void write(Iterator<ProvidedCHO> value, int size, OutputStream out) throws IOException {
        // empty for now
    }

}
