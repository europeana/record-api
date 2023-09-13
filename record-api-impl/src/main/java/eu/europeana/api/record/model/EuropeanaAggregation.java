package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import dev.morphia.annotations.Entity;

import static eu.europeana.api.record.vocabulary.RecordFields.TYPE;

@Entity(discriminator = "EuropeanaAggregation", discriminatorKey = "type")
public class EuropeanaAggregation extends Aggregation {

//    @JsonGetter(TYPE)
//    public String getType() {
//        return "EuropeanaAggregation";
//    }

}
