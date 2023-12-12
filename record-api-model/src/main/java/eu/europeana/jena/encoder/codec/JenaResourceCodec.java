/**
 * 
 */
package eu.europeana.jena.encoder.codec;

import eu.europeana.jena.encoder.JenaObjectDecoder.DecoderContext;
import eu.europeana.jena.encoder.JenaObjectEncoder.EncoderContext;
import eu.europeana.jena.encoder.library.ClassTemplate;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;

/**
 * @author Hugo
 * @since 15 Oct 2023
 */
public class JenaResourceCodec implements JenaCodec<Object>
{
    private ClassTemplate template;

    public JenaResourceCodec(ClassTemplate template) {
        this.template = template;
    }

    @Override
    public Class<Object> getSupportedClass() { return Object.class; }

    @Override
    public void encode(Model m, Object value, EncoderContext context) {
        context.process(template, value);
    }

    @Override
    public Object decode(RDFNode node, DecoderContext context) {
        return null;
//        if ( !node.isResource() ) { return null; }

//        return context.process(node.asResource(), template);
    }
}
