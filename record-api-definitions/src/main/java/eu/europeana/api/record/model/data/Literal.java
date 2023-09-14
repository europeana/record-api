package eu.europeana.api.record.model.data;

public interface Literal<T>  {

    T getValue();

    void setValue(T value);
}
