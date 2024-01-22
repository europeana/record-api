/**
 * 
 */
package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import dev.morphia.annotations.Transient;
import eu.europeana.api.edm.*;
import eu.europeana.api.record.io.json.EDMClassReferenceSerializer;
import eu.europeana.jena.encoder.annotation.JenaId;
import eu.europeana.jena.encoder.annotation.JenaProperty;
import eu.europeana.jena.encoder.annotation.JenaResource;

import java.util.ArrayList;
import java.util.List;
import static eu.europeana.api.record.model.ModelConstants.*;

/**
 * @author Hugo
 * @since 5 Aug 2023
 */
@JenaResource(ns = ORE.NS, localName = ORE.Proxy)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY
            , visible = true, property = RDF.type)
@JsonPropertyOrder({id, RDF.type
                   , DC.title, DCTerms.alternative, DC.description
                   , DCTerms.tableOfContents, edmType, DC.language
                   , DC.creator, DC.contributor, DC.publisher
                   , DCTerms.created, DCTerms.issued, DCTerms.temporal, DC.date, EDM.year
                   , DCTerms.spatial, EDM.currentLocation, DC.coverage
                   , DC.subject, dcType, DC.format, DCTerms.medium
                   , DCTerms.conformsTo, DCTerms.extent, EDM.hasType, EDM.hasMet
                   , EDM.isRelatedTo, DC.rights, DCTerms.provenance, EDM.realizes
                   , DC.source, DC.relation, DCTerms.isPartOf, DCTerms.hasPart
                   , EDM.isNextInSequence, DCTerms.isFormatOf, DCTerms.hasFormat
                   , DCTerms.isVersionOf, DCTerms.hasVersion, DCTerms.isReferencedBy
                   , DCTerms.references, DCTerms.isReplacedBy, DCTerms.replaces
                   , DCTerms.isRequiredBy, DCTerms.requires, EDM.incorporates
                   , EDM.isDerivativeOf, EDM.isRepresentationOf, EDM.isSimilarTo
                   , EDM.isSuccessorOf, DC.identifier, OWL.sameAs
                   , ORE.proxyIn, ORE.lineage, ORE.proxyFor })
@Entity(discriminator = ORE.Proxy, discriminatorKey = RDF.type)
public class Proxy extends ObjectMetadata implements EDMClass
{
    @JenaId
    @JsonProperty(ModelConstants.id)
    @Property(ModelConstants.id)
    private String      id;

    @JenaProperty(ns = ORE.NS, localName = ORE.proxyIn)
    @JsonProperty(ORE.proxyIn)
    @Property(ORE.proxyIn)
    private Aggregation proxyIn;

    @JenaProperty(ns = ORE.NS, localName = ORE.lineage)
    @JsonProperty(ORE.lineage)
    @JsonSerialize(using = EDMClassReferenceSerializer.class)
    @Transient
    private List<Proxy> lineage;

    @JenaProperty(ns = ORE.NS, localName = ORE.proxyFor)
    @JsonProperty(ORE.proxyFor)
    @JsonSerialize(using = EDMClassReferenceSerializer.class)
    @Transient
    private ProvidedCHO proxyFor;

    public Proxy(String id) { this.id = id; }

    public Proxy() {}

    public String getID() { return id; }

    public String getType() { return ORE.Proxy; }

    public boolean isEuropeanaProxy()
    {
        return ( (this.proxyIn != null)
              && (this.proxyIn instanceof EuropeanaAggregation) );
    }

    public ProvidedCHO getProxyFor() {
        return proxyFor;
    }

    protected void setProxyFor(ProvidedCHO cho) {
        this.proxyFor = cho;
    }


    public boolean hasLineages() { return lineage != null && !lineage.isEmpty(); }

    
    public List<Proxy> getLineages()
    {
        return ( lineage != null ? lineage
                                 : (lineage = new ArrayList<Proxy>()) );
    }

    public void addLineage(Proxy lineage)
    {
        getLineages().add(lineage);
    }


    public Aggregation getProxyIn() {
        return proxyIn;
    }

    public void setProxyIn(Aggregation aggr)
    {
        this.proxyIn = aggr;
    }

    public String toString() { return ("ore:Proxy<" + id + ">"); }
}
