package eu.europeana.api.record.model;

import eu.europeana.api.record.datatypes.Literal;

import java.util.Map;

public interface Entity {

    String getAbout();

    void setAbout(String about);

    Literal<String> getPrefLabel(String language);

    void setPrefLabel(Map<String, Literal<String>> prefLabel);
}
