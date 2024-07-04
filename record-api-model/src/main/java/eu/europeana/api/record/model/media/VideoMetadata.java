/**
 * 
 */
package eu.europeana.api.record.model.media;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import eu.europeana.api.edm.EBUCORE;
import eu.europeana.api.edm.EDM;
import eu.europeana.api.edm.RDF;
import eu.europeana.api.record.io.json.CompactSerializer;
import eu.europeana.api.record.model.ModelConstants;
import eu.europeana.api.record.model.data.DatatypeLiteral;
import eu.europeana.api.record.model.data.EdmType;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.jena.encoder.annotation.JenaClass;
import eu.europeana.jena.encoder.annotation.JenaProperty;

/**
 * @author Hugo
 * @since 8 Aug 2023
 */
@JenaClass
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@Entity(discriminator = ModelConstants.Video, discriminatorKey = RDF.type)
public class VideoMetadata extends TechnicalMetadata {
    
    @JenaProperty(ns = EBUCORE.NS, localName = EBUCORE.width)
    @Property(EBUCORE.width)
    @JsonProperty(EBUCORE.width)
    @JsonSerialize(using = CompactSerializer.class)
    private DatatypeLiteral<Integer> width;

    @JenaProperty(ns = EBUCORE.NS, localName = EBUCORE.height)
    @Property(EBUCORE.height)
    @JsonProperty(EBUCORE.height)
    @JsonSerialize(using = CompactSerializer.class)
    private DatatypeLiteral<Integer> height;

    @JenaProperty(ns = EBUCORE.NS, localName = EBUCORE.orientation)
    @Property(EBUCORE.orientation)
    @JsonProperty(EBUCORE.orientation)
    @JsonSerialize(using = CompactSerializer.class)
    private Literal<ImageOrientation> orientation;

    @JenaProperty(ns = EBUCORE.NS, localName = EBUCORE.duration)
    @Property(EBUCORE.duration)
    @JsonProperty(EBUCORE.duration)
    @JsonSerialize(using = CompactSerializer.class)
    private Literal<Integer> duration;

    @JenaProperty(ns = EDM.NS, localName = EDM.codecName)
    @Property(EDM.codecName)
    @JsonProperty(EDM.codecName)
    @JsonSerialize(using = CompactSerializer.class)
    private Literal<String> codecName;

    @JenaProperty(ns = EBUCORE.NS, localName = EBUCORE.bitRate)
    @Property(EBUCORE.bitRate)
    @JsonProperty(EBUCORE.bitRate)
    @JsonSerialize(using = CompactSerializer.class)
    private DatatypeLiteral<Integer> bitRate;

    @JenaProperty(ns = EBUCORE.NS, localName = EBUCORE.frameRate)
    @Property(EBUCORE.frameRate)
    @JsonProperty(EBUCORE.frameRate)
    @JsonSerialize(using = CompactSerializer.class)
    private DatatypeLiteral<Double> frameRate;

    public VideoMetadata() {}

    public EdmType getType() { return EdmType.VIDEO; }

    public DatatypeLiteral<Integer> getWidth() {
        return this.width;
    }

    public void setWidth(DatatypeLiteral<Integer> width) {
        this.width = width;
    }

    public DatatypeLiteral<Integer> getHeight() {
        return this.height;
    }

    public void setHeight(DatatypeLiteral<Integer> height) {
        this.height = height;
    }

    public Literal<Integer> getDuration() {
        return this.duration;
    }

    public void setDuration(Literal<Integer> duration) {
        this.duration = duration;
    }

    public Literal<String> getCodecName() {
        return this.codecName;
    }

    public void setCodecName(Literal<String> codecName) {
        this.codecName = codecName;
    }

    public DatatypeLiteral<Integer> getBitRate() {
        return this.bitRate;
    }

    public void setBitRate(DatatypeLiteral<Integer> bitRate) {
        this.bitRate = bitRate;
    }

    public DatatypeLiteral<Double> getFrameRate() {
        return this.frameRate;
    }

    public void setFrameRate(DatatypeLiteral<Double> frameRate) {
        this.frameRate = frameRate;
    }
}