/**
 * 
 */
package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import dev.morphia.annotations.Transient;
import eu.europeana.api.edm.*;
import eu.europeana.api.record.io.json.CompactSerializer;
import eu.europeana.api.record.io.json.EDMClassReferenceSerializer;
import eu.europeana.api.record.model.data.DataValue;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.data.ObjectReference;
import eu.europeana.api.record.model.entity.QualityAnnotation;
import eu.europeana.api.record.model.media.WebResource;
import eu.europeana.jena.encoder.annotation.JenaId;
import eu.europeana.jena.encoder.annotation.JenaProperty;
import eu.europeana.jena.encoder.annotation.JenaResource;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import static eu.europeana.api.record.model.ModelConstants.ID;
import static eu.europeana.api.record.model.ModelConstants.DCRIGHTS;


/**
 * @author Hugo
 * @since 5 Aug 2023
 */
@JenaResource(ns = ORE.NS, localName = ORE.Aggregation)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ ID, RDF.type
                   , EDM.dataProvider, EDM.intermediateProvider, EDM.provider
                   , EDM.object, EDM.isShownAt, EDM.isShownBy, EDM.hasView
                   , EDM.rights, DCRIGHTS, EDM.ugc
                   , DCTerms.created, DCTerms.modified
                   , DQV.hasQualityAnnotation, EDM.aggregatedCHO })
@Entity(discriminator = ORE.Aggregation, discriminatorKey = RDF.type)
public class Aggregation implements EDMClass
{
    @JenaId
    @JsonProperty(ID)
    @Property(ID)
    protected String id;

    //providers

    @JenaProperty(ns = EDM.NS, localName = EDM.dataProvider)
    @Property(EDM.dataProvider)
    @JsonProperty(EDM.dataProvider)
    private DataValue dataProvider;

    @JenaProperty(ns = EDM.NS, localName = EDM.intermediateProvider)
    @Property(EDM.intermediateProvider)
    @JsonProperty(EDM.intermediateProvider)
    private List<DataValue> intermediateProvider;

    @JenaProperty(ns = EDM.NS, localName = EDM.provider)
    @Property(EDM.provider)
    @JsonProperty(EDM.provider)
    private DataValue provider;

    //web resources

    @JenaProperty(ns = EDM.NS, localName = EDM.object)
    @JsonProperty(EDM.object)
    @Property(EDM.object)
    private WebResource object;

    @JenaProperty(ns = EDM.NS, localName = EDM.isShownAt)
    @JsonProperty(EDM.isShownAt)
    @Property(EDM.isShownAt)
    private WebResource isShownAt;

    @JenaProperty(ns = EDM.NS, localName = EDM.isShownBy)
    @JsonProperty(EDM.isShownBy)
    @Property(EDM.isShownBy)
    private WebResource isShownBy;

    @JenaProperty(ns = EDM.NS, localName = EDM.hasView)
    @JsonProperty(EDM.hasView)
    @Property(EDM.hasView)
    private List<WebResource> hasViews;

    //other metadata

    @JenaProperty(ns = EDM.NS, localName = EDM.rights)
    @Property(EDM.rights)
    @JsonProperty(EDM.rights)
    @JsonSerialize(using = CompactSerializer.class)
    private ObjectReference rights;

    @JenaProperty(ns = DC.NS, localName = DC.rights)
    @Property(DCRIGHTS)
    @JsonProperty(DCRIGHTS)
    private List<DataValue> dcRights;

    @JenaProperty(ns = EDM.NS, localName = EDM.ugc)
    @Property(EDM.ugc)
    @JsonProperty(EDM.ugc)
    @JsonSerialize(using = CompactSerializer.class)
    private Literal<Boolean> ugc;

    //timestamps

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.created)
    @Property(DCTerms.created)
    @JsonProperty(DCTerms.created)
    @JsonSerialize(using = CompactSerializer.class)
    private Literal<Instant> created;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.modified)
    @Property(DCTerms.modified)
    @JsonProperty(DCTerms.modified)
    @JsonSerialize(using = CompactSerializer.class)
    private Literal<Instant> modified;

    //quality

    @JenaProperty(ns = DQV.NS, localName = DQV.hasQualityAnnotation)
    @Property(DQV.hasQualityAnnotation)
    @JsonProperty(DQV.hasQualityAnnotation)
    private List<QualityAnnotation> hasQualityAnnotation;

    @JenaProperty(ns = EDM.NS, localName = EDM.aggregatedCHO)
    @JsonProperty(EDM.aggregatedCHO)
    @JsonSerialize(using = EDMClassReferenceSerializer.class)
    @Transient
    private ProvidedCHO aggregatedCHO;
        

    public Aggregation(String id) { this.id = id; }

    public Aggregation() {}

    public String getID() { return id; }

    public String getType() { return ORE.Aggregation; }

    // web resources

    public WebResource getObject() {
        return object;
    }

    public void setObject(WebResource object) {
        this.object = object;
    }

    public WebResource getIsShownAt() {
        return isShownAt;
    }

    public void setIsShownAt(WebResource isShownAt) {
        this.isShownAt = isShownAt;
    }

    public WebResource getIsShownBy() {
        return isShownBy;
    }

    public void setIsShownBy(WebResource isShownBy) {
        this.isShownBy = isShownBy;
    }

    public List<WebResource> getViews() {
        return ( hasViews != null ? hasViews
                                  : (hasViews = new ArrayList<WebResource>()) );
    }

    public boolean hasViews() { return ( hasViews != null && !hasViews.isEmpty() ); }

    //providers

    public DataValue getDataProvider() {
        return dataProvider;
    }

    public void setDataProvider(DataValue dataProvider) {
        this.dataProvider = dataProvider;
    }

    public List<DataValue> getIntermediateProviders() {
        return ( intermediateProvider != null
             ? intermediateProvider
             : (intermediateProvider = new ArrayList<DataValue>(1)) );
    }

    public void addSameAs(DataValue intermediateProvider) {
        getIntermediateProviders().add(intermediateProvider);
    }

    public DataValue getProvider() {
        return dataProvider;
    }

    public void setProvider(DataValue provider) {
        this.provider = provider;
    }

    //other metadata

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

    public Literal<Boolean> getUGC() {
        return ugc;
    }

    public void setUGC(Literal<Boolean> ugc) {
        this.ugc = ugc;
    }


    //timestamps

    public Literal<Instant> getCreated() {
        return this.created;
    }

    public void setCreated(Literal<Instant> created) {
        this.created = created;
    }

    public Literal<Instant> getModified() {
        return this.modified;
    }

    public void setModified(Literal<Instant> modified) {
        this.modified = modified;
    }

    //quality

    public List<QualityAnnotation> getHasQualityAnnotations() {
        return ( hasQualityAnnotation != null
                ? hasQualityAnnotation
                : (hasQualityAnnotation = new ArrayList<QualityAnnotation>(2)) );
    }

    public void addHasQualityAnnotation(QualityAnnotation hasQualityAnnotation) {
        getHasQualityAnnotations().add(hasQualityAnnotation);
    }

    public ProvidedCHO getAgregatedCHO() {
        return aggregatedCHO;
    }

    protected void setAggregatedCHO(ProvidedCHO cho) {
        this.aggregatedCHO = cho;
    }

    public String toString() { return ("ore:Aggregation<" + id + ">"); }
}