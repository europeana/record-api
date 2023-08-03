package eu.europeana.recordapi.datatypes;

public class LanguageTaggedLiteral implements Literal<String> {

    private Literal value;
    private String lang;

    @Override
    public String getValue() {
        return String.valueOf(value);
    }

    public void setValue(Literal<String> value) {
        this.value = value;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
