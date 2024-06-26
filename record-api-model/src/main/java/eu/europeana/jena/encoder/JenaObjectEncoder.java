package eu.europeana.jena.encoder;

import eu.europeana.api.record.model.EDMClass;
import eu.europeana.jena.encoder.codec.JenaCodec;
import eu.europeana.jena.encoder.library.ClassTemplate;
import eu.europeana.jena.encoder.library.ClassTemplate.FieldDefinition;
import eu.europeana.jena.encoder.library.ClassTemplate.PropertyDefinition;
import eu.europeana.jena.encoder.library.ClassTemplate.ReflectionDefinition;
import eu.europeana.jena.encoder.library.ClassTemplate.ResourceDefinition;
import eu.europeana.jena.encoder.library.TemplateLibrary;
import eu.europeana.jena.encoder.utils.JenaUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;

import java.lang.reflect.Field;
import java.util.HashSet;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * @author Hugo
 * @since 10 Oct 2023
 */
@NotThreadSafe
public class JenaObjectEncoder<T extends EDMClass>
{
    private Model             m;
    private HashSet<Resource> cache;
    private TemplateLibrary   library;
    private String            base;

    public JenaObjectEncoder(TemplateLibrary library) {
        this.library      = library;
        cache = new HashSet<Resource>();
    }

    public Model encode(T o) {
        return encode(o, ModelFactory.createDefaultModel());
    }

    public Model encode(T o, Model m) {
        return encode(o, m, o.getID() + "/");
    }

    public Model encode(T o, String base) {
        return encode(o, ModelFactory.createDefaultModel(), base);
    }

    public Model encode(T o, Model m, String base) {
        try {
            this.base = base;
            this.m = m;
            encodeObject(o, new EncoderContext(null));
        }
        finally {
            cache.clear();
        }
        return m;
    }


    /*
     * The template takes precedence over any codec
     */
    protected void encodeObject(Object o, EncoderContext context) {
        if ( o == null ) { return; }

        Class<?>      clazz    = o.getClass();
        ClassTemplate template = library.getTemplateByClassRecursively(clazz);

        JenaCodec codec = ( template != null ? template.getCodec()
                : library.getCodecRecursively(clazz) );
        if ( codec != null ) { codec.encode(m, o, context); }
        else {
            System.err.println("Missing codec: " + o);
        }
    }

    protected void encodeByTemplate(Object o, ClassTemplate template
            , EncoderContext context) {
        if ( o == null ) { return; }

        ResourceDefinition type = template.getType();
        if ( type != null ) {
            Resource r = createResource(template, o);

            if ( context.resource != null && context.property != null
                    && !context.getPropertyDefinition().isInverse()) {
                context.resource.addProperty(context.property, r);
            }

            if ( cache.contains(r) ) { return; }
            cache.add(r);

            r.addProperty(RDF.type, type.getResource());
            JenaUtils.setNamespace(m, type.getNamespace());

            context = context.newContext(r);
        }
        else {
            if ( context.resource == null ) { return; }
        }

        for ( ReflectionDefinition definition : template.getDefinitions() ) {

            if ( !definition.acceptsRead() ) { continue; }

            PropertyDefinition propDefinition = definition.getPropertyDefinition();
            if ( propDefinition != null ) {
                context.property   = propDefinition.getProperty();
                context.definition = definition;
                JenaUtils.setNamespace(m, propDefinition.getNamespace());
            }

            Object    value = definition.getValue(o);
            JenaCodec codec = definition.getCodec();
            if ( codec != null ) {
                codec.encode(m, value, context);
                continue;
            }
            encodeObject(value, context);
        }
    }

    private Resource createResource(ClassTemplate template, Object o) {
        String id = template.getId().getValue(o);
        return (id == null ? m.createResource()
                : m.createResource(library.getUriNormalizer().expand(id, base)));
    }

    private String expandUri(String uri) {
        return library.getUriNormalizer().expand(uri, base);
    }



    public class EncoderContext extends AbsContext {

        public EncoderContext(Resource resource) { super(resource); }


        public TemplateLibrary getLibrary() { return library; }

        public void process(Object o) {
            encodeObject(o, this);
        }

        public void process(ClassTemplate template, Object o) {
            encodeByTemplate(o, template, this);
        }

        protected EncoderContext newContext(Resource resource) {
            return new EncoderContext(resource);
        }

        public String expandUri(String uri) {
            return JenaObjectEncoder.this.expandUri(uri);
        }
    }
}
