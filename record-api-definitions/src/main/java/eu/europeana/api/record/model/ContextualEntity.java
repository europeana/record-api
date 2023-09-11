package eu.europeana.api.record.model;

import eu.europeana.api.record.model.data.EDMObject;
import eu.europeana.api.record.model.data.Literal;

import java.util.List;
import java.util.Map;

public interface ContextualEntity extends EDMObject {

    List<Literal<String>> getPrefLabels();

}
