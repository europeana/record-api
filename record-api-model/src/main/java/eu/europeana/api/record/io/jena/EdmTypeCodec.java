/**
 * 
 */
package eu.europeana.api.record.io.jena;

import eu.europeana.api.record.model.data.EdmType;
import eu.europeana.jena.encoder.JenaObjectDecoder.DecoderContext;
import eu.europeana.jena.encoder.JenaObjectEncoder.EncoderContext;
import eu.europeana.jena.encoder.codec.JenaCodec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;

/**
 * @author Hugo
 * @since 12 Oct 2023
 */
public class EdmTypeCodec implements JenaCodec<EdmType>
{
    public static EdmTypeCodec INSTANCE = new EdmTypeCodec();

    @Override
    public Class<EdmType> getSupportedClass() { return EdmType.class; }

    @Override
    public void encode(Model m, EdmType value, EncoderContext context) {
        Property property = context.getProperty();
        if ( property == null ) { return; }
        context.getResource().addLiteral(property, value.toString());
    }

    @Override
    public EdmType decode(RDFNode node, DecoderContext context) {
        if ( !node.isLiteral() ) { return null; }

        return EdmType.decode(node.asLiteral().getValue().toString());
    }
}
