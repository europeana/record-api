package eu.europeana.api.record.datatypes;

public interface Literal<T> extends DataValue {

    T getValue();

    void setValue(T value);
}
