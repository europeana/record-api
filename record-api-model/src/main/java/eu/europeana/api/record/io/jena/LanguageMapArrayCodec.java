/**
 * 
 */
package eu.europeana.api.record.io.jena;

import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.internal.LanguageMapArray;
import eu.europeana.jena.encoder.JenaObjectDecoder.DecoderContext;
import eu.europeana.jena.encoder.JenaObjectEncoder.EncoderContext;
import eu.europeana.jena.encoder.codec.JenaCodec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.StmtIterator;

/**
 * @author Hugo
 * @since 12 Oct 2023
 */
public class LanguageMapArrayCodec implements JenaCodec<LanguageMapArray>
{
    public static LanguageMapArrayCodec INSTANCE = new LanguageMapArrayCodec();

    @Override
    public Class<LanguageMapArray> getSupportedClass() { return LanguageMapArray.class; }

    @Override
    public void encode(Model m, LanguageMapArray map, EncoderContext context) {
        for ( Literal<?> l : map.getValues() ) { context.process(l); }
    }

    @Override
    public LanguageMapArray decode(RDFNode node, DecoderContext context) {
        Property property = context.getProperty();
        if ( property == null ) { return null; }

        StmtIterator iter = context.getResource().listProperties(property);
        if ( !iter.hasNext() ) { return null; }

        JenaCodec<Literal> codec = context.getLibrary().getCodec(Literal.class);
        LanguageMapArray map = new LanguageMapArray();
        while ( iter.hasNext() )
        {
            RDFNode value = iter.next().getObject();
            Literal literal = codec.decode(value, context);
            if ( literal != null ) { map.add(literal); }
        }
        return map;
    }
}
