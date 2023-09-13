package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.morphia.annotations.*;

@Entity(discriminatorKey = "type")
public interface EDMClass {
    public String getID();

    @JsonProperty("type")
    public String getType();
}
