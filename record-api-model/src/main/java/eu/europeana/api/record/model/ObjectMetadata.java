/**
 * 
 */
package eu.europeana.api.record.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import eu.europeana.api.edm.DC;
import eu.europeana.api.edm.DCTerms;
import eu.europeana.api.edm.EDM;
import eu.europeana.api.edm.OWL;
import eu.europeana.api.record.io.json.CompactSerializer;
import eu.europeana.api.record.model.data.DataValue;
import eu.europeana.api.record.model.data.EdmType;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.data.ObjectReference;
import eu.europeana.api.record.model.internal.LanguageMap;
import eu.europeana.api.record.model.internal.LanguageMapArray;
import eu.europeana.jena.encoder.annotation.JenaCollection;
import eu.europeana.jena.encoder.annotation.JenaProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hugo
 * @since 4 Aug 2023
 */
@Entity
public class ObjectMetadata 
{
    // title & descriptions

    @JenaProperty(ns = DC.NS, localName = DC.title)
    @JenaCollection
    @JsonProperty(DC.title)
    @Property(DC.title)
    private LanguageMap title;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.alternative)
    @JenaCollection
    @JsonProperty(DCTerms.alternative)
    @Property(DCTerms.alternative)
    private LanguageMapArray altTitle;

    @JenaProperty(ns = DC.NS, localName = DC.description)
    @JsonProperty(DC.description)
    @Property(DC.description)
    private List<DataValue> description;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.tableOfContents)
    @JsonProperty(DCTerms.tableOfContents)
    @Property(DCTerms.tableOfContents)
    private List<DataValue> tableOfContents;

    // type & language

    @JenaProperty(ns = EDM.NS, localName = EDM.type)
    @JsonProperty(ModelConstants.edmType)
    @Property(ModelConstants.edmType)
    private EdmType edmType;

    @JenaProperty(ns = DC.NS, localName = DC.language)
    @JsonProperty(DC.language)
    @Property(DC.language)
    private List<Literal<String>> language;

    // agents

    @JenaProperty(ns = DC.NS, localName = DC.creator)
    @JsonProperty(DC.creator)
    @Property(DC.creator)
    private List<DataValue> creator;

    @JenaProperty(ns = DC.NS, localName = DC.contributor)
    @JsonProperty(DC.contributor)
    @Property(DC.contributor)
    private List<DataValue> contributor;

    @JenaProperty(ns = DC.NS, localName = DC.publisher)
    @JsonProperty(DC.publisher)
    @Property(DC.publisher)
    private List<DataValue> publisher;

    // time & space

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.created)
    @JsonProperty(DCTerms.created)
    @Property(DCTerms.created)
    private List<DataValue> created;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.issued)
    @JsonProperty(DCTerms.issued)
    @Property(DCTerms.issued)
    private List<DataValue> issued;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.temporal)
    @JsonProperty(DCTerms.temporal)
    @Property(DCTerms.temporal)
    private List<DataValue> temporal;

    @JenaProperty(ns = DC.NS, localName = DC.date)
    @JsonProperty(DC.date)
    @Property(DC.date)
    private List<DataValue> date;
    
    @JenaProperty(ns = EDM.NS, localName = EDM.year)
    @JsonProperty(EDM.year)
    @Property(EDM.year)
    private List<Literal<String>> year;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.spatial)
    @JsonProperty(DCTerms.spatial)
    @Property(DCTerms.spatial)
    private List<DataValue> spatial;

    @JenaProperty(ns = EDM.NS, localName = EDM.currentLocation)
    @JsonProperty(EDM.currentLocation)
    @Property(EDM.currentLocation)
    private DataValue currentLocation;

    @JenaProperty(ns = DC.NS, localName = DC.coverage)
    @JsonProperty(DC.coverage)
    @Property(DC.coverage)
    private List<DataValue> coverage;

    // subject, type and format (concept relations)

    @JenaProperty(ns = DC.NS, localName = DC.subject)
    @JsonProperty(DC.subject)
    @Property(DC.subject)
    private List<DataValue> subject;

    @JenaProperty(ns = DC.NS, localName = DC.type)
    @JsonProperty(ModelConstants.dcType)
    @Property(ModelConstants.dcType)
    private List<DataValue> dcType;

    @JenaProperty(ns = DC.NS, localName = DC.format)
    @JsonProperty(DC.format)
    @Property(DC.format)
    private List<DataValue> format;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.medium)
    @JsonProperty(DCTerms.medium)
    @Property(DCTerms.medium)
    private List<DataValue> medium;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.conformsTo)
    @JsonProperty(DCTerms.conformsTo)
    @Property(DCTerms.conformsTo)
    private List<DataValue> conformsTo;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.extent)
    @JsonProperty(DCTerms.extent)
    @Property(DCTerms.extent)
    private List<DataValue> extent;

    @JenaProperty(ns = EDM.NS, localName = EDM.hasType)
    @JsonProperty(EDM.hasType)
    @Property(EDM.hasType)
    private List<DataValue> hasType;

    @JenaProperty(ns = EDM.NS, localName = EDM.hasMet)
    @JsonProperty(EDM.hasMet)
    @Property(EDM.hasMet)
    private List<DataValue> hasMet;

    @JenaProperty(ns = EDM.NS, localName = EDM.isRelatedTo)
    @JsonProperty(EDM.isRelatedTo)
    @Property(EDM.isRelatedTo)
    private List<DataValue> isRelatedTo;

    // rights and provenance

    @JenaProperty(ns = DC.NS, localName = DC.rights)
    @Property(ModelConstants.dcRights)
    @JsonProperty(ModelConstants.dcRights)
    private List<DataValue> dcRights;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.provenance)
    @Property(DCTerms.provenance)
    @JsonProperty(DCTerms.provenance)
    private List<DataValue> provenance;

    // relations 

    @JenaProperty(ns = EDM.NS, localName = EDM.realizes)
    @Property(EDM.realizes)
    @JsonProperty(EDM.realizes)
    private List<DataValue> realizes;

    @JenaProperty(ns = DC.NS, localName = DC.source)
    @Property(DC.source)
    @JsonProperty(DC.source)
    private List<DataValue> source;

    @JenaProperty(ns = DC.NS, localName = DC.relation)
    @Property(DC.relation)
    @JsonProperty(DC.relation)
    private List<DataValue> relation;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.isPartOf)
    @Property(DCTerms.isPartOf)
    @JsonProperty(DCTerms.isPartOf)
    private List<DataValue> isPartOf;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.hasPart)
    @Property(DCTerms.hasPart)
    @JsonProperty(DCTerms.hasPart)
    private List<DataValue> hasPart;

    @JenaProperty(ns = EDM.NS, localName = EDM.isNextInSequence)
    @Property(EDM.isNextInSequence)
    @JsonProperty(EDM.isNextInSequence)
    private List<ObjectReference> isNextInSequence;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.isFormatOf)
    @Property(DCTerms.isFormatOf)
    @JsonProperty(DCTerms.isFormatOf)
    private List<DataValue> isFormatOf;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.hasFormat)
    @Property(DCTerms.hasFormat)
    @JsonProperty(DCTerms.hasFormat)
    private List<DataValue> hasFormat;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.isVersionOf)
    @Property(DCTerms.isVersionOf)
    @JsonProperty(DCTerms.isVersionOf)
    private List<DataValue> isVersionOf;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.hasVersion)
    @Property(DCTerms.hasVersion)
    @JsonProperty(DCTerms.hasVersion)
    private List<DataValue> hasVersion;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.isReferencedBy)
    @Property(DCTerms.isReferencedBy)
    @JsonProperty(DCTerms.isReferencedBy)
    private List<DataValue> isReferencedBy;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.references)
    @Property(DCTerms.references)
    @JsonProperty(DCTerms.references)
    private List<DataValue> references;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.isReplacedBy)
    @Property(DCTerms.isReplacedBy)
    @JsonProperty(DCTerms.isReplacedBy)
    private List<DataValue> isReplacedBy;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.replaces)
    @Property(DCTerms.replaces)
    @JsonProperty(DCTerms.replaces)
    private List<DataValue> replaces;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.isRequiredBy)
    @Property(DCTerms.isRequiredBy)
    @JsonProperty(DCTerms.isRequiredBy)
    private List<DataValue> isRequiredBy;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.requires)
    @Property(DCTerms.requires)
    @JsonProperty(DCTerms.requires)
    private List<DataValue> requires;

    @JenaProperty(ns = EDM.NS, localName = EDM.incorporates)
    @Property(EDM.incorporates)
    @JsonProperty(EDM.incorporates)
    private List<ObjectReference> incorporates;

    @JenaProperty(ns = EDM.NS, localName = EDM.isDerivativeOf)
    @Property(EDM.isDerivativeOf)
    @JsonProperty(EDM.isDerivativeOf)
    private List<ObjectReference> isDerivativeOf;

    @JenaProperty(ns = EDM.NS, localName = EDM.isRepresentationOf)
    @Property(EDM.isRepresentationOf)
    @JsonProperty(EDM.isRepresentationOf)
    private List<ObjectReference> isRepresentationOf;

    @JenaProperty(ns = EDM.NS, localName = EDM.isSimilarTo)
    @Property(EDM.isSimilarTo)
    @JsonProperty(EDM.isSimilarTo)
    private List<ObjectReference> isSimilarTo;

    @JenaProperty(ns = EDM.NS, localName = EDM.isSuccessorOf)
    @Property(EDM.isSuccessorOf)
    @JsonProperty(EDM.isSuccessorOf)
    private List<ObjectReference> isSuccessorOf;
    
    // identifiers

    @JenaProperty(ns = DC.NS, localName = DC.identifier)
    @JsonProperty(DC.identifier)
    @Property(DC.identifier)
    private List<Literal<String>> identifier;

    @JenaProperty(ns = OWL.NS, localName = OWL.sameAs)
    @JsonProperty(OWL.sameAs)
    @Property(OWL.sameAs)
    @JsonSerialize(using = CompactSerializer.class)
    private List<ObjectReference> sameAs;


    public List<Literal<String>> getTitles()
    {
        return newTitle().getValues();
    }

    public void addTitle(Literal<String> title)
    {
        newTitle().add(title);
    }



    public List<Literal<String>> getAlternativeTitles()
    {
        return newAlternativeTitle().getValues();
    }


    public void addAlternativeTitle(Literal<String> title)
    {
        newAlternativeTitle().add(title);
    }


    public List<DataValue> getDescriptions()
    {
        return ( description != null ? description
                                     : (description = new ArrayList<DataValue>()) );
    }

    public void addDescription(DataValue description)
    {
        getDescriptions().add(description);
    }


    public EdmType getEdmType()
    {
        return edmType;
    }

    public void setType(EdmType edmType)
    {
        this.edmType = edmType;
    }

    
    public List<DataValue> getCreators()
    {
        return ( creator != null ? creator : (creator = new ArrayList<DataValue>()) );
    }

    public void addCreator(DataValue creator)
    {
        getCreators().add(creator);
    }


    public List<DataValue> getSpatials()
    {
        return ( spatial != null ? spatial : (spatial = new ArrayList<DataValue>()) );
    }

    public void addSpatial(DataValue spatial)
    {
        getSpatials().add(spatial);
    }

    public List<Literal<String>> getIdentifiers()
    {
        return ( identifier != null ? identifier 
                                    : (identifier = new ArrayList<Literal<String>>()) );
    }

    public void addIdentifier(Literal<String> identifier)
    {
        getIdentifiers().add(identifier);
    }



    private LanguageMap newTitle()
    {
        return ( title == null ? (title = new LanguageMap()) : title );
    }

    private LanguageMapArray newAlternativeTitle()
    {
        return ( altTitle == null ? (altTitle = new LanguageMapArray()) : altTitle );
    }
}