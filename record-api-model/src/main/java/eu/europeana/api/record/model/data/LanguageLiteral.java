package eu.europeana.api.record.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.morphia.annotations.Property;
import eu.europeana.api.record.model.ModelConstants;


//@Entity(useDiscriminator = false)
public class LanguageLiteral extends Literal<String> {

    @JsonProperty(ModelConstants.language)
    @Property(ModelConstants.lang)
    protected String lang;

    public LanguageLiteral() {}

    public LanguageLiteral(String value, String lang) {
        this.value = value;
        this.lang  = lang;
    }

    public String getLanguage() { return this.lang; }

    public String toString() { return ('"' + this.value + "\"@" + this.lang); }
}

