package eu.europeana.api.record.model.media;

import com.fasterxml.jackson.annotation.JsonInclude;
import dev.morphia.annotations.Entity;


@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@Entity(discriminator = "Audio", discriminatorKey = "type")
public class AudioMetadata extends TechnicalMetadata {
    
}
