package eu.europeana.api.record.model;

import eu.europeana.api.record.model.data.Literal;

import java.util.List;
import java.util.Map;

public interface ContextualEntity{

    List<Literal<String>> getPrefLabels();

}
