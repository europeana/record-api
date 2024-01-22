/**
 * 
 */
package eu.europeana.jena.encoder.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Inherited
@Retention(RUNTIME)
@Target(TYPE)
public @interface JenaResource
{
    public String ns();

    public String localName();
}
