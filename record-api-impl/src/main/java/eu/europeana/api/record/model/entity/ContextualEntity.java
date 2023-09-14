package eu.europeana.api.record.model.entity;

import com.fasterxml.jackson.annotation.*;
import dev.morphia.annotations.*;
import eu.europeana.api.record.model.EDMClass;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.internal.LanguageMap;
import org.bson.types.ObjectId;

import java.util.List;

import static eu.europeana.api.record.vocabulary.RecordFields.*;

@Entity(value = "ContextualEntity", discriminator = "ContextualEntity", discriminatorKey = "type")
public abstract class ContextualEntity implements EDMClass {
    @Id
    protected ObjectId    objID;

    @JsonProperty(ID)
    @Indexed(options = @IndexOptions(name="idx_id", unique = true))
    @Property(ID)
    protected String id;

    @JsonProperty(PREF_LABEL)
    protected LanguageMap prefLabel = null;

    public ContextualEntity() {}

    public ContextualEntity(String id)
    {
        this.id = id;
    }

    public ObjectId getObjectID()
    {
        return objID;
    }

    public String getID()
    {
        return id;
    }

    public List<Literal<String>> getPrefLabels()
    {
        return getPrefLabel().getValues();
    }

    public void addPrefLabel(Literal<String> label)
    {
        getPrefLabel().add(label);
    }


    private LanguageMap getPrefLabel()
    {
        if ( this.prefLabel == null ) { this.prefLabel = new LanguageMap(); }
        return this.prefLabel;
    }
}

