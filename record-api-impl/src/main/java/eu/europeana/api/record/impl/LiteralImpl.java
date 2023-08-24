package eu.europeana.api.record.impl;

import eu.europeana.api.record.datatypes.Literal;

public class LiteralImpl<T> implements Literal<T> {

    private T value;

    public LiteralImpl(T value) {
        this.value = value;
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LiteralImpl{" +
                "value=" + value +
                '}';
    }
}
