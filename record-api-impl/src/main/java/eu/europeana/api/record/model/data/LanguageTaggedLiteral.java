package eu.europeana.api.record.model.data;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;

@Entity(useDiscriminator = false)
public class LanguageTaggedLiteral extends Literal<String> {

    @Property("lang")
    protected String lang;

    public LanguageTaggedLiteral() {
    }

    public LanguageTaggedLiteral(String value, String lang) {
        this.value = value;
        this.lang = lang;
    }

    public String getLanguage() {
        return this.lang;
    }

    public void setLanguage(String lang) {
        this.lang = lang;
    }
}
