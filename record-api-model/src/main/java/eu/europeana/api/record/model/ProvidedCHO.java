/**
 * 
 */
package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonAppend;
import dev.morphia.Datastore;
import dev.morphia.annotations.*;
import eu.europeana.api.edm.*;
import eu.europeana.jena.encoder.annotation.JenaId;
import eu.europeana.jena.encoder.annotation.JenaProperty;
import eu.europeana.jena.encoder.annotation.JenaResource;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

import static eu.europeana.api.record.model.ModelConstants.*;

/**
 * @author Hugo
 * @since 4 Aug 2023
 */
@JenaResource(ns = EDM.NS, localName = EDM.ProvidedCHO)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonAppend(prepend = true, attrs = { @JsonAppend.Attr(value = context) })
@JsonPropertyOrder({ id, RDF.type
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
                   , proxies, ORE.isAggregatedBy })
@Entity(value = Record, discriminator = EDM.ProvidedCHO, discriminatorKey = RDF.type)
public class ProvidedCHO extends ObjectMetadata implements EDMClass
{
    @Id
    private ObjectId    objID;

    @JenaId
    @JsonProperty(ModelConstants.id)
    @Indexed(options = @IndexOptions(name="idx_id", unique = true))
    @Property(ModelConstants.id)
    private String      id;

    @JenaProperty(ns = ORE.NS, localName = ORE.proxyFor, inverse = true)
//    @JsonManagedReference
    @JsonProperty(ModelConstants.proxies)
    @Property(ModelConstants.proxies)
    private List<Proxy> proxies = new ArrayList();

    @JenaProperty(ns = ORE.NS, localName = ORE.isAggregatedBy)
    @JsonProperty(ORE.isAggregatedBy)
    @Property(ORE.isAggregatedBy)
    private Aggregation isAggregatedBy;

    public ProvidedCHO() {}

    public String getID() { return id; }

    public void setID(String id) { this.id = id; }

    public String getType() { return EDM.ProvidedCHO; }
    
    public List<Proxy> getProxies() { return this.proxies; }

    public Proxy getProviderProxy() {
        return this.proxies.get(this.proxies.size()-1);
    }

    public void addProxy(Proxy proxy) {
        this.proxies.add(proxy);
        proxy.setProxyFor(this);
    }


    public Aggregation getIsAggregatedBy() {
        return isAggregatedBy;
    }

    public void setIsAggregatedBy(Aggregation aggr) {
        isAggregatedBy = aggr;
    }

    public String toString() { return ("edm:ProvidedCHO<" + id + ">"); }

    @PostLoad
    public void postLoad(Document document, Datastore ds) {
        Proxy last = null;
        for ( Proxy proxy : getProxies() ) {

            proxy.setProxyFor(this);
            Aggregation aggr = proxy.getProxyIn();
            if ( aggr != null ) { aggr.setAggregatedCHO(this); }

            if ( last != null ) { last.addLineage(proxy); }
            last = proxy;
        }
    }
}
