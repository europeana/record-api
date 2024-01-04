package eu.europeana.jena.encoder.codec;

import eu.europeana.jena.encoder.JenaObjectDecoder.DecoderContext;
import eu.europeana.jena.encoder.JenaObjectEncoder.EncoderContext;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;

/**
 * @author Hugo
 * @since 12 Oct 2023
 */
public class JenaArrayCodec implements JenaCodec<Object[]>
{
    public static JenaArrayCodec INSTANCE = new JenaArrayCodec();

    @Override
    public Class<Object[]> getSupportedClass() { return Object[].class; }

    @Override
    public void encode(Model m, Object[] value, EncoderContext context) {
        for ( Object v : value ) { context.process(v); }
    }

    @Override
    public Object[] decode(RDFNode node, DecoderContext context) {
        return null;
    }
}
