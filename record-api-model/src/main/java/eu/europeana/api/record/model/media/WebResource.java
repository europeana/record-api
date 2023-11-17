/**
 * 
 */
package eu.europeana.api.record.model.media;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import eu.europeana.api.edm.*;
import eu.europeana.api.record.io.json.CompactSerializer;
import eu.europeana.api.record.model.EDMClass;
import eu.europeana.api.record.model.data.DataValue;
import eu.europeana.api.record.model.data.ObjectReference;
import eu.europeana.jena.encoder.annotation.JenaId;
import eu.europeana.jena.encoder.annotation.JenaProperty;
import eu.europeana.jena.encoder.annotation.JenaResource;
import eu.europeana.jena.encoder.annotation.JenaTransitive;

import java.util.ArrayList;
import java.util.List;

import static eu.europeana.api.record.model.ModelConstants.*;


/**
 * @author Hugo
 * @since 5 Aug 2023
 */
@JenaResource(ns = EDM.NS, localName = EDM.WebResource)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, visible = true, property = "type")
@JsonPropertyOrder({ ID, RDF.type, ADDITIONALTYPE
                   , DC.description, DC.creator, DCTerms.created, DCTerms.issued
                   , DC.type, DC.format, DCTerms.conformsTo, DCTerms.extent
                   , EDM.rights, DC.rights
                   , EBUCORE.hasMimeType, EBUCORE.fileByteSize 
                   , EBUCORE.width, EBUCORE.height, EBUCORE.duration
                   , EDM.hasColorSpace, EDM.componentColor
                   , EBUCORE.orientation, EDM.spatialResolution
                   , EDM.codecName, EBUCORE.sampleSize, EBUCORE.sampleRate
                   , EBUCORE.bitRate, EBUCORE.frameRate, EBUCORE.audioChannelNumber                   
                   , DC.source, DCTerms.isPartOf, DCTerms.hasPart
                   , DCTerms.isFormatOf, DCTerms.isReferencedBy
                   , EDM.isNextInSequence, OWL.sameAs })
@Entity(discriminator = EDM.WebResource, discriminatorKey = RDF.type)
public class WebResource implements EDMClass
{
    @JenaId
    @JsonProperty(ID)
    @Property(ID)
    private String id;

    // description

    @JenaProperty(ns = DC.NS, localName = DC.description)
    @JsonProperty(DC.description)
    @Property(DC.description)
    private List<DataValue> description;

    // provenance

