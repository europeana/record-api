/**
 * 
 */
package eu.europeana.api.record.io.jena;

import eu.europeana.api.edm.EDM;
import eu.europeana.jena.encoder.JenaObjectDecoder.DecoderContext;
import eu.europeana.jena.encoder.JenaObjectEncoder.EncoderContext;
import eu.europeana.jena.encoder.codec.JenaCodec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

import static org.apache.jena.rdf.model.ResourceFactory.createResource;

/**
 * @author Hugo
 * @since 15 Oct 2023
 */
public class FulltextFieldCodec implements JenaCodec<Boolean>
{
    public static FulltextFieldCodec INSTANCE = new FulltextFieldCodec();

    private static Resource FULLTEXT_RESOURCE = createResource(EDM.NS + EDM.FullTextResource);

    @Override
    public Class<Boolean> getSupportedClass() { return Boolean.class; }

    @Override
    public void encode(Model m, Boolean value, EncoderContext context) {
        if ( value ) { context.getResource().addProperty(RDF.type, FULLTEXT_RESOURCE); }
    }

    @Override
    public Boolean decode(RDFNode value, DecoderContext context) {
        return context.getResource().hasProperty(context.getProperty()
                                               , FULLTEXT_RESOURCE);
    }
}
