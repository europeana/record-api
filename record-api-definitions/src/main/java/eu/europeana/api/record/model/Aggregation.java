package eu.europeana.api.record.model;

import eu.europeana.api.record.datatypes.Literal;
import eu.europeana.api.record.datatypes.ObjectReference;

import java.util.List;

public interface Aggregation {

    Literal<String> getType();

    Literal<String> getIsShownBy();

    List<ObjectReference> getHasView();

    void setType(Literal<String> type);

    void setIsShownBy(Literal<String> isShownBy);

    void setHasView(List<ObjectReference> hasView);

}
