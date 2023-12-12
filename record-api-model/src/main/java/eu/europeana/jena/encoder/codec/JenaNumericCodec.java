/**
 * 
 */
package eu.europeana.jena.encoder.codec;

import eu.europeana.jena.encoder.JenaObjectDecoder.DecoderContext;
import eu.europeana.jena.encoder.JenaObjectEncoder.EncoderContext;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;

/**
 * @author Hugo
 * @since 12 Oct 2023
 */
public abstract class JenaNumericCodec<T extends Number> implements JenaCodec<T>
{
    public static ByteCodec    BYTE    = new ByteCodec();
    public static ShortCodec   SHORT   = new ShortCodec();
    public static IntegerCodec INTEGER = new IntegerCodec();
    public static LongCodec    LONG    = new LongCodec();
    public static FloatCodec   FLOAT   = new FloatCodec();
    public static DoubleCodec  DOUBLE  = new DoubleCodec();

    public static void addToRegistry(CodecRegistry registry) {
        registry.addCodec(BYTE);
        registry.addCodec(SHORT);
        registry.addCodec(INTEGER);
        registry.addCodec(LONG);
        registry.addCodec(FLOAT);
        registry.addCodec(DOUBLE);
    }

    @Override
    public void encode(Model m, T value, EncoderContext context) {
        Property property = context.getProperty();
        if ( property == null ) { return; }
        context.getResource().addLiteral(property, value.toString());
    }

    @Override
    public T decode(RDFNode node, DecoderContext context) {
        return ( !node.isLiteral() ? null : parse(node.asLiteral().getValue()) );
    }

    protected abstract T parse(Object obj);


    public static class ByteCodec extends JenaNumericCodec<Byte> {

        @Override
        public Class<Byte> getSupportedClass() { return Byte.class; }

        public Byte parse(Object obj) {
            return ( obj instanceof Number ? ((Number)obj).byteValue() 
                                           : Byte.parseByte(obj.toString()) );
        }
    }

    public static class ShortCodec extends JenaNumericCodec<Short> {

        @Override
        public Class<Short> getSupportedClass() { return Short.class; }

        public Short parse(Object obj) {
            return ( obj instanceof Number ? ((Number)obj).shortValue() 
                                           : Short.parseShort(obj.toString()) );
        }
    }

    public static class IntegerCodec extends JenaNumericCodec<Integer> {

        @Override
        public Class<Integer> getSupportedClass() { return Integer.class; }

        public Integer parse(Object obj) {
            return ( obj instanceof Number ? ((Number)obj).intValue() 
                                           : Integer.parseInt(obj.toString()) );
        }
    }

    public static class LongCodec extends JenaNumericCodec<Long> {

        @Override
        public Class<Long> getSupportedClass() { return Long.class; }

        public Long parse(Object obj) {
            return ( obj instanceof Number ? ((Number)obj).longValue() 
                                           : Long.parseLong(obj.toString()) );
        }
    }

    public static class FloatCodec extends JenaNumericCodec<Float> {

        @Override
        public Class<Float> getSupportedClass() { return Float.class; }

        public Float parse(Object obj) {
            return ( obj instanceof Number ? ((Number)obj).floatValue() 
                                           : Float.parseFloat(obj.toString()) );
        }
    }

    public static class DoubleCodec extends JenaNumericCodec<Double> {
        
        @Override
        public Class<Double> getSupportedClass() { return Double.class; }

        @Override
        public Double parse(Object obj) {
            return ( obj instanceof Number ? ((Number)obj).doubleValue() 
                                           : Double.parseDouble(obj.toString()) );
        }
    }
}