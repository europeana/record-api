package eu.europeana.jena.encoder.codec;

import eu.europeana.jena.encoder.JenaObjectDecoder.DecoderContext;
import eu.europeana.jena.encoder.JenaObjectEncoder.EncoderContext;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.Temporal;

/**
 * @author Hugo
 * @since 12 Oct 2023
 */
public abstract class JenaTimeCodec<T extends Temporal> implements JenaCodec<T>
{
    public static InstantCodec   INSTANT    = new InstantCodec();
    public static LocalDateCodec LOCAL_DATE = new LocalDateCodec();
    //public static IntegerCodec INTEGER = new IntegerCodec();
    //public static LongCodec    LONG    = new LongCodec();
    //public static FloatCodec   FLOAT   = new FloatCodec();
    //public static DoubleCodec  DOUBLE  = new DoubleCodec();

    public static void addToRegistry(CodecRegistry registry) {
        registry.addCodec(INSTANT);
        registry.addCodec(LOCAL_DATE);
//        registry.addCodec(INTEGER);
//        registry.addCodec(LONG);
//        registry.addCodec(FLOAT);
//        registry.addCodec(DOUBLE);
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


    public static class InstantCodec extends JenaTimeCodec<Instant> {

        @Override
        public Class<Instant> getSupportedClass() { return Instant.class; }

        public Instant parse(Object obj) {
            return ( obj instanceof Instant ? ((Instant)obj) 
                                            : Instant.parse(obj.toString()) );
        }
    }

    public static class LocalDateCodec extends JenaTimeCodec<LocalDate> {

        @Override
        public Class<LocalDate> getSupportedClass() { return LocalDate.class; }

        public LocalDate parse(Object obj) {
            return ( obj instanceof LocalDate ? ((LocalDate)obj)
                                              : LocalDate.parse(obj.toString()) );
        }
    }

    /*
    public static class IntegerCodec extends JenaTimeCodec<Integer> {

        @Override
        public Class<Integer> getSupportedClass() { return Integer.class; }

        public Integer parse(Object obj) {
            return ( obj instanceof Number ? ((Number)obj).intValue() 
                                           : Integer.parseInt(obj.toString()) );
        }
    }

    public static class LongCodec extends JenaTimeCodec<Long> {

        @Override
        public Class<Long> getSupportedClass() { return Long.class; }

        public Long parse(Object obj) {
            return ( obj instanceof Number ? ((Number)obj).longValue() 
                                           : Long.parseLong(obj.toString()) );
        }
    }

    public static class FloatCodec extends JenaTimeCodec<Float> {

        @Override
        public Class<Float> getSupportedClass() { return Float.class; }

        public Float parse(Object obj) {
            return ( obj instanceof Number ? ((Number)obj).floatValue() 
                                           : Float.parseFloat(obj.toString()) );
        }
    }

    public static class DoubleCodec extends JenaTimeCodec<Double> {
        
        @Override
        public Class<Double> getSupportedClass() { return Double.class; }

        @Override
        public Double parse(Object obj) {
            return ( obj instanceof Number ? ((Number)obj).doubleValue() 
                                           : Double.parseDouble(obj.toString()) );
        }
    }
    */
}