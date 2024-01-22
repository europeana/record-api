package eu.europeana.api.record.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import eu.europeana.api.edm.RDF;
import eu.europeana.api.edm.SKOS;
import eu.europeana.api.record.io.json.CompactSerializer;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.data.ObjectReference;
import eu.europeana.api.record.model.internal.LanguageMapArray;
import eu.europeana.jena.encoder.annotation.JenaCollection;
import eu.europeana.jena.encoder.annotation.JenaProperty;
import eu.europeana.jena.encoder.annotation.JenaResource;

import static eu.europeana.api.record.model.ModelConstants.id;
import static eu.europeana.api.record.model.ModelConstants.ContextualEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hugo
 * @since 5 Aug 2023
 */
@JenaResource(ns = SKOS.NS, localName = SKOS.Concept)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ id, RDF.type, SKOS.prefLabel, SKOS.altLabel
        , SKOS.note, SKOS.notation
        , SKOS.broader, SKOS.narrower, SKOS.related
        , SKOS.broadMatch, SKOS.narrowMatch, SKOS.relatedMatch
        , SKOS.closeMatch, SKOS.exactMatch, SKOS.inScheme })
@Entity(value = ContextualEntity, discriminator = SKOS.Concept, discriminatorKey = RDF.type)
public class Concept extends ContextualEntity {

    //note

    @JenaProperty(ns = SKOS.NS, localName = SKOS.note)
    @JenaCollection
    @JsonProperty(SKOS.note)
    protected LanguageMapArray note = null;

    @JenaProperty(ns = SKOS.NS, localName = SKOS.notation)
    @JenaCollection
    @JsonProperty(SKOS.notation)
    protected List<Literal<String>> notation = null;


    //semantic relations

    @JenaProperty(ns = SKOS.NS, localName = SKOS.broader)
    @JsonProperty(SKOS.broader)
    @Property(SKOS.broader)
    private List<ObjectReference> broader = null;

    @JenaProperty(ns = SKOS.NS, localName = SKOS.narrower)
    @JsonProperty(SKOS.narrower)
    @Property(SKOS.narrower)
    private List<ObjectReference> narrower = null;

    @JenaProperty(ns = SKOS.NS, localName = SKOS.related)
    @JsonProperty(SKOS.related)
    @Property(SKOS.related)
    private List<ObjectReference> related = null;

    //match relations

    @JenaProperty(ns = SKOS.NS, localName = SKOS.broadMatch)
    @JsonProperty(SKOS.broadMatch)
    @Property(SKOS.broadMatch)
    private List<ObjectReference> broadMatch = null;

    @JenaProperty(ns = SKOS.NS, localName = SKOS.narrowMatch)
    @JsonProperty(SKOS.narrowMatch)
    @Property(SKOS.narrowMatch)
    private List<ObjectReference> narrowMatch = null;

    @JenaProperty(ns = SKOS.NS, localName = SKOS.relatedMatch)
    @JsonProperty(SKOS.relatedMatch)
    @Property(SKOS.relatedMatch)
    private List<ObjectReference> relatedMatch = null;

    @JenaProperty(ns = SKOS.NS, localName = SKOS.closeMatch)
    @JsonProperty(SKOS.closeMatch)
    @Property(SKOS.closeMatch)
    private List<ObjectReference> closeMatch = null;

    @JenaProperty(ns = SKOS.NS, localName = SKOS.exactMatch)
    @Property(SKOS.exactMatch)
    @JsonProperty(SKOS.exactMatch)
    @JsonSerialize(using = CompactSerializer.class)
    private List<ObjectReference> exactMatch = null;

    //scheme

    @JenaProperty(ns = SKOS.NS, localName = SKOS.inScheme)
    @JsonProperty(SKOS.inScheme)
    @Property(SKOS.inScheme)
    private List<ObjectReference> inScheme = null;


    public Concept() {}

    public Concept(String id) { super(id); }

    public String getType() { return SKOS.Concept; }

    //note

    public List<Literal<String>> getNotes() {
        return newNote().getValues();
    }

    public void addNote(Literal<String> note) {
        newNote().add(note);
    }

    public List<Literal<String>> getNotations() {
        return ( notation != null ? notation
                : (notation = new ArrayList<Literal<String>>()) );
    }

    public void addNotation(Literal<String> notation) {
        getNotations().add(notation);
    }

    //semantic relations

    public List<ObjectReference> getBroaders() {
        return ( broader != null ? broader
                : (broader = new ArrayList<ObjectReference>()) );
    }

    public void addBroader(ObjectReference broader) {
        getBroaders().add(broader);
    }

    public List<ObjectReference> getNarrowers() {
        return ( narrower != null ? narrower
                : (narrower = new ArrayList<ObjectReference>()) );
    }

    public void addNarrower(ObjectReference narrower) {
        getNarrowers().add(narrower);
    }

    public List<ObjectReference> getRelateds() {
        return ( related != null ? related
                : (related = new ArrayList<ObjectReference>()) );
    }

    public void addRelated(ObjectReference related) {
        getRelateds().add(related);
    }

    //match relations

    public List<ObjectReference> getBroadMatches() {
        return ( broadMatch != null ? broadMatch
                : (broadMatch = new ArrayList<ObjectReference>()) );
    }

    public void addBroadMatch(ObjectReference broadMatch) {
        getBroadMatches().add(broadMatch);
    }

    public List<ObjectReference> getNarrowMatches() {
        return ( narrowMatch != null ? narrowMatch
                : (narrowMatch = new ArrayList<ObjectReference>()) );
    }

    public void addNarrowMatch(ObjectReference narrowMatch) {
        getNarrowMatches().add(narrowMatch);
    }

    public List<ObjectReference> getRelatedMatches() {
        return ( relatedMatch != null ? relatedMatch
                : (relatedMatch = new ArrayList<ObjectReference>()) );
    }

    public void addRelatedMatch(ObjectReference relatedMatch) {
        getRelatedMatches().add(relatedMatch);
    }

    public List<ObjectReference> getCloseMatches() {
        return ( closeMatch != null ? closeMatch
                : (closeMatch = new ArrayList<ObjectReference>()) );
    }

    public void addCloseMatch(ObjectReference closeMatch) {
        getCloseMatches().add(closeMatch);
    }

    public List<ObjectReference> getExactMatches() {
        return ( exactMatch != null ? exactMatch
                : (exactMatch = new ArrayList<ObjectReference>()) );
    }

    public void addExactMatch(ObjectReference exactMatch) {
        getExactMatches().add(exactMatch);
    }


    //scheme

    public List<ObjectReference> getInSchemes() {
        return ( inScheme != null ? inScheme
                : (inScheme = new ArrayList<ObjectReference>()) );
    }

    public void addInScheme(ObjectReference inScheme) {
        getInSchemes().add(inScheme);
    }

    // private methods

    private LanguageMapArray newNote() {
        return ( note == null ? (note = new LanguageMapArray()) : note );
    }

    public String toString() { return ("skos:Concept<" + id + ">"); }
}

