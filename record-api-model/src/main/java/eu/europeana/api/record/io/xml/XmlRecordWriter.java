/**
 * 
 */
package eu.europeana.api.record.io.xml;

import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.format.FormatWriter;
import eu.europeana.api.record.io.jena.RecordApiTemplateLibrary;
import eu.europeana.api.record.model.ProvidedCHO;
import eu.europeana.jena.encoder.JenaObjectEncoder;
import eu.europeana.jena.encoder.library.TemplateLibrary;
import org.apache.jena.rdf.model.Model;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Hugo
 * @since 8 Nov 2023
 */
@Component(AppConfigConstants.BEAN_RECORD_XML_SERIALIZER)
public class XmlRecordWriter extends EdmXmlStreamWriter implements FormatWriter<ProvidedCHO> {

   //@Resource(name = AppConfigConstants.BEAN_RECORD_TEMPLATE_LIBRARY)
    private RecordApiTemplateLibrary library;

    public XmlRecordWriter() {
        library = RecordApiTemplateLibrary.INSTANCE;
    }

    @Override
    public void write(ProvidedCHO cho, OutputStream out) throws IOException {
        try {
            Model m = new JenaObjectEncoder(library).encode(cho, cho.getID());
            super.write(m, out);
        }
        catch(XMLStreamException e) { throw new IOException(e); }
    }
}