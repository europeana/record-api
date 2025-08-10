package eu.europeana.api.record.io.jena;

import dev.morphia.mapping.codec.references.MorphiaProxy;
import eu.europeana.api.record.model.EDMClass;
import eu.europeana.api.record.model.data.ObjectReference;
import eu.europeana.api.record.model.data.SharedObject;
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

        //Note: when the cho is retrieved, the object reference comes with a 
        //morphia proxy, however, the entity that is retrieved via lazy loading
        //does not create a morphia proxy for any object reference it may contain.
        //This requires further investigation to understand why!
        //Good example to test this is: /221/URN_NBN_SI_DOC_0CEKTP4N
        if ( ref.isDereferenced() ) {
            //TODO: review how to best work with lazy loading
            SharedObject o = ref.getDereferencedObject();
            // the object may be null when it has not been stored before and 
            // should be covered on the dereferenced
            if ( o != null ) { 
                context.process(o);
                return;
            }
        }

        //Since there is not object and therefore no handler, the reference 
        //still needs to be created and associated to the source object.
        String uri = context.expandUri(ref.getID());
        context.getResource().addProperty(context.getProperty()
                                        , m.createResource(uri));
    }

    @Override
    public ObjectReference decode(RDFNode node, DecoderContext context) {
        if ( !node.isResource() ) { return null; }

        Resource r   = node.asResource();
        String   uri = context.compactUri(r.getURI());
        if ( !r.hasProperty(RDF.type) ) { return context.getFactory().newReference(uri); }

        Object   obj    = context.decodeResource(r);
        if ( obj != null && obj instanceof EDMClass ) {
            return context.getFactory().newReference((EDMClass)obj);
        }
        return context.getFactory().newReference(uri);
    }

    private boolean isLocal(String uri) {
        return ( uri.startsWith("/") || uri.startsWith("#") );
    }
}
