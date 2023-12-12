/**
 * 
 */
package eu.europeana.jena.encoder.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(FIELD)
public @interface JenaProperty
{
    public String ns();

    public String localName();

    public boolean inverse() default false;
}
