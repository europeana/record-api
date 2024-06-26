/**
 * 
 */
package eu.europeana.jena.encoder.codec;

import eu.europeana.jena.encoder.JenaObjectDecoder.DecoderContext;
import eu.europeana.jena.encoder.JenaObjectEncoder.EncoderContext;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;

/**
 * @author Hugo
 * @since 12 Oct 2023
 */
public class JenaStringCodec implements JenaCodec<String>
{
    public static JenaStringCodec INSTANCE = new JenaStringCodec();

    @Override
    public Class<String> getSupportedClass() { return String.class; }

    @Override
    public void encode(Model m, String value, EncoderContext context) {
        Property property = context.getProperty();
        if ( property == null ) { return; }
        context.getResource().addLiteral(property, value);
    }

    @Override
    public String decode(RDFNode node, DecoderContext context) {
        if ( !node.isLiteral() ) { return null; }

        return node.asLiteral().getLexicalForm();
    }
}
