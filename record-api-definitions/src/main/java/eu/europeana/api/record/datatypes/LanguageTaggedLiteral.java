package eu.europeana.api.record.datatypes;

public interface LanguageTaggedLiteral<T> extends Literal<T> {

    T getValue();
    String getLang();

}