package eu.europeana.api.record.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import eu.europeana.api.edm.*;
import eu.europeana.api.record.io.json.CompactSerializer;
import eu.europeana.api.record.model.data.DataValue;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.data.ObjectReference;
import eu.europeana.api.record.model.internal.LanguageMapArray;
import eu.europeana.jena.encoder.annotation.JenaCollection;
import eu.europeana.jena.encoder.annotation.JenaProperty;
import eu.europeana.jena.encoder.annotation.JenaResource;

import java.util.ArrayList;
import java.util.List;

import static eu.europeana.api.record.model.ModelConstants.CONTEXTUALENTITY;
import static eu.europeana.api.record.model.ModelConstants.ID;

@JenaResource(ns = EDM.NS, localName = EDM.Agent)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true, property = RDF.type)
@JsonPropertyOrder({ ID, RDF.type, SKOS.prefLabel, SKOS.altLabel, FOAF.name
        , EDM.begin, RDAGR2.dateOfBirth, RDAGR2.dateOfEstablishment
        , EDM.end, RDAGR2.dateOfDeath, RDAGR2.dateOfTermination
        , RDAGR2.placeOfBirth, RDAGR2.placeOfDeath
        , RDAGR2.professionOrOccupation, RDAGR2.gender, RDAGR2.biographicalInformation
        , DCTerms.isPartOf, DCTerms.hasPart, EDM.isRelatedTo, EDM.hasMet
        , DC.identifier, OWL.sameAs })
@Entity(value = CONTEXTUALENTITY, discriminator = EDM.Agent, discriminatorKey = RDF.type)
public class Agent extends ContextualEntity {

    @JenaProperty(ns = FOAF.NS, localName = FOAF.name)
    @JenaCollection
    @JsonProperty(FOAF.name)
    private LanguageMapArray name = null;

    //Dates

    @JenaProperty(ns = EDM.NS, localName = EDM.begin)
    @JsonProperty(EDM.begin)
    @Property(EDM.begin)
    private Literal<String> begin = null;

    @JenaProperty(ns = RDAGR2.NS, localName = RDAGR2.dateOfBirth)
    @JsonProperty(RDAGR2.dateOfBirth)
    @Property(RDAGR2.dateOfBirth)
    private Literal<String> dateOfBirth = null;

    @JenaProperty(ns = RDAGR2.NS, localName = RDAGR2.dateOfEstablishment)
    @JsonProperty(RDAGR2.dateOfEstablishment)
    @Property(RDAGR2.dateOfEstablishment)
    private Literal<String> dateOfEstablishment = null;

    @JenaProperty(ns = EDM.NS, localName = EDM.end)
    @JsonProperty(EDM.end)
    @Property(EDM.end)
    private Literal<String> end = null;

    @JenaProperty(ns = RDAGR2.NS, localName = RDAGR2.dateOfDeath)
    @JsonProperty(RDAGR2.dateOfDeath)
    @Property(RDAGR2.dateOfDeath)
    private Literal<String> dateOfDeath = null;

    @JenaProperty(ns = RDAGR2.NS, localName = RDAGR2.dateOfTermination)
    @JsonProperty(RDAGR2.dateOfTermination)
    @Property(RDAGR2.dateOfTermination)
    private Literal<String> dateOfTermination = null;

    @JenaProperty(ns = DC.NS, localName = DC.date)
    @JsonProperty(DC.date)
    @Property(DC.date)
    private List<DataValue> date = null;

    //Places

    @JenaProperty(ns = RDAGR2.NS, localName = RDAGR2.placeOfBirth)
    @JsonProperty(RDAGR2.placeOfBirth)
    @Property(RDAGR2.placeOfBirth)
    private DataValue placeOfBirth = null;

    @JenaProperty(ns = RDAGR2.NS, localName = RDAGR2.placeOfDeath)
    @JsonProperty(RDAGR2.placeOfDeath)
    @Property(RDAGR2.placeOfDeath)
    private DataValue placeOfDeath = null;

    //other information

    @JenaProperty(ns = RDAGR2.NS, localName = RDAGR2.professionOrOccupation)
    @JsonProperty(RDAGR2.professionOrOccupation)
    @Property(RDAGR2.professionOrOccupation)
    private List<DataValue> professionOrOccupation = null;

    @JenaProperty(ns = RDAGR2.NS, localName = RDAGR2.gender)
    @JsonProperty(RDAGR2.gender)
    @Property(RDAGR2.gender)
    private Literal<String> gender = null;

    @JenaProperty(ns = RDAGR2.NS, localName = RDAGR2.biographicalInformation)
    @JenaCollection
    @JsonProperty(RDAGR2.biographicalInformation)
    private LanguageMapArray biographicalInformation = null;

    @JenaProperty(ns = SKOS.NS, localName = SKOS.note)
    @JenaCollection
    @JsonProperty(SKOS.note)
    private LanguageMapArray note = null;

