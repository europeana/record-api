package eu.europeana.api.record.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import eu.europeana.api.edm.*;
import eu.europeana.api.record.io.json.CompactSerializer;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.data.ObjectReference;
import eu.europeana.api.record.model.internal.LanguageMapArray;
import eu.europeana.jena.encoder.annotation.JenaCollection;
import eu.europeana.jena.encoder.annotation.JenaProperty;
import eu.europeana.jena.encoder.annotation.JenaResource;

import java.util.ArrayList;
import java.util.List;

import static eu.europeana.api.record.model.ModelConstants.id;
import static eu.europeana.api.record.model.ModelConstants.ContextualEntity;

@JenaResource(ns = EDM.NS, localName = EDM.Place)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ id, RDF.type, SKOS.prefLabel, SKOS.altLabel
        , WGS84.latitude, WGS84.longitude, WGS84.altitude
        , SKOS.note, DCTerms.isPartOf, DCTerms.hasPart
        , EDM.isNextInSequence, OWL.sameAs })
@Entity(value = ContextualEntity, discriminator = EDM.Place, discriminatorKey = RDF.type)
public class Place extends ContextualEntity {

    //coordinates

    @JenaProperty(ns = WGS84.NS, localName = WGS84.latitude)
    @Property(WGS84.latitude)
    @JsonProperty(WGS84.latitude)
    private Literal<Double> latitude;

    @JenaProperty(ns = WGS84.NS, localName = WGS84.longitude)
    @Property(WGS84.longitude)
    @JsonProperty(WGS84.longitude)
    private Literal<Double> longitude;

    @JenaProperty(ns = WGS84.NS, localName = WGS84.altitude)
    @Property(WGS84.altitude)
    @JsonProperty(WGS84.altitude)
    private Literal<Double> altitude;

    //note

    @JenaProperty(ns = SKOS.NS, localName = SKOS.note)
    @JenaCollection
    @JsonProperty(SKOS.note)
    protected LanguageMapArray note;

    //relations

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.isPartOf)
    @JsonProperty(DCTerms.isPartOf)
    @Property(DCTerms.isPartOf)
    private List<ObjectReference> isPartOf;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.hasPart)
    @JsonProperty(DCTerms.hasPart)
    @Property(DCTerms.hasPart)
    private List<ObjectReference> hasPart;

    @JenaProperty(ns = EDM.NS, localName = EDM.isNextInSequence)
    @JsonProperty(EDM.isNextInSequence)
    @Property(EDM.isNextInSequence)
    private List<ObjectReference> isNextInSequence;

    //identifiers

    @JenaProperty(ns = OWL.NS, localName = OWL.sameAs)
    @JsonProperty(OWL.sameAs)
    @Property(OWL.sameAs)
    @JsonSerialize(using = CompactSerializer.class)
    private List<ObjectReference> sameAs;


    public Place() {}

    public Place(String id) { super(id); }

    public String getType() { return EDM.Place; }

    //coordinates

    public Literal<Double> getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Literal<Double> latitude) {
        this.latitude = latitude;
    }

    public Literal<Double> getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Literal<Double> longitude) {
        this.longitude = longitude;
    }

    public Literal<Double> getAltitude() {
        return this.altitude;
    }

    public void setAltitude(Literal<Double> altitude) {
        this.altitude = altitude;
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

    public String toString() { return ("edm:Place<" + id + ">"); }
}
