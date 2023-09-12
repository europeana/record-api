package eu.europeana.api.record.model;

import dev.morphia.annotations.Entity;

@Entity(discriminator = "EuropeanaAggregation", discriminatorKey = "type")
public class EuropeanaAggregation extends Aggregation {

}
