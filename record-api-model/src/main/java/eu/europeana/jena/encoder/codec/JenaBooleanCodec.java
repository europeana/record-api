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
public class JenaBooleanCodec implements JenaCodec<Boolean>
{
    public static JenaBooleanCodec INSTANCE = new JenaBooleanCodec();

    @Override
    public Class<Boolean> getSupportedClass() { return Boolean.class; }

    @Override
    public void encode(Model m, Boolean value, EncoderContext context) {
        Property property = context.getProperty();
        if ( property == null ) { return; }
        context.getResource().addLiteral(property, value.toString());
    }

    @Override
    public Boolean decode(RDFNode node, DecoderContext context) {
        if ( !node.isLiteral() ) { return null; }

        return Boolean.valueOf(node.asLiteral().getLexicalForm());
    }
}
