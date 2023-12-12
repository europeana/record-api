/**
 * 
 */
package eu.europeana.jena.encoder.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({ FIELD, TYPE })
public @interface JenaCodec
{
    public Class<?> using();
}
