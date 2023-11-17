/**
 * 
 */
package eu.europeana.api.record.io.jena;

import eu.europeana.api.record.model.EDMClass;
import eu.europeana.api.record.model.data.LocalReference;
import eu.europeana.api.record.model.data.ObjectReference;
import eu.europeana.api.record.model.data.SharedReference;
import eu.europeana.jena.encoder.JenaObjectDecoder.DecoderContext;
import eu.europeana.jena.encoder.JenaObjectEncoder.EncoderContext;
import eu.europeana.jena.encoder.codec.JenaCodec;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

/**
 * @author Hugo
 * @since 12 Oct 2023
 */
public class ObjectReferenceCodec implements JenaCodec<ObjectReference> {

    public static final ObjectReferenceCodec INSTANCE = new ObjectReferenceCodec();

    @Override
    public Class<ObjectReference> getSupportedClass() { return ObjectReference.class; }

    @Override
    public void encode(Model m, ObjectReference ref, EncoderContext context) {
        if ( !ref.isDereferenced() ) {
            String uri = context.expandUri(ref.getID());
            context.getResource().addProperty(context.getProperty()
                                            , m.createResource(uri));
            return;
        }

        context.process(ref.getDereferencedObject());
    }

    @Override
    public ObjectReference decode(RDFNode node, DecoderContext context) {
        if ( !node.isResource() ) { return null; }

        Resource r   = node.asResource();
        String   uri = context.compactUri(r.getURI());
        if ( !r.hasProperty(RDF.type) ) { return new LocalReference(uri); }

        Object   obj    = context.decodeResource(r);
        EDMClass edmObj = (obj != null && obj instanceof EDMClass ? (EDMClass)obj
                                                                  : null);

        if ( isLocal(uri) ) {
            return (edmObj == null ? new LocalReference(uri)
                                   : new LocalReference(edmObj));
        }

        return (edmObj == null ? new SharedReference(uri)
                               : new SharedReference(edmObj));
    }

    private boolean isLocal(String uri) {
        return ( uri.startsWith("/") || uri.startsWith("#") );
    }
}