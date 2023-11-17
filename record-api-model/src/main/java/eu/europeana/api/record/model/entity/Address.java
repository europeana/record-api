package eu.europeana.api.record.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import eu.europeana.api.edm.RDF;
import eu.europeana.api.edm.VCARD;
import eu.europeana.api.record.io.json.CompactSerializer;
import eu.europeana.api.record.model.EDMClass;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.data.ObjectReference;
import eu.europeana.jena.encoder.annotation.JenaId;
import eu.europeana.jena.encoder.annotation.JenaProperty;
import eu.europeana.jena.encoder.annotation.JenaResource;

import static eu.europeana.api.record.model.ModelConstants.ID;

/**
 * @author Hugo
 * @since 5 Aug 2023
 */
@JenaResource(ns = VCARD.NS, localName = VCARD.Address)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ ID, RDF.type
        , VCARD.streetAddress, VCARD.postalCode, VCARD.locality
        , VCARD.countryName, VCARD.hasGeo })
@Entity(discriminator = VCARD.Address, discriminatorKey = RDF.type)
public class Address implements EDMClass {

    @JenaId
    @JsonProperty(ID)
    @Property(ID)
    protected String id;

    @JenaProperty(ns = VCARD.NS, localName = VCARD.rdfStreetAddress)
    @Property(VCARD.streetAddress)
    @JsonProperty(VCARD.streetAddress)
    private Literal<String> streetAddress;

    @JenaProperty(ns = VCARD.NS, localName = VCARD.rdfPostalCode)
    @Property(VCARD.postalCode)
    @JsonProperty(VCARD.postalCode)
    private Literal<String> postalCode;

    @JenaProperty(ns = VCARD.NS, localName = VCARD.locality)
    @Property(VCARD.locality)
    @JsonProperty(VCARD.locality)
    private Literal<String> locality;

    @JenaProperty(ns = VCARD.NS, localName = VCARD.rdfCountryName)
    @Property(VCARD.countryName)
    @JsonProperty(VCARD.countryName)
    private Literal<String> countryName;

    @JenaProperty(ns = VCARD.NS, localName = VCARD.hasGeo)
    @Property(VCARD.hasGeo)
    @JsonProperty(VCARD.hasGeo)
    @JsonSerialize(using = CompactSerializer.class)
    private ObjectReference hasGeo;


    public Address() {
    }

    public Address(String id) {
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public String getType() {
        return VCARD.Address;
    }

    public Literal<String> getStreetAddress() {
        return this.streetAddress;
    }

    public void setStreetAddress(Literal<String> streetAddress) {
        this.streetAddress = streetAddress;
    }

    public Literal<String> getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(Literal<String> postalCode) {
        this.postalCode = postalCode;
    }

    public Literal<String> getLocality() {
        return this.locality;
    }

    public void setLocality(Literal<String> locality) {
        this.locality = locality;
    }

    public Literal<String> getCountryName() {
        return this.countryName;
    }

    public void setCountryName(Literal<String> countryName) {
        this.countryName = countryName;
    }

    public ObjectReference getHasGeo() {
        return this.hasGeo;
    }

    public void setHasGeo(ObjectReference hasGeo) {
        this.hasGeo = hasGeo;
    }

    public String toString() {
        return ("vcard:Address<" + id + ">");
    }
}
