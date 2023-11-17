package eu.europeana.api.record.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import static eu.europeana.api.record.model.ModelConstants.LANGUAGE;
import static eu.europeana.api.record.model.ModelConstants.LANG;


@Entity(useDiscriminator = false)
public class LanguageLiteral extends Literal<String> {

    @JsonProperty(LANGUAGE)
    @Property(LANG)
    protected String lang;

    public LanguageLiteral() {}

    public LanguageLiteral(String value, String lang) {
        this.value = value;
        this.lang  = lang;
    }

    public String getLanguage() { return this.lang; }

    public String toString() { return ('"' + this.value + "\"@" + this.lang); }
}

