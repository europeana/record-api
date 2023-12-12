package eu.europeana.api.record.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import eu.europeana.api.edm.CC;
import eu.europeana.api.edm.ODRL;
import eu.europeana.api.edm.RDF;
import eu.europeana.api.record.model.EDMClass;
import eu.europeana.api.record.model.ModelConstants;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.data.ObjectReference;
import eu.europeana.jena.encoder.annotation.JenaId;
import eu.europeana.jena.encoder.annotation.JenaProperty;
import eu.europeana.jena.encoder.annotation.JenaResource;

import java.time.LocalDate;

/**
 * @author Hugo
 * @since 5 Aug 2023
 */
@JenaResource(ns = CC.NS, localName = CC.License)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ ModelConstants.id, RDF.type
        , ODRL.inheritFrom, CC.deprecatedOn })
@Entity(discriminator = CC.License, discriminatorKey = RDF.type)
public class License implements EDMClass {

    @JenaId
    @JsonProperty(ModelConstants.id)
    @Property(ModelConstants.id)
    protected String id;

    @JenaProperty(ns = ODRL.NS, localName = ODRL.inheritFrom)
    @Property(ODRL.inheritFrom)
    @JsonProperty(ODRL.inheritFrom)
    private ObjectReference inheritFrom;

    @JenaProperty(ns = CC.NS, localName = CC.deprecatedOn)
    @Property(CC.deprecatedOn)
    @JsonProperty(CC.deprecatedOn)
    private Literal<LocalDate> deprecatedOn;

    public License() {}

    public License(String id) { this.id = id; }

    public String getID() { return id; }

    public String getType() { return CC.License; }

    public ObjectReference getInheritFrom() {
        return this.inheritFrom;
    }

    public void setInheritFrom(ObjectReference inheritFrom) {
        this.inheritFrom = inheritFrom;
    }

    public Literal<LocalDate> getDeprecatedOn() {
        return this.deprecatedOn;
    }

    public void setDeprecatedOn(Literal<LocalDate> deprecatedOn) {
        this.deprecatedOn = deprecatedOn;
    }

    public String toString() { return ("cc:License<" + id + ">"); }
}
