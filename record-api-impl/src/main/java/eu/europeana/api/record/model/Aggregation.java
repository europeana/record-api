package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.*;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import static eu.europeana.api.record.vocabulary.RecordFields.VIEWS;

import java.util.ArrayList;
import java.util.List;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

@Entity(discriminator = "Aggregation", discriminatorKey = "type")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true, property = "type")
@JsonPropertyOrder({ID, TYPE, IS_SHOWN_BY, IS_SHOWN_AT, VIEWS})
public class Aggregation implements EDMClass {

    @JsonProperty(ID)
    @Property(ID)
    private String      id;

    @JsonProperty(IS_SHOWN_AT)
    @Property(IS_SHOWN_AT)
    private WebResource isShownAt;

    @JsonProperty(IS_SHOWN_BY)
    @Property(IS_SHOWN_BY)
    private WebResource isShownBy;

    @JsonProperty(VIEWS)
    @Property(VIEWS)
    private List<WebResource> hasViews = new ArrayList();

    public Aggregation() {
    }

    public Aggregation(String id) {
        this.id = id;
    }

    @Override
    public String getID() {
        return this.id;
    }

    @Override
    public String getType() {
        return "Aggregation";
    }

    public WebResource getIsShownAt() {
        return isShownAt;
    }

    public WebResource getIsShownBy() {
        return isShownBy;
    }

    public List<WebResource> getViews() {
        return hasViews;
    }

    public void setIsShownAt(WebResource isShownAt) {
        this.isShownAt = isShownAt;
    }

    public void setIsShownBy(WebResource isShownBy) {
        this.isShownBy = isShownBy;
    }


}
