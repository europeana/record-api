package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.*;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import eu.europeana.api.record.model.media.TechnicalMetadata;
import static eu.europeana.api.record.vocabulary.RecordFields.MONGO_TECHMETA;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

@Entity(discriminator = "WebResource", discriminatorKey = "type")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true, property = "type")
@JsonPropertyOrder({ID, TYPE })
public class WebResource implements EDMClass {

    @JsonProperty(ID)
    @Property(ID)
    private String            id;

    @JsonUnwrapped
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

    @Override
    public String getType() {
       return "WebResource";
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
