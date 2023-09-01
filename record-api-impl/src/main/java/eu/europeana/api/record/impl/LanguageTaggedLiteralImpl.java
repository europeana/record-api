package eu.europeana.api.record.impl;

import eu.europeana.api.record.datatypes.LanguageTaggedLiteral;

public class LanguageTaggedLiteralImpl<T> implements LanguageTaggedLiteral<T> {

    private T value;

    private String language;

    public LanguageTaggedLiteralImpl(T value, String language) {
        this.value = value;
        this.language = language;
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
    public String getLanguage() {
        return language;
    }

    @Override
    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "LanguageTaggedLiteralImpl{" +
                "value=" + value +
                ", language='" + language + '\'' +
                '}';
    }
}
