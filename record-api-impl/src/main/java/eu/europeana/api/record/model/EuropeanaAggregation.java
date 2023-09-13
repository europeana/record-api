package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import dev.morphia.annotations.Entity;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true, property = "type")
@Entity(discriminator = "EuropeanaAggregation", discriminatorKey = "type")
public class EuropeanaAggregation extends Aggregation {

    public String getType() { return "EuropeanaAggregation"; }

    public EuropeanaAggregation(String id) {
        super(id);
    }

    public EuropeanaAggregation() {

    }

}
