package eu.europeana.jena.encoder;

import eu.europeana.api.record.model.data.DataValueFactory;
import eu.europeana.jena.encoder.codec.JenaCodec;
import eu.europeana.jena.encoder.library.ClassTemplate;
import eu.europeana.jena.encoder.library.ClassTemplate.FieldDefinition;
import eu.europeana.jena.encoder.library.TemplateLibrary;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.RDF;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * @author Hugo
 * @since 10 Oct 2023
 */
public class JenaObjectDecoder
{
    private HashMap<String,Object> cache;
    private TemplateLibrary        library;
    protected DataValueFactory factory;
    private String                 base;
    private boolean                makeRelative;

    public JenaObjectDecoder(TemplateLibrary library
            , DataValueFactory factory, boolean makeRelative) {
        this.library      = library;
        this.factory      = factory;
        this.makeRelative = makeRelative;
        cache = new HashMap<String,Object>();
    }

    public JenaObjectDecoder(TemplateLibrary library, DataValueFactory factory) {
        this(library, factory, true);
    }

    public Object decode(Resource r) {
        return decode(r, makeRelative ? r.getURI() : null);
    }

    public Object decode(Resource r, String base) {
        try {
            this.base = base;
            return decodeResource(r);
        }
        finally {
            cache.clear();
        }
    }

    public Collection<Object> decodeAll(Model m) {
        Collection<Object> ret = new ArrayList<Object>();
        DecoderContext context = new DecoderContext();
        StmtIterator iter = m.listStatements(null, RDF.type, (RDFNode)null);
        try {
            while ( iter.hasNext() )
            {
                Statement stmt  = iter.next();
                RDFNode   value = stmt.getObject();
                if ( !value.isResource() ) { continue; }

                ClassTemplate template = library.getTemplateByType(value.asResource());
                if ( template == null ) { continue; }

                context.resource = stmt.getSubject();
                Object obj = decodeByTemplate(template, context);
                if ( obj != null ) { ret.add(obj); }
            }
            return ret;
        }
        finally {
            cache.clear();
            iter.close();
        }
    }

    /*
     * Decodes an object out of a rdf:Resource.
     *
     * The rdf:type of the resource will dictacte which class template will be
     * used to decode the object.
     */
    protected Object decodeResource(Resource r) {
        StmtIterator iter = r.listProperties(RDF.type);
        try {
            while ( iter.hasNext() ) {
                RDFNode node = iter.next().getObject();
                if ( !node.isResource() ) { continue; }

                ClassTemplate template = library.getTemplateByType(node.asResource());
                if ( template == null ) { continue; }

                return decodeByTemplate(template, new DecoderContext(r));
            }
            return null;
        }
        finally {
            iter.close();
        }
    }

    /*
     * The value in the argument vs the context.resource may not match especially
     * in transitive cases
     *

    needs to cover both cases of a List<Proxy> or  List<DataValue>

     */

    protected Object decodeByAny(RDFNode value, Class<?> clazz
            , DecoderContext context) {
        if ( value.isResource() ) {
            Resource r = value.asResource();

            ClassTemplate template = getTemplateForResource(r);
            if ( template == null || !template.acceptsClass(clazz) ) {
                return decodeByClass(value, clazz, context, false);
            }

            Object obj = decodeByTemplate(template, new DecoderContext(r));
            if ( obj != null ) { return obj; }
        }
        return decodeByClass(value, clazz, context, true);
    }

    /*
     * Decodes a RDFNode value based on a object class
     */
    // to avoid having to create multiple nesting of contexts,
    // the function needs to receive the value
    // this is important when there is a chain of java classes
    protected Object decodeByClass(RDFNode value, Class<?> clazz
            , DecoderContext context, boolean recursively) {
        JenaCodec<?> codec = ( recursively ? library.getCodecRecursively(clazz)
                : library.getCodec(clazz) );
        if ( codec != null ) { return codec.decode(value, context); }

        if ( value.isResource() ) {
            context = context.newContext(value.asResource());
            ClassTemplate template = library.getTemplateByClass(clazz);
            return ( template == null ? null : decodeByTemplate(template, context) );
        }
        return null;
    }

    /*
     * Decodes an object based on a class template.
     *
     * A class template is obtained from all classes that are annotated
     * with @JenaResource.
     */
    protected Object decodeByTemplate(ClassTemplate template, DecoderContext context) {
        Object o;
        Field id = template.getId();
        if ( id != null ) {
            String uri = compactUri(context.resource.getURI());
            // check the cache to see if the resource has previously been decoded
            o = cache.get(uri);
            if ( o != null ) { return o; }

            o = template.newObject();
            setValue(id, o, uri);
            // add to the cache to avoid decoding the resource multiple times
            cache.put(uri, o);
        }
        else { o = template.newObject(); }

        for ( FieldDefinition field : template.getFields() ) {
            context.field       = field;
            context.currentType = field.getField().getGenericType();
            Object value = decodeField(context);
            if ( value != null ) { setValue(field.getField(), o, value); }
        }
        return o;
    }

