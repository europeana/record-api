package eu.europeana.api.record.model.data;

public interface Literal<T> extends DataValue {

    T getValue();

    void setValue(T value);
}
