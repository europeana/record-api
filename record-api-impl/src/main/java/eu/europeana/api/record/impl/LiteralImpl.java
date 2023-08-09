package eu.europeana.api.record.impl;

public class LiteralImpl<T> implements eu.europeana.api.record.datatypes.Literal<T> {

    private T value;

    public LiteralImpl() {
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }
}
