package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import eu.europeana.api.record.model.media.TechnicalMetadata;
import static eu.europeana.api.record.vocabulary.RecordFields.MONGO_TECHMETA;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

@Entity(useDiscriminator = false)
@JsonPropertyOrder({ID, MIME_TYPE, FILE_BYTE_SIZE})
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class WebResource {

    @Property(ID)
    private String            id;

    @Property(MONGO_TECHMETA)
    private TechnicalMetadata techMeta;

    public WebResource() {}

    public WebResource(String id)
    {
        this.id = id;
    }

    public String getID()
    {
        return this.id;
    }

    @JsonUnwrapped
    public TechnicalMetadata getTechnicalMetadata()
    {
        return techMeta;
    }

    public boolean hasTechnicalMetadata()
    {
        return (techMeta != null);
    }

    public void setTechnicalMetadata(TechnicalMetadata techMeta)
    {
        this.techMeta = techMeta;
    }

}
