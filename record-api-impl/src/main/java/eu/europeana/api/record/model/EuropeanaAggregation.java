package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dev.morphia.annotations.Entity;
import eu.europeana.api.record.model.data.Literal;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

@Entity(discriminator = "EuropeanaAggregation", discriminatorKey = "type")
@JsonPropertyOrder({ID, TYPE})
public class EuropeanaAggregation extends Aggregation {

}
