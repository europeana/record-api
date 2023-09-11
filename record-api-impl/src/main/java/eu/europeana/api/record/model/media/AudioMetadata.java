/**
 * 
 */
package eu.europeana.api.record.model.media;

import dev.morphia.annotations.Entity;

/**
 * @author Hugo
 * @since 8 Aug 2023
 */
@Entity(discriminator = "Audio", discriminatorKey = "type")
public class AudioMetadata extends TechnicalMetadata {
    
}
