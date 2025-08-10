/**
 * 
 */
package eu.europeana.api.record.model.data;

import dev.morphia.annotations.Entity;
import eu.europeana.api.edm.RDF;

/**
 * @author Hugo
 * @since 17 Jun 2024
 */
@Entity(discriminatorKey = RDF.type)
public interface SharedObject
{
    public String getID();
}
