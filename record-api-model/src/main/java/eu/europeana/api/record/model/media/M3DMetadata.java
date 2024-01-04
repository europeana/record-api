/**
 * 
 */
package eu.europeana.api.record.model.media;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.morphia.annotations.Entity;
import eu.europeana.api.edm.RDF;
import eu.europeana.api.record.model.data.EdmType;
import eu.europeana.jena.encoder.annotation.JenaClass;

import static eu.europeana.api.record.model.ModelConstants._3D;

/**
 * @author Hugo
 * @since 8 Aug 2023
 */
@JenaClass
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@Entity(discriminator = _3D, discriminatorKey = RDF.type)
public class M3DMetadata extends TechnicalMetadata {
    
    public EdmType getType() { return EdmType._3D; }
}
