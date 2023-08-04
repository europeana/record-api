package eu.europeana.api.record.impl;

import eu.europeana.api.record.datatypes.Literal;

public class WebResource extends EdmEntity {

    private Literal<String> type;

    public Literal<String> getType() {
        return type;
    }

    public void setType(Literal<String> type) {
        this.type = type;
    }
}
