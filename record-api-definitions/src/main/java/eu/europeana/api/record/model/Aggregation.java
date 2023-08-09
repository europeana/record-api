package eu.europeana.api.record.model;

import eu.europeana.api.record.datatypes.Literal;
import eu.europeana.api.record.datatypes.ObjectReference;

import java.util.List;

public interface Aggregation {

    Literal<String> getType();

    Literal<String> getIsShownBy();

    List<ObjectReference> getHasViews();

    void setType(Literal<String> type);

    void setIsShownBy(Literal<String> isShownBy);

    void setHasViews(List<ObjectReference> hasViews);

}
