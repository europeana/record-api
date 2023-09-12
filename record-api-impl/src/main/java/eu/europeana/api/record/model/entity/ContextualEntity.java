package eu.europeana.api.record.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dev.morphia.annotations.Entity;
import eu.europeana.api.record.model.EDMObject;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.internal.LanguageMap;

import java.util.List;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

@Entity(value = "ContextualEntity", discriminator = "ContextualEntity", discriminatorKey = "type")
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ID, PREF_LABELS})
public abstract class ContextualEntity extends EDMObject {

    protected LanguageMap prefLabel = null;

    public ContextualEntity() {
    }

    public ContextualEntity(String id) {
        this.id = id;
    }

    @JsonGetter(PREF_LABELS)
    public List<Literal<String>> getPrefLabels() {
        return getPrefLabel().getValues();
    }

    public void addPrefLabel(Literal<String> label) {
        getPrefLabel().add(label);
    }

    private LanguageMap getPrefLabel() {
        if (this.prefLabel == null) {
            this.prefLabel = new LanguageMap();
        }
        return this.prefLabel;
    }
}
