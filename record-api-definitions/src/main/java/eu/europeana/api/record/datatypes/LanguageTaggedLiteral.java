package eu.europeana.api.record.datatypes;

public interface LanguageTaggedLiteral<T> extends Literal<T> {

    T getValue();

    void setValue(T value);

    String getLanguage();

    void setLanguage(String lang);

}
