package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.*;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;

import java.util.ArrayList;
import java.util.List;

import static eu.europeana.api.record.vocabulary.RecordFields.ID;
import static eu.europeana.api.record.vocabulary.RecordFields.TYPE;

@Entity(discriminator = "Aggregation", discriminatorKey = "type")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ID, TYPE})
public class Aggregation {

    @Property("isShownAt")
    private WebResource isShownAt;

    @Property("isShownBy")
    private WebResource isShownBy;

    @Property("views")
    private List<WebResource> hasViews = new ArrayList();

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
