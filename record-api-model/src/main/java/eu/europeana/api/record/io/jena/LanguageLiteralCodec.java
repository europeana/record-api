/**
 * 
 */
package eu.europeana.api.record.io.jena;

import eu.europeana.api.record.model.data.LanguageLiteral;
import eu.europeana.jena.encoder.JenaObjectDecoder.DecoderContext;
import eu.europeana.jena.encoder.JenaObjectEncoder.EncoderContext;
import eu.europeana.jena.encoder.codec.JenaCodec;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;

/**
 * @author Hugo
 * @since 12 Oct 2023
 */
public class LanguageLiteralCodec implements JenaCodec<LanguageLiteral>
{
    public static LanguageLiteralCodec INSTANCE = new LanguageLiteralCodec();

    @Override
    public Class<LanguageLiteral> getSupportedClass() { return LanguageLiteral.class; }

    @Override
    public void encode(Model m, LanguageLiteral value, EncoderContext context) {
        context.getResource().addProperty(context.getProperty()
                                        , value.getValue()
                                        , value.getLanguage());
    }

    @Override
    public LanguageLiteral decode(RDFNode value, DecoderContext context) {
        if ( !value.isLiteral() ) { return null; }

        Literal literal = value.asLiteral();
        String  lang    = literal.getLanguage();
        if ( lang == null ) { return null; }
        return new LanguageLiteral(literal.getString(), lang);
    }
}
