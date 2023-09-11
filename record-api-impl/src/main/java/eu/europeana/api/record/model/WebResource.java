package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import eu.europeana.api.record.model.media.TechnicalMetadata;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

@Entity(useDiscriminator = false)
@JsonPropertyOrder({ID, TYPE})
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class WebResource {

    @Property("id")
    private String            id;

    @Property("techMeta")
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
