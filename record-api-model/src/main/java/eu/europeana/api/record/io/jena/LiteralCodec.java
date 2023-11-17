/**
 * 
 */
package eu.europeana.api.record.io.jena;

import eu.europeana.api.record.model.data.*;
import eu.europeana.jena.encoder.JenaEncoderException;
import eu.europeana.jena.encoder.JenaObjectDecoder.DecoderContext;
import eu.europeana.jena.encoder.JenaObjectEncoder.EncoderContext;
import eu.europeana.jena.encoder.codec.JenaCodec;
import eu.europeana.jena.utils.JenaUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author Hugo
 * @since 12 Oct 2023
 */
public class LiteralCodec implements JenaCodec<Literal>
{
    public static LiteralCodec INSTANCE = new LiteralCodec();

    @Override
    public Class<Literal> getSupportedClass() { return Literal.class; }

    @Override
    public void encode(Model m, Literal value, EncoderContext context) {
        context.getResource().addLiteral(context.getProperty()
                , value.getValue().toString());
    }

    @Override
    public Literal<?> decode(RDFNode node, DecoderContext context) {
        if ( !node.isLiteral() ) { return null; }

        org.apache.jena.rdf.model.Literal literal  = node.asLiteral();
        String  lang = literal.getLanguage();
        if ( !StringUtils.isBlank(lang) ) {
            return new LanguageLiteral(literal.getValue().toString(), lang);
        }

        Object value = parseObject(literal, context);
        if ( value == null ) {
            throw new JenaEncoderException("Literal: " + literal);
        }
        if ( JenaUtils.hasDatatype(literal.getDatatype()) ) {
            Datatype dt = DatatypeUtils.getDatatypeByUri(literal.getDatatypeURI());
            System.err.println("dt = " + dt + " | " + literal.getDatatypeURI());
            return new DatatypeLiteral(value, dt);
        }

        return new Literal(value);
    }

    public static Object parseObject(org.apache.jena.rdf.model.Literal literal
            , DecoderContext context) {
        Type type = context.getCurrentType();
        if ( !(type instanceof ParameterizedType) ) {
            return literal.getValue().toString();
        }

        Type nested = ((ParameterizedType)type).getActualTypeArguments()[0];
        if ( nested == String.class ) { return literal.getLexicalForm(); }

        return context.decodeByClass(literal, (Class<?>)nested, nested, true);
    }
}
