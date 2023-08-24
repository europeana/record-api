package eu.europeana.api.record.datatypes;

public interface Literal<T> {

    T getValue();

    void setValue(T value);
}
