/**
 * 
 */
package eu.europeana.jena.encoder.codec;

import eu.europeana.jena.encoder.JenaEncoderException;
import eu.europeana.jena.encoder.JenaObjectDecoder.DecoderContext;
import eu.europeana.jena.encoder.JenaObjectEncoder.EncoderContext;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Hugo
 * @since 12 Oct 2023
 */
public class JenaDateCodec implements JenaCodec<Date>
{
    public static JenaDateCodec INSTANCE = new JenaDateCodec();

    private SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");

    @Override
    public Class<Date> getSupportedClass() { return Date.class; }

    @Override
    public void encode(Model m, Date value, EncoderContext context) {
        Property property = context.getProperty();
        if ( property == null ) { return; }
        context.getResource().addLiteral(property, fmt.format(value));
    }

    @Override
    public Date decode(RDFNode node, DecoderContext context) {
        if ( !node.isLiteral() ) { return null; }

        try { return fmt.parse(node.asLiteral().getString()); }
        catch (ParseException e) { throw new JenaEncoderException(e); }
    }
}
