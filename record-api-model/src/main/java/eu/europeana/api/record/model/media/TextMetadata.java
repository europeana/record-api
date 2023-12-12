/**
 * 
 */
package eu.europeana.api.record.model.media;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import eu.europeana.api.edm.EDM;
import eu.europeana.api.edm.RDF;
import eu.europeana.api.record.io.jena.FulltextFieldCodec;
import eu.europeana.api.record.io.json.CompactSerializer;
import eu.europeana.api.record.io.json.FullTextResourceSerializer;
import eu.europeana.api.record.model.data.DatatypeLiteral;
import eu.europeana.api.record.model.data.EdmType;
import eu.europeana.jena.encoder.annotation.JenaClass;
import eu.europeana.jena.encoder.annotation.JenaCodec;
import eu.europeana.jena.encoder.annotation.JenaProperty;

import static eu.europeana.api.record.model.ModelConstants.*;


/**
 * @author Hugo
 * @since 8 Aug 2023
 */
@JenaClass
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@Entity(discriminator = Text, discriminatorKey = RDF.type)
public class TextMetadata extends TechnicalMetadata {
    
    @JenaProperty(ns = EDM.NS, localName = EDM.spatialResolution)
    @Property(EDM.spatialResolution)
    @JsonProperty(EDM.spatialResolution)
    @JsonSerialize(using = CompactSerializer.class)
    private DatatypeLiteral<Integer> spatialResolution;

    @JenaCodec(using = FulltextFieldCodec.class)
    @Property("fulltext")
    @JsonProperty(additionalType)
    @JsonSerialize(using = FullTextResourceSerializer.class)
    private boolean isFulltextResource;

    public TextMetadata() {}

    public EdmType getType() { return EdmType.TEXT; }

    public DatatypeLiteral<Integer> getSpatialResolution() {
        return this.spatialResolution;
    }

    public void setSpatialResolution(DatatypeLiteral<Integer> spatialResolution) {
        this.spatialResolution = spatialResolution;
    }

    public boolean isFulltextResource() {
        return this.isFulltextResource;
    }

    public void setIsFulltextResource(boolean isFulltextResource) {
        this.isFulltextResource = isFulltextResource;
    }
}
