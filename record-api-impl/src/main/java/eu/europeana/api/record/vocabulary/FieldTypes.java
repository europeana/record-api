package eu.europeana.api.record.vocabulary;

public enum FieldTypes {
    Proxy("Proxy"),
    Aggregation("Aggregation"),
    EuropeanaAggregation("EuropeanaAggregation"),
    WebResource("WebResource");

    private String fieldType;

    public String getFieldType() {
        return fieldType;
    }


    FieldTypes(String fieldType) {
        this.fieldType = fieldType;

    }

}
