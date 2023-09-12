package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.*;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import static eu.europeana.api.record.vocabulary.RecordFields.MONGO_VIEWS;

import java.util.ArrayList;
import java.util.List;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

@Entity(discriminator = "Aggregation", discriminatorKey = "type")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({IS_SHOWN_BY, IS_SHOWN_AT, HAS_VIEWS})
public class Aggregation {

    @Property(IS_SHOWN_AT)
    private WebResource isShownAt;

    @Property(IS_SHOWN_BY)
    private WebResource isShownBy;

    @Property(MONGO_VIEWS)
    private List<WebResource> hasViews = new ArrayList();

    @JsonGetter(IS_SHOWN_AT)
    public WebResource getIsShownAt() {
        return isShownAt;
    }

    @JsonGetter(IS_SHOWN_BY)
    public WebResource getIsShownBy() {
        return isShownBy;
    }

    @JsonGetter(HAS_VIEWS)
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
