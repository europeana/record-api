/**
 * 
 */
package eu.europeana.api.record.io.json.v2;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.jena.rdf.model.Model;

import eu.europeana.api.format.FormatWriter;
import eu.europeana.api.record.io.jena.RecordApiTemplateLibrary;
import eu.europeana.api.record.model.ProvidedCHO;
import eu.europeana.jena.encoder.JenaObjectEncoder;

/**
 * @author Hugo
 * @since 14 Oct 2023
 */
public class JsonV2Writer extends RecordV2JsonWriter 
                          implements FormatWriter<ProvidedCHO> {

    private JenaObjectEncoder encoder;

    public JsonV2Writer() {
        encoder = new JenaObjectEncoder(RecordApiTemplateLibrary.INSTANCE);
    }

    @Override
    public void write(ProvidedCHO cho, OutputStream out) throws IOException {
        Model m = encoder.encode(cho, cho.getID());
        super.write(m, out);
    }

}
