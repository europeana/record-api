package eu.europeana.api.record.io.xml;

import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.format.FormatWriter;
import eu.europeana.api.record.model.ProvidedCHO;
import eu.europeana.jena.encoder.JenaObjectEncoder;
import eu.europeana.jena.encoder.library.TemplateLibrary;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
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
@Component(AppConfigConstants.BEAN_FORMAT_WRITER_XML)
public class XmlRecordWriter extends EdmXmlStreamWriter implements FormatWriter<ProvidedCHO> {

    @Resource(name = AppConfigConstants.BEAN_RECORD_TEMPLATE_LIBRARY)
    private TemplateLibrary library;

    @Override
    public void write(ProvidedCHO cho, OutputStream out) throws IOException {
        try {
            Model m = new JenaObjectEncoder(library).encode(cho);
            super.write(m.getResource(cho.getID()), out);
        }
        catch(XMLStreamException e) { throw new IOException(e); }
    }

    @Override
    public void write(Iterator<ProvidedCHO> iter, int size, OutputStream out) throws IOException {
        try {
            if ( iter.hasNext() && size == 1 ) {
                write(iter.next(), out);
                return;
            }
            JenaObjectEncoder encoder = new JenaObjectEncoder(library);
            Model m = ModelFactory.createDefaultModel();
            while ( iter.hasNext() && size-- > 0 ) {
                ProvidedCHO cho = iter.next();
                encoder.encode(cho, m);
            }
            super.write(m, out);
        }
        catch(XMLStreamException e) { throw new IOException(e); }
    }
}