    //relations

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.isPartOf)
    @JsonProperty(DCTerms.isPartOf)
    @Property(DCTerms.isPartOf)
    private List<ObjectReference> isPartOf = null;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.hasPart)
    @JsonProperty(DCTerms.hasPart)
    @Property(DCTerms.hasPart)
    private List<ObjectReference> hasPart = null;

    @JenaProperty(ns = EDM.NS, localName = EDM.isRelatedTo)
    @JsonProperty(EDM.isRelatedTo)
    @Property(EDM.isRelatedTo)
    private List<ObjectReference> isRelatedTo = null;

    @JenaProperty(ns = EDM.NS, localName = EDM.hasMet)
    @JsonProperty(EDM.hasMet)
    @Property(EDM.hasMet)
    private List<ObjectReference> hasMet = null;

    //identifiers

    @JenaProperty(ns = DC.NS, localName = DC.identifier)
    @JsonProperty(DC.identifier)
    @Property(DC.identifier)
    private List<Literal<String>> identifier = null;

    @JenaProperty(ns = OWL.NS, localName = OWL.sameAs)
    @JsonProperty(OWL.sameAs)
    @Property(OWL.sameAs)
    @JsonSerialize(using = CompactSerializer.class)
    private List<ObjectReference> sameAs = null;


    public Agent() {}

    public Agent(String id) { super(id); }

    public String getType() { return EDM.Agent; }


    //Names

    public List<Literal<String>> getNames() {
        return newName().getValues();
    }


    public void addName(Literal<String> name) {
        newName().add(name);
    }

    //Dates

    public Literal<String> getBegin() {
        return this.begin;
    }

    public void setBegin(Literal<String> begin) {
        this.begin = begin;
    }

    public Literal<String> getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(Literal<String> dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Literal<String> getDateOfEstablishment() {
        return this.dateOfEstablishment;
    }

    public void setDateOfEstablishment(Literal<String> dateOfEstablishment) {
        this.dateOfEstablishment = dateOfEstablishment;
    }

    public Literal<String> getEnd() {
        return this.end;
    }

    public void setEnd(Literal<String> end) {
        this.end = end;
    }

    public Literal<String> getDateOfDeath() {
        return this.dateOfDeath;
    }

    public void setDateOfDeath(Literal<String> dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public Literal<String> getDateOfTermination() {
        return this.dateOfTermination;
    }

    public void setDateOfTermination(Literal<String> dateOfTermination) {
        this.dateOfTermination = dateOfTermination;
    }

    public List<DataValue> getDates() {
        return ( date != null ? date : (date = new ArrayList<DataValue>()) );
    }

    public void addDate(DataValue description) {
        getDates().add(description);
    }


    //Places

    public DataValue getPlaceOfBirth() {
        return this.placeOfBirth;
    }

    public void setPlaceOfBirth(DataValue placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public DataValue getPlaceOfDeath() {
        return this.placeOfDeath;
    }

    public void setPlaceOfDeath(DataValue placeOfDeath) {
        this.placeOfDeath = placeOfDeath;
    }


    //other information

    public List<DataValue> getProfessionOrOccupations() {
        return ( professionOrOccupation != null ? professionOrOccupation
                : (professionOrOccupation = new ArrayList<DataValue>()) );
    }

    public void addHasPart(DataValue professionOrOccupation) {
        getProfessionOrOccupations().add(professionOrOccupation);
    }

    public Literal<String> getGender() {
        return this.gender;
    }

    public void setGender(Literal<String> gender) {
        this.gender = gender;
    }

    public List<Literal<String>> getBiographicalInformations() {
        return newBiographicalInformation().getValues();
    }

    public void addBiographicalInformation(Literal<String> bioInformation) {
        newBiographicalInformation().add(bioInformation);
    }

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

    public List<ObjectReference> getIsRelatedTos() {
        return ( isRelatedTo != null ? isRelatedTo
                : (isRelatedTo = new ArrayList<ObjectReference>()) );
    }

    public void addIsRelatedTo(ObjectReference isRelatedTo) {
        getIsRelatedTos().add(isRelatedTo);
    }


    public List<ObjectReference> getHasMets() {
        return ( hasMet != null ? hasMet
                : (hasMet = new ArrayList<ObjectReference>()) );
    }

    public void addHasMet(ObjectReference hasMet) {
        getHasMets().add(hasMet);
    }


    //identifiers

    public List<Literal<String>> getIdentifiers() {
        return ( identifier != null ? identifier
                : (identifier = new ArrayList<Literal<String>>()) );
    }

    public void addIdentifier(Literal<String> identifier) {
        getIdentifiers().add(identifier);
    }

    public List<ObjectReference> getSameAs() {
        return ( sameAs != null ? sameAs
                : (sameAs = new ArrayList<ObjectReference>()) );
    }

    public void addSameAs(ObjectReference reference) {
        getSameAs().add(reference);
    }

    // private methods

    private LanguageMapArray newName() {
        return ( name == null ? (name = new LanguageMapArray()) : name );
    }

    private LanguageMapArray newBiographicalInformation() {
        return ( biographicalInformation == null ? (biographicalInformation = new LanguageMapArray())
                : biographicalInformation );
    }

    private LanguageMapArray newNote() {
        return ( note == null ? (note = new LanguageMapArray()) : note );
    }

    public String toString() { return ("edm:Agent<" + id + ">"); }
}
