package eu.europeana.api.record.model;

import eu.europeana.api.record.datatypes.Literal;

public interface WebResource {

    Literal<String> getType();

    void setType(Literal<String> type);

    TechMetadata getTechMetadata();

    void setTechMetadata(TechMetadata techMetadata);
}
