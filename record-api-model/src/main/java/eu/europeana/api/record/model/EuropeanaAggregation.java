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
import eu.europeana.api.edm.*;
import eu.europeana.api.record.io.json.CompactSerializer;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.data.ObjectReference;
import eu.europeana.api.record.model.media.WebResource;
import eu.europeana.jena.encoder.annotation.JenaProperty;
import eu.europeana.jena.encoder.annotation.JenaResource;
import static eu.europeana.api.record.model.ModelConstants.ID;

/**
 * @author Hugo
 * @since 5 Aug 2023
 */
@JenaResource(ns = EDM.NS, localName = EDM.EuropeanaAggregation)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ ID, RDF.type
    , EDM.dataProvider, EDM.intermediateProvider, EDM.provider
    , EDM.datasetName, EDM.country, EDM.language
    , EDM.object, EDM.preview, EDM.isShownAt, EDM.isShownBy, EDM.hasView
    , EDM.rights, DC.rights, EDM.completeness, EDM.ugc, EDM.landingPage
    , DCTerms.created, DCTerms.modified
    , DQV.hasQualityAnnotation, EDM.aggregatedCHO })
@Entity(discriminator = EDM.EuropeanaAggregation, discriminatorKey = RDF.type)
public class EuropeanaAggregation extends Aggregation
{

    @JenaProperty(ns = EDM.NS, localName = EDM.datasetName)
    @Property(EDM.datasetName)
    @JsonProperty(EDM.datasetName)
    @JsonSerialize(using = CompactSerializer.class)    
    private Literal<String> datasetName = null;

    @JenaProperty(ns = EDM.NS, localName = EDM.country)
    @Property(EDM.country)
    @JsonProperty(EDM.country)
    @JsonSerialize(using = CompactSerializer.class)    
    private Literal<String> country = null;

    @JenaProperty(ns = EDM.NS, localName = EDM.language)
    @Property(EDM.language)
    @JsonProperty(EDM.language)
    @JsonSerialize(using = CompactSerializer.class)    
    private Literal<String> language = null;

    @JenaProperty(ns = EDM.NS, localName = EDM.completeness)
    @Property(EDM.completeness)
    @JsonProperty(EDM.completeness)
    @JsonSerialize(using = CompactSerializer.class)    
    private Literal<Integer> completeness = null;

    @JenaProperty(ns = EDM.NS, localName = EDM.preview)
    @JsonProperty(EDM.preview)
    @Property(EDM.preview)
    private WebResource preview = null;

    @JenaProperty(ns = EDM.NS, localName = EDM.landingPage)
    @Property(EDM.landingPage)
    @JsonProperty(EDM.landingPage)
    @JsonSerialize(using = CompactSerializer.class)    
    private ObjectReference landingPage = null;


    public EuropeanaAggregation(String id) { super(id); }

    public EuropeanaAggregation() {}

    public String getType() { return EDM.EuropeanaAggregation; }

    public Literal<String> getDatasetName() { 
        return this.datasetName; 
    }

    public void setDatasetName(Literal<String> datasetName) {
        this.datasetName = datasetName;
    }

    public Literal<String> getCountry() { 
        return this.country; 
    }

    public void setCountry(Literal<String> country) {
        this.country = country;
    }

    public Literal<String> getLanguage() { 
        return this.country;
    }

    public void setLanguage(Literal<String> language) {
        this.language = language;
    }

    public WebResource getPreview() { 
        return this.preview;
    }

    public void setPreview(WebResource preview) {
        this.preview = preview;
    }

    public ObjectReference getLandingPage() { 
        return this.landingPage;
    }

    public void setLandingPage(ObjectReference landingPage) {
        this.landingPage = landingPage;
    }

    public String toString() { return ("edm:EuropeanaAggregation<" + id + ">"); }
}
