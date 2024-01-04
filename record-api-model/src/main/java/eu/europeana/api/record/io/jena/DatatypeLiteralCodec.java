package eu.europeana.api.record.io.jena;

import eu.europeana.api.record.model.data.Datatype;
import eu.europeana.api.record.model.data.DatatypeLiteral;
import eu.europeana.api.record.model.data.DatatypeUtils;
import eu.europeana.jena.encoder.JenaEncoderException;
import eu.europeana.jena.encoder.JenaObjectDecoder.DecoderContext;
import eu.europeana.jena.encoder.JenaObjectEncoder.EncoderContext;
import eu.europeana.jena.encoder.codec.JenaCodec;
import eu.europeana.jena.encoder.utils.JenaUtils;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;


public class DatatypeLiteralCodec implements JenaCodec<DatatypeLiteral>
{
    public static DatatypeLiteralCodec INSTANCE = new DatatypeLiteralCodec();

    @Override
    public Class<DatatypeLiteral> getSupportedClass() {
        return DatatypeLiteral.class;
    }

    @Override
    public void encode(Model m, DatatypeLiteral literal, EncoderContext context) {
        Literal l = m.createTypedLiteral(literal.getValue().toString()
                , literal.getDatatype().getURI());
        context.getResource().addLiteral(context.getProperty(), l);
    }

    @Override
    public DatatypeLiteral decode(RDFNode node, DecoderContext context) {
        if ( !node.isLiteral() ) { return null; }

        Literal literal  = node.asLiteral();
        if ( !JenaUtils.hasDatatype(literal.getDatatype()) ) { return null; }

        Object value = LiteralCodec.parseObject(literal, context);
        if ( value == null ) {
            throw new JenaEncoderException("Literal: " + literal);
        }
        Datatype dt = DatatypeUtils.getDatatypeByUri(literal.getDatatypeURI());
        return new DatatypeLiteral(value, dt);
    }
}
