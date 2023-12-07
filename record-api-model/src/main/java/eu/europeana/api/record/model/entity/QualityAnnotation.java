package eu.europeana.api.record.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import eu.europeana.api.edm.DCTerms;
import eu.europeana.api.edm.DQV;
import eu.europeana.api.edm.OA;
import eu.europeana.api.edm.RDF;
import eu.europeana.api.record.io.json.EDMClassReferenceSerializer;
import eu.europeana.api.record.io.json.CompactSerializer;
import eu.europeana.api.record.model.Aggregation;
import eu.europeana.api.record.model.EDMClass;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.data.ObjectReference;
import eu.europeana.jena.encoder.annotation.JenaId;
import eu.europeana.jena.encoder.annotation.JenaProperty;
import eu.europeana.jena.encoder.annotation.JenaResource;

import static eu.europeana.api.record.model.ModelConstants.ID;


import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hugo
 * @since 5 Aug 2023
 */
@JenaResource(ns = DQV.NS, localName = DQV.QualityAnnotation)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ ID, RDF.type
        , OA.hasBody, OA.hasTarget, DCTerms.created })
@Entity(discriminator = DQV.QualityAnnotation, discriminatorKey = RDF.type)
public class QualityAnnotation implements EDMClass {

    @JenaId
    @JsonProperty(ID)
    @Property(ID)
    protected String id;

    @JenaProperty(ns = OA.NS, localName = OA.hasBody)
    @Property(OA.hasBody)
    @JsonProperty(OA.hasBody)
    @JsonSerialize(using = CompactSerializer.class)
    private ObjectReference hasBody;

    @JenaProperty(ns = OA.NS, localName = OA.hasTarget)
    @Property(OA.hasTarget)
    @JsonProperty(OA.hasTarget)
    @JsonSerialize(using = CompactSerializer.class)
    private List<ObjectReference> hasTarget;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.created)
    @Property(DCTerms.created)
    @JsonProperty(DCTerms.created)
    @JsonSerialize(using = CompactSerializer.class)
    private Literal<Instant> created;



    public QualityAnnotation() {}

    public QualityAnnotation(String id) { this.id = id; }

    public String getID() { return id; }

    public String getType() { return DQV.QualityAnnotation; }


    public ObjectReference getHasBody() {
        return this.hasBody;
    }

    public void setHasBody(ObjectReference hasBody) {
        this.hasBody = hasBody;
    }


    public List<ObjectReference> getHasTargets() {
        return ( hasTarget != null ? hasTarget
                : (hasTarget = new ArrayList<ObjectReference>()) );
    }

    public void addHasTarget(ObjectReference hasTarget) {
        getHasTargets().add(hasTarget);
    }

    public Literal<Instant> getCreated() {
        return this.created;
    }

    public void setCreated(Literal<Instant> created) {
        this.created = created;
    }

    public String toString() { return ("dcat:QualityAnnotation<" + id + ">"); }
}

