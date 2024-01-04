/**
 * 
 */
package eu.europeana.jena.encoder.codec;

import eu.europeana.jena.encoder.JenaObjectEncoder.EncoderContext;
import org.apache.jena.rdf.model.Model;

/**
 * @author Hugo
 * @since 12 Oct 2023
 */
public interface JenaEncoder<T>
{

    /**
     * Encode an instance of the type parameter {@code T} into a BSON value.
     * @param writer the BSON writer to encode into
     * @param value the value to encode
     * @param encoderContext the encoder context
     */
    void encode(Model m, T value, EncoderContext context);

    /**
     * Returns the Class instance that this encodes. This is necessary because Java does not reify generic types.
     *
     * @return the Class instance that this encodes.
     */
    Class<T> getSupportedClass();
}
