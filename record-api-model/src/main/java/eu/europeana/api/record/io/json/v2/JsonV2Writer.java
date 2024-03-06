/**
 * 
 */
package eu.europeana.api.record.io.json.v2;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.jena.encoder.library.TemplateLibrary;
import org.apache.jena.rdf.model.Model;

import eu.europeana.api.format.FormatWriter;
import eu.europeana.api.record.model.ProvidedCHO;
import eu.europeana.jena.encoder.JenaObjectEncoder;

import javax.annotation.Resource;

/**
 * @author Hugo
 * @since 14 Oct 2023
 */
public class JsonV2Writer extends RecordV2JsonWriter
                          implements FormatWriter<ProvidedCHO> {

    private JenaObjectEncoder encoder;

    @Resource(name = AppConfigConstants.BEAN_RECORD_TEMPLATE_LIBRARY)
    private TemplateLibrary library;

    public JsonV2Writer() {
        encoder = new JenaObjectEncoder(library);
    }

    @Override
    public void write(ProvidedCHO cho, OutputStream out) throws IOException {
        Model m = encoder.encode(cho, cho.getID());
        super.write(m, out);
    }

    @Override
    public void write(List<ProvidedCHO> value, OutputStream out) throws IOException {
        // empty for now
    }

}
