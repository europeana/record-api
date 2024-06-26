/**
 * 
 */
package eu.europeana.jena.encoder.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(value = { FIELD, METHOD} )
public @interface JenaProperty
{
    public String ns();

    public String localName();

    /*
     * Indicates whether the statement should have as subject the object where
     * this annotation is attached (value=false) or it should be instead 
     * the target (value=true)
     */
    public boolean inverse() default false;
}
