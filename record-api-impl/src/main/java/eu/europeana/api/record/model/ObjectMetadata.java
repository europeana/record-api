package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import eu.europeana.api.record.model.data.DataValue;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.internal.LanguageMap;
import eu.europeana.api.record.model.internal.LanguageMapArray;

import java.util.ArrayList;
import java.util.List;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

/**
 * @author Hugo
 * @since 4 Aug 2023
 */
@Entity
public class ObjectMetadata {

    @Property(MONGO_TITLE)
    private LanguageMap title = null;

    @Property(MONGO_ALT_TITLE)
    private LanguageMapArray altTitle = null;

    @Property(MONGO_DESCRIPTION)
    private List<DataValue> description = null;

    @Property(MONGO_CREATOR)
    private List<DataValue> creator = new ArrayList();

    @Property(MONGO_IDENTIFIER)
    private List<Literal<String>> identifier = new ArrayList();

    @JsonGetter(TITLES)
    public List<Literal<String>> getTitles() {
        return getTitle().getValues();
    }

    public void addTitle(Literal<String> title) {
        getTitle().add(title);
    }

    @JsonGetter(ALTERNATIVE_TITLES)
    public List<Literal<String>> getAlternativeTitles() {
        return getAlternativeTitle().getValues();
    }

    public void addAlternativeTitle(Literal<String> title) {
        getAlternativeTitle().add(title);
    }

    @JsonGetter(DESCRIPTIONS)
    public List<DataValue> getDescriptions() {
        return getDescription();
    }

    public void addDescription(DataValue description) {
        getDescription().add(description);
    }

    @JsonGetter(CREATORS)
    public List<DataValue> getCreators() {
        return creator;
    }

    public void addCreator(DataValue creator) {
        this.creator.add(creator);
    }

    @JsonGetter(IDENTIFIERS)
    public List<Literal<String>> getIdentifiers() {
        return identifier;
    }

    public void addIdentifier(Literal<String> identifier) {
        this.identifier.add(identifier);
    }


    private LanguageMap getTitle() {
        if (this.title == null) {
            this.title = new LanguageMap();
        }
        return this.title;
    }

    private LanguageMapArray getAlternativeTitle() {
        if (this.altTitle == null) {
            this.altTitle = new LanguageMapArray();
        }
        return this.altTitle;
    }

    private List<DataValue> getDescription() {
        if (this.description == null) {
            this.description = new ArrayList();
        }
        return this.description;
    }
}