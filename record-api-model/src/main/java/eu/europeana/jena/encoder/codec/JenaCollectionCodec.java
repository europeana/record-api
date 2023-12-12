/**
 * 
 */
package eu.europeana.jena.encoder.codec;

import eu.europeana.jena.encoder.JenaObjectDecoder.DecoderContext;
import eu.europeana.jena.encoder.JenaObjectEncoder.EncoderContext;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.StmtIterator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Hugo
 * @since 12 Oct 2023
 */
public class JenaCollectionCodec implements JenaCodec<Collection>
{
    public static JenaCollectionCodec INSTANCE = new JenaCollectionCodec();

    @Override
    public Class<Collection> getSupportedClass() { return Collection.class; }

    @Override
    public void encode(Model m, Collection value, EncoderContext context)
    {
        for ( Object v : value ) { context.process(v); }
    }

    @Override
    public Collection decode(RDFNode value, DecoderContext context) {
        if ( !context.hasProperty() ) { return null; }

        return ( context.getPropertyDefinition().isInverse() 
               ? processInverse(context, null)
               : process(context, null) );
    }

    private Collection process(DecoderContext context, Collection col) {

        StmtIterator iter = context.getResource().listProperties(context.getProperty());
        if ( !iter.hasNext() ) { return null; }

        Type     type  = getGenericType(context.getCurrentType());
        Class<?> clazz = getClassFromType(type);
        
        Collection ret = new ArrayList();
        while ( iter.hasNext() )
        {
            context.setCurrentType(type);
            Object v = context.decodeByAny(iter.next().getObject(), clazz);
            if ( v != null ) { ret.add(v); }
        }
        return ret;
    }

    private Collection processInverse(DecoderContext context, Collection col) {

        ResIterator iter = context.getModel()
                                  .listResourcesWithProperty(context.getProperty()
                                                           , context.getResource());
        if ( !iter.hasNext() ) { return null; }

        Type     type  = getGenericType(context.getCurrentType());
        Class<?> clazz = getClassFromType(type);
        
        Collection ret = new ArrayList();
        while ( iter.hasNext() )
        {
            context.setCurrentType(type);
            Object v = context.decodeByAny(iter.next(), clazz);
            if ( v != null ) { ret.add(v); }
        }
        return ret;
    }

    private Type getGenericType(Type type) {
        ParameterizedType ptype = (ParameterizedType)type;
        return ptype.getActualTypeArguments()[0];
    }

    private Class<?> getClassFromType(Type type) {
        return (Class<?>)( type instanceof ParameterizedType ? 
                ((ParameterizedType)type).getRawType()
                : type );
    }
}
