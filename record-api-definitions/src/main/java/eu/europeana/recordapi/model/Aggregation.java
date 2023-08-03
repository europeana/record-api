package eu.europeana.recordapi.model;

import eu.europeana.recordapi.datatypes.Literal;
import eu.europeana.recordapi.datatypes.ObjectReference;

import java.util.List;

public class Aggregation extends EdmEntity {

    private Literal<String> type;
    private Literal<String> isShownBy;
    private List<ObjectReference> hasViews;  // multiple object refre

    public Literal<String> getType() {
        return type;
    }

    public void setType(Literal<String> type) {
        this.type = type;
    }

    public Literal<String> getIsShownBy() {
        return isShownBy;
    }

    public void setIsShownBy(Literal<String> isShownBy) {
        this.isShownBy = isShownBy;
    }

    public List<ObjectReference> getHasViews() {
        return hasViews;
    }

    public void setHasViews(List<ObjectReference> hasViews) {
        this.hasViews = hasViews;
    }
}
