package eu.europeana.api.record.model.entity;

import com.fasterxml.jackson.annotation.*;
import dev.morphia.annotations.*;
import eu.europeana.api.edm.RDF;
import eu.europeana.api.edm.SKOS;
import eu.europeana.api.record.model.EDMClass;
import eu.europeana.api.record.model.ModelConstants;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.internal.LanguageMap;
import eu.europeana.api.record.model.internal.LanguageMapArray;
import eu.europeana.jena.encoder.annotation.JenaCollection;
import eu.europeana.jena.encoder.annotation.JenaId;
import eu.europeana.jena.encoder.annotation.JenaProperty;
import org.bson.types.ObjectId;

import java.util.List;

import static eu.europeana.api.record.model.ModelConstants.ContextualEntity;

@Entity(value = ContextualEntity, discriminator = ContextualEntity
        , discriminatorKey = RDF.type)
public abstract class ContextualEntity implements EDMClass
{
    @Id
    protected ObjectId    objID;

    @JenaId
    @JsonProperty(ModelConstants.id)
    @Indexed(options = @IndexOptions(name="idx_id", unique = true))
    @Property(ModelConstants.id)
    protected String id;

    @JenaProperty(ns = SKOS.NS, localName = SKOS.prefLabel)
    @JenaCollection
    @JsonProperty(SKOS.prefLabel)
    protected LanguageMap prefLabel = null;

    @JenaProperty(ns = SKOS.NS, localName = SKOS.altLabel)
    @JenaCollection
    @JsonProperty(SKOS.altLabel)
    @Property(SKOS.altLabel)
    protected LanguageMapArray altLabel = null;

    public ContextualEntity() {}

    public ContextualEntity(String id) { this.id = id; }

    public ObjectId getObjectID() { return objID; }

    public String getID() { return id; }

    public List<Literal<String>> getPrefLabels() {
        return newPrefLabel().getValues();
    }

    public void addPrefLabel(Literal<String> label)
    {
        newPrefLabel().add(label);
    }


    public List<Literal<String>> getAltLabels()
    {
        return newAltLabel().getValues();
    }


    public void addAltLabel(Literal<String> title)
    {
        newAltLabel().add(title);
    }


    private LanguageMap newPrefLabel()
    {
        if ( this.prefLabel == null ) { this.prefLabel = new LanguageMap(); }
        return this.prefLabel;
    }

    private LanguageMapArray newAltLabel()
    {
        if ( this.altLabel == null ) { this.altLabel = new LanguageMapArray(); }
        return this.altLabel;
    }
}
