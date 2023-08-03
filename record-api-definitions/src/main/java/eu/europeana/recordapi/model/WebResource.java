package eu.europeana.recordapi.model;

import eu.europeana.recordapi.datatypes.Literal;

public class WebResource extends EdmEntity {

    private Literal<String> type;

    public Literal<String> getType() {
        return type;
    }

    public void setType(Literal<String> type) {
        this.type = type;
    }
}
