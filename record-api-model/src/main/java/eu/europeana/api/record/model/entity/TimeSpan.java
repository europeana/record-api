package eu.europeana.api.record.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import eu.europeana.api.edm.DCTerms;
import eu.europeana.api.edm.EDM;
import eu.europeana.api.edm.OWL;
import eu.europeana.api.edm.RDF;
import eu.europeana.api.edm.SKOS;
import eu.europeana.api.record.io.json.CompactSerializer;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.data.ObjectReference;
import eu.europeana.api.record.model.internal.LanguageMapArray;
import eu.europeana.jena.encoder.annotation.JenaCollection;
import eu.europeana.jena.encoder.annotation.JenaProperty;
import eu.europeana.jena.encoder.annotation.JenaResource;

import static eu.europeana.api.record.model.ModelConstants.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hugo
 * @since 5 Aug 2023
 */
@JenaResource(ns = EDM.NS, localName = EDM.TimeSpan)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ ID, RDF.type, SKOS.prefLabel, SKOS.altLabel
        , EDM.begin, EDM.end
        , SKOS.note, DCTerms.isPartOf, DCTerms.hasPart
        , EDM.isNextInSequence, OWL.sameAs })
@Entity(value = CONTEXTUALENTITY, discriminator = EDM.TimeSpan, discriminatorKey = RDF.type)
public class TimeSpan extends ContextualEntity {

    //dates

    @JenaProperty(ns = EDM.NS, localName = EDM.begin)
    @JsonProperty(EDM.begin)
    @Property(EDM.begin)
    private Literal<String> begin = null;

    @JenaProperty(ns = EDM.NS, localName = EDM.end)
    @JsonProperty(EDM.end)
    @Property(EDM.end)
    private Literal<String> end = null;

    //note

    @JenaProperty(ns = SKOS.NS, localName = SKOS.note)
    @JenaCollection
    @JsonProperty(SKOS.note)
    protected LanguageMapArray note = null;

    //relations

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.isPartOf)
    @JsonProperty(DCTerms.isPartOf)
    @Property(DCTerms.isPartOf)
    private List<ObjectReference> isPartOf = null;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.hasPart)
    @JsonProperty(DCTerms.hasPart)
    @Property(DCTerms.hasPart)
    private List<ObjectReference> hasPart = null;

    @JenaProperty(ns = EDM.NS, localName = EDM.isNextInSequence)
    @JsonProperty(EDM.isNextInSequence)
    @Property(EDM.isNextInSequence)
    private List<ObjectReference> isNextInSequence = null;

    //identifiers

    @JenaProperty(ns = OWL.NS, localName = OWL.sameAs)
    @JsonProperty(OWL.sameAs)
    @Property(OWL.sameAs)
    @JsonSerialize(using = CompactSerializer.class)
    private List<ObjectReference> sameAs = null;


    public TimeSpan() {}

    public TimeSpan(String id) { super(id); }

    public String getType() { return EDM.TimeSpan; }

    //dates

    public Literal<String> getBegin() {
        return this.begin;
    }

    public void setBegin(Literal<String> begin) {
        this.begin = begin;
    }

    public Literal<String> getEnd() {
        return this.end;
    }

    public void setEnd(Literal<String> end) {
        this.end = end;
    }

    //note

    public List<Literal<String>> getNotes() {
        return newNote().getValues();
    }

    public void addNote(Literal<String> note) {
        newNote().add(note);
    }

    //relations

    public List<ObjectReference> getIsPartOfs() {
        return ( isPartOf != null ? isPartOf
                : (isPartOf = new ArrayList<ObjectReference>()) );
    }

    public void addIsPartOf(ObjectReference isPartOf) {
        getIsPartOfs().add(isPartOf);
    }

    public List<ObjectReference> getHasParts() {
        return ( hasPart != null ? hasPart
                : (hasPart = new ArrayList<ObjectReference>()) );
    }

    public void addHasPart(ObjectReference hasPart) {
        getHasParts().add(hasPart);
    }

    public List<ObjectReference> getIsNextInSequences() {
        return ( isNextInSequence != null ? isNextInSequence
                : (isNextInSequence = new ArrayList<ObjectReference>()) );
    }

    public void addIsNextInSequence(ObjectReference isNextInSequence) {
        getIsNextInSequences().add(isNextInSequence);
    }

    //identifiers

    public List<ObjectReference> getSameAs() {
        return ( sameAs != null ? sameAs
                : (sameAs = new ArrayList<ObjectReference>()) );
    }

    public void addSameAs(ObjectReference reference) {
        getSameAs().add(reference);
    }

    // private methods

    private LanguageMapArray newNote() {
        return ( note == null ? (note = new LanguageMapArray()) : note );
    }

    public String toString() { return ("edm:TimeSpan<" + id + ">"); }
}
