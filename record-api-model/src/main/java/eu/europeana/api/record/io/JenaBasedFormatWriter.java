package eu.europeana.api.record.io;

import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.format.FormatWriter;
import eu.europeana.api.record.model.ProvidedCHO;
import eu.europeana.jena.encoder.JenaObjectEncoder;
import eu.europeana.jena.encoder.library.TemplateLibrary;
import org.apache.jena.rdf.model.Model;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author Hugo
 * @since 12 Oct 2023
 */
//@Component(AppConfigConstants.BEAN_RECORD_JENA_FORMAT_SERIALIZER)
public class JenaBasedFormatWriter implements FormatWriter<ProvidedCHO> {

    private String format;

    @Resource(name = AppConfigConstants.BEAN_RECORD_TEMPLATE_LIBRARY)
    private TemplateLibrary library;

    public JenaBasedFormatWriter(String format) {
        this.format = format;
    }

    @Override
    public void write(ProvidedCHO cho, OutputStream out) throws IOException {
        Model m = new JenaObjectEncoder(library).encode(cho, cho.getID());
        m.write(out, format);
    }

    @Override
    public void write(List<ProvidedCHO> value, OutputStream out) throws IOException {
        // empty for now
    }
}
