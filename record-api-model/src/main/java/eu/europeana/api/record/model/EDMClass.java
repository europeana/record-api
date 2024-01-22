/**
 * 
 */
package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.morphia.annotations.Entity;
import eu.europeana.api.edm.RDF;

/**
 * @author Hugo
 * @since 1 Sep 2023
 */
@Entity(discriminatorKey = RDF.type)
public interface EDMClass
{
    public String getID();

    @JsonProperty(RDF.type)
    public String getType();
}