    @JenaProperty(ns = DC.NS, localName = DC.creator)
    @JsonProperty(DC.creator)
    @Property(DC.creator)
    private List<DataValue> creator;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.created)
    @Property(DCTerms.created)
    @JsonProperty(DCTerms.created)
    private List<DataValue> created;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.issued)
    @Property(DCTerms.issued)
    @JsonProperty(DCTerms.issued)
    private List<DataValue> issued;
    
    // categorisation of the resource

    @JenaProperty(ns = DC.NS, localName = DC.type)
    @Property(DCTYPE)
    @JsonProperty(DCTYPE)
    private List<DataValue> dcType;

    @JenaProperty(ns = DC.NS, localName = DC.format)
    @JsonProperty(DC.format)
    @Property(DC.format)
    private List<DataValue> format;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.conformsTo)
    @Property(DCTerms.conformsTo)
    @JsonProperty(DCTerms.conformsTo)
    private List<DataValue> conformsTo;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.extent)
    @Property(DCTerms.extent)
    @JsonProperty(DCTerms.extent)
    private List<DataValue> extent;

    // rights

    @JenaProperty(ns = EDM.NS, localName = EDM.rights)
    @Property(EDM.rights)
    @JsonProperty(EDM.rights)
    @JsonSerialize(using = CompactSerializer.class)
    private ObjectReference rights;

    @JenaProperty(ns = DC.NS, localName = DC.rights)
    @Property(DCRIGHTS)
    @JsonProperty(DCRIGHTS)
    private List<DataValue> dcRights;

    //relations

    @JenaProperty(ns = DC.NS, localName = DC.source)
    @Property(DC.source)
    @JsonProperty(DC.source)
    private List<DataValue> source;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.isPartOf)
    @Property(DCTerms.isPartOf)
    @JsonProperty(DCTerms.isPartOf)
    @JsonSerialize(using = CompactSerializer.class)
    private List<ObjectReference> isPartOf;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.hasPart)
    @Property(DCTerms.hasPart)
    @JsonProperty(DCTerms.hasPart)
    @JsonSerialize(using = CompactSerializer.class)
    private List<ObjectReference> hasPart;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.isFormatOf)
    @Property(DCTerms.isFormatOf)
    @JsonProperty(DCTerms.isFormatOf)
    private List<DataValue> isFormatOf;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.isReferencedBy)
    @Property(DCTerms.isReferencedBy)
    @JsonProperty(DCTerms.isReferencedBy)
    private List<DataValue> isReferencedBy;

    @JenaProperty(ns = EDM.NS, localName = EDM.isNextInSequence)
    @Property(EDM.isNextInSequence)
    @JsonProperty(EDM.isNextInSequence)
    @JsonSerialize(using = CompactSerializer.class)
    private List<ObjectReference> isNextInSequence;

    // technical metadata

    @JenaTransitive
    @JsonUnwrapped
    @Property(TECHMETA)
    private TechnicalMetadata techMeta;

    // service

    @JenaProperty(ns = SVCS.NS, localName = SVCS.has_service)
    @Property(SVCS.has_service)
    @JsonProperty(SVCS.has_service)
    private List<ObjectReference> hasService;

    // identifiers

    @JenaProperty(ns = OWL.NS, localName = OWL.sameAs)
    @JsonProperty(OWL.sameAs)
    @JsonSerialize(using = CompactSerializer.class)
    @Property(OWL.sameAs)
    private List<ObjectReference> sameAs;


    public WebResource() {}

    public WebResource(String id) { this.id = id; }

    public String getID() { return this.id; }

    @Override
    public String getType() { return EDM.WebResource; }


    // description

    public List<DataValue> getDescriptions() {
        return ( description != null ? description
                                : (description = new ArrayList<DataValue>()) );
    }

    public void addDescription(DataValue description) {
        getDescriptions().add(description);
    }

    // provenance

    public List<DataValue> getCreators() {
        return ( creator != null ? creator
                                : (creator = new ArrayList<DataValue>()) );
    }

    public void addCreators(DataValue creator) {
        getCreators().add(creator);
    }

    public List<DataValue> getCreateds() {
        return ( created != null ? created
                                : (created = new ArrayList<DataValue>()) );
    }

    public void addCreated(DataValue created) {
        getCreateds().add(created);
    }

    public List<DataValue> getIssueds() {
        return ( issued != null ? issued
                                : (issued = new ArrayList<DataValue>()) );
    }

    public void addIssued(DataValue issued) {
        getIssueds().add(issued);
    }

    // categorisation of the resource

    public List<DataValue> getDcTypes() {
        return ( dcType != null ? dcType
                                : (dcType = new ArrayList<DataValue>()) );
    }

    public void addDcType(DataValue dcType) {
        getDcTypes().add(dcType);
    }

    public List<DataValue> getFormats() {
        return ( format != null ? format
                                : (format = new ArrayList<DataValue>()) );
    }

    public void addFormat(DataValue format) {
        getFormats().add(format);
    }

    public List<DataValue> getConformsTos() {
        return ( conformsTo != null ? conformsTo
                                    : (conformsTo = new ArrayList<DataValue>()) );
    }

    public void addConformsTo(DataValue conformsTo) {
        getConformsTos().add(conformsTo);
    }

    public List<DataValue> getExtents() {
        return ( extent != null ? extent
                                : (extent = new ArrayList<DataValue>()) );
    }

    public void addExtent(DataValue extent) {
        getExtents().add(extent);
    }

    // rights

    public ObjectReference getRights() {
        return rights;
    }

    public void setRights(ObjectReference rights) {
        this.rights = rights;
    }

    public List<DataValue> getDcRights() {
        return ( dcRights != null ? dcRights
                                  : (dcRights = new ArrayList<DataValue>(1)) );
    }

    public void addDcRights(DataValue dcRights) {
        getDcRights().add(dcRights);
    }

    //relations

    public List<DataValue> getSources() {
        return ( source != null ? source
                                : (source = new ArrayList<DataValue>()) );
    }

    public void addSource(DataValue source) {
        getSources().add(source);
    }

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

    public List<DataValue> getIsFormatOfs() {
        return ( isFormatOf != null ? isFormatOf
                                    : (isFormatOf = new ArrayList<DataValue>()) );
    }

    public void addIsFormatOf(DataValue isFormatOf) {
        getIsFormatOfs().add(isFormatOf);
    }

    public List<DataValue> getIsReferencedBys() {
        return ( isReferencedBy != null ? isReferencedBy
                                        : (isReferencedBy = new ArrayList<DataValue>()) );
    }

    public void addIsReferencedBy(DataValue isReferencedBy) {
        getIsReferencedBys().add(isReferencedBy);
    }

    public boolean hasIsNextInSequences() {
        return (isNextInSequence != null);
    }

    public List<ObjectReference> getIsNextInSequences() {
        return ( isNextInSequence != null ? isNextInSequence
                                          : (isNextInSequence = new ArrayList<ObjectReference>()) );
    }

    public void addIsNextInSequence(ObjectReference isNextInSequence) {
        getIsNextInSequences().add(isNextInSequence);
    }

    // technical metadata

    public TechnicalMetadata getTechnicalMetadata() {
        return techMeta;
    }

    public boolean hasTechnicalMetadata() {
        return (techMeta != null);
    }

    public void setTechnicalMetadata(TechnicalMetadata techMeta) {
        this.techMeta = techMeta;
    }

    // services

    public List<ObjectReference> getHasServices() {
        return ( hasService != null ? hasService
                                    : (hasService = new ArrayList<ObjectReference>()) );
    }

    public void addJHasService(ObjectReference hasService) {
        getHasServices().add(hasService);
    }

    // identifiers

    public List<ObjectReference> getSameAs() {
        return ( sameAs != null ? sameAs
                                : (sameAs = new ArrayList<ObjectReference>()) );
    }

    public void addSameAs(ObjectReference reference) {
        getSameAs().add(reference);
    }

    public String toString() { return ("edm:WebResource<" + id + ">"); }
}
