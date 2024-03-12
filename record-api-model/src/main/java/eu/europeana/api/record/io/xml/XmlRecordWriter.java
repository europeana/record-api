package eu.europeana.api.record.io.xml;

import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.format.FormatWriter;
import eu.europeana.api.record.model.ProvidedCHO;
import eu.europeana.jena.encoder.JenaObjectEncoder;
import eu.europeana.jena.encoder.library.TemplateLibrary;
import org.apache.jena.rdf.model.Model;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

/**
 * @author Hugo
 * @since 8 Nov 2023
 */
@Component(AppConfigConstants.BEAN_RECORD_XML_SERIALIZER)
public class XmlRecordWriter extends EdmXmlStreamWriter implements FormatWriter<ProvidedCHO> {

    // TODO not thread safe
    private JenaObjectEncoder encoder;

    // TODO later
    // encoder = new JenaObjectEncoder(RecordApiTemplateLibrary.INSTANCE);

    @Resource(name = AppConfigConstants.BEAN_RECORD_TEMPLATE_LIBRARY)
    private TemplateLibrary library;

    @Override
    public void write(ProvidedCHO cho, OutputStream out) throws IOException {
        try {
            Model m = new JenaObjectEncoder(library).encode(cho, cho.getID());
            super.write(m, out);
        }
        catch(XMLStreamException e) { throw new IOException(e); }
    }

    @Override
    public void write(Iterator<ProvidedCHO> value, OutputStream out) throws IOException {
        // empty for now
    }
}
