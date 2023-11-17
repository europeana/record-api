/**
 * 
 */
package eu.europeana.api.record.io.jena;

import eu.europeana.api.record.model.data.DataValue;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.data.ObjectReference;
import eu.europeana.jena.encoder.JenaObjectDecoder.DecoderContext;
import eu.europeana.jena.encoder.JenaObjectEncoder.EncoderContext;
import eu.europeana.jena.encoder.codec.JenaCodec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;

/**
 * @author Hugo
 * @since 12 Oct 2023
 */
public class DataValueCodec implements JenaCodec<DataValue>
{
    public static DataValueCodec INSTANCE = new DataValueCodec();

    public DataValueCodec() {}

    @Override
    public Class<DataValue> getSupportedClass() { return DataValue.class; }

    @Override
    public void encode(Model m, DataValue value, EncoderContext context) {}

    @Override
    public DataValue decode(RDFNode node, DecoderContext context) {
        JenaCodec<?> codec = null;
        if ( node.isLiteral() ) { 
            codec = context.getLibrary().getCodec(Literal.class);
        }
        else if ( node.isResource() ) { 
            codec = context.getLibrary().getCodec(ObjectReference.class);
        }
        
        return ( codec != null ? (DataValue)codec.decode(node, context) : null );
    }
}
