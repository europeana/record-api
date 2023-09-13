package eu.europeana.api.record.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import static eu.europeana.api.record.vocabulary.RecordFields.LANGUAGE;
import static eu.europeana.api.record.vocabulary.RecordFields.LANG;


@Entity(useDiscriminator = false)
public class LanguageTaggedLiteral extends Literal<String> {

    @JsonProperty(LANGUAGE)
    @Property(LANG)
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
