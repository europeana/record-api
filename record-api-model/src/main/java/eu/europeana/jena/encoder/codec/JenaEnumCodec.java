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
public class JenaEnumCodec implements JenaCodec<Enum>
{
    public static JenaEnumCodec INSTANCE = new JenaEnumCodec();

    @Override
    public Class<Enum> getSupportedClass() { return Enum.class; }

    @Override
    public void encode(Model m, Enum value, EncoderContext context) {
        Property property = context.getProperty();
        if ( property == null ) { return; }
        context.getResource().addLiteral(property, value.toString());
    }

    @Override
    public Enum decode(RDFNode node, DecoderContext context) {
        if ( !node.isLiteral() ) { return null; }

        Class<Enum> type = context.getCurrentTypeAsClass();
        return Enum.valueOf(type, node.asLiteral().getValue().toString());
    }
}