    protected String compactUri(String uri) {
        return library.getUriNormalizer().compact(uri, base);
    }

    private ClassTemplate getTemplateForResource(Resource r) {
        StmtIterator iter = r.listProperties(RDF.type);
        try {
            while ( iter.hasNext() ) {
                RDFNode node = iter.next().getObject();
                if ( !node.isResource() ) { continue; }

                return library.getTemplateByType(node.asResource());
            }
            return null;
        }
        finally {
            iter.close();
        }
    }

    private Object decodeField(DecoderContext context)
    {
        FieldDefinition field = context.field;
        context.property = ( field.hasPropertyDefinition() ?
                field.getPropertyDefinition().getProperty() : null );

        if (field.isCollection()) {
            JenaCodec codec = getCodecForField(field);
            return ( codec == null ? null : codec.decode(null, context) );
        }

        Property property = context.getProperty();
        if ( property == null ) {
            JenaCodec codec = getCodecForField(field);
            if ( codec != null ) { return codec.decode(context.getResource(), context); }

            ClassTemplate template = library.getTemplateByClass(field.getField().getType());
            if ( template != null ) { return decodeByTemplate(template, context); }

            return null;
        }

        StmtIterator iter = context.getResource().listProperties(property);
        try {
            if ( !iter.hasNext() ) { return null; }

            RDFNode value = iter.next().getObject();
            if ( value.isResource() ) { context = context.newContext(value.asResource()); }

            JenaCodec codec = getCodecForField(field);
            if ( codec != null ) { return codec.decode(value, context); }

            if ( value.isResource() ) {
                ClassTemplate template = library.getTemplateForResource(value.asResource());
                if ( template == null ) {
                    System.out.println("Untyped resource: " + value);
                }
                return (template != null ? decodeByTemplate(template, context) : null);
            }
        }
        finally {
            iter.close();
        }

        return null;
    }

    private JenaCodec getCodecForField(FieldDefinition field) {
        JenaCodec codec = field.getCodec();
        if ( codec != null ) { return codec; }

        Class<?> clazz = field.getField().getType();
        return library.getCodecRecursively(clazz);
    }

    private void setValue(Field field, Object o, Object value)
    {
        try {
            field.set(o, value);
        }
        catch (IllegalArgumentException | IllegalAccessException e) {
            throw new JenaDecoderException(e);
        }
    }


    /*
     *
     */
    public class DecoderContext extends AbsContext
    {
        private Type currentType;

        protected DecoderContext() {}

        protected DecoderContext(Resource resource) { super(resource); }

        public DataValueFactory getFactory() {
            return JenaObjectDecoder.this.factory;
        }

        public DecoderContext newContext(Resource r) {
//            if ( this.resource.equals(r) ) { System.out.println("equal"); }
//            return (this.resource.equals(r) ? this : new DecoderContext(r));
            return new DecoderContext(r);
        }

        public Type getCurrentType() { return currentType; }

        public <T> Class<T> getCurrentTypeAsClass() {
            return (Class<T>)( currentType instanceof ParameterizedType ?
                    ((ParameterizedType)currentType).getRawType()
                    : currentType );
        }

        public void setCurrentType(Type type) { currentType = type; }

        public TemplateLibrary getLibrary() { return library; }

        public Model getModel() { return this.resource.getModel(); }

        public String compactUri(String uri) {
            return JenaObjectDecoder.this.compactUri(uri);
        }

        public <T> T decodeByAny(RDFNode value, Class<T> clazz)
        {
            return (T)JenaObjectDecoder.this.decodeByAny(value, clazz, this);
        }

        public <T> T decodeByClass(RDFNode value, Class<T> clazz, boolean recursively) {
            return (T)JenaObjectDecoder.this.decodeByClass(value, clazz, this, recursively);
        }

        public <T> T decodeByClass(RDFNode value, Class<T> clazz, Type type, boolean recursively) {
            currentType = type;
            return (T)JenaObjectDecoder.this.decodeByClass(value, clazz, this, recursively);
        }

        public <T> T decodeByTemplate(ClassTemplate template)
        {
            return (T)JenaObjectDecoder.this.decodeByTemplate(template, this);
        }

        public Object decodeResource(Resource r) {
            return JenaObjectDecoder.this.decodeResource(r);
        }
    }
}
