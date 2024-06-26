/**
 * 
 */
package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.morphia.annotations.Entity;
import eu.europeana.api.edm.RDF;
import eu.europeana.api.record.model.data.SharedObject;

/**
 * @author Hugo
 * @since 1 Sep 2023
 */
@Entity(discriminatorKey = RDF.type)
public interface EDMClass extends SharedObject
{
    public String getID();

    @JsonProperty(RDF.type)
    public String getType();
}
