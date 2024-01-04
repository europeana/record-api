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
@Entity(discriminator = ModelConstants.Audio, discriminatorKey = RDF.type)
public class AudioMetadata extends TechnicalMetadata {

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

    @JenaProperty(ns = EBUCORE.NS, localName = EBUCORE.sampleSize)
    @Property(EBUCORE.sampleSize)
    @JsonProperty(EBUCORE.sampleSize)
    @JsonSerialize(using = CompactSerializer.class)
    private DatatypeLiteral<Integer> sampleSize;

    @JenaProperty(ns = EBUCORE.NS, localName = EBUCORE.sampleRate)
    @Property(EBUCORE.sampleRate)
    @JsonProperty(EBUCORE.sampleRate)
    @JsonSerialize(using = CompactSerializer.class)
    private DatatypeLiteral<Integer> sampleRate;

    @JenaProperty(ns = EBUCORE.NS, localName = EBUCORE.bitRate)
    @Property(EBUCORE.bitRate)
    @JsonProperty(EBUCORE.bitRate)
    @JsonSerialize(using = CompactSerializer.class)
    private DatatypeLiteral<Integer> bitRate;

    @JenaProperty(ns = EBUCORE.NS, localName = EBUCORE.audioChannelNumber)
    @Property(EBUCORE.audioChannelNumber)
    @JsonProperty(EBUCORE.audioChannelNumber)
    @JsonSerialize(using = CompactSerializer.class)
    private DatatypeLiteral<Integer> audioChannelNumber;

    public EdmType getType() { return EdmType.SOUND; }

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

    public DatatypeLiteral<Integer> getSampleSize() {
        return this.sampleSize;
    }

    public void setSampleSize(DatatypeLiteral<Integer> sampleSize) {
        this.sampleSize = sampleSize;
    }

    public DatatypeLiteral<Integer> getSampleRate() {
        return this.sampleRate;
    }

    public void setSampleRate(DatatypeLiteral<Integer> sampleRate) {
        this.sampleRate = sampleRate;
    }

    public DatatypeLiteral<Integer> getBitRate() {
        return this.bitRate;
    }

    public void setBitRate(DatatypeLiteral<Integer> bitRate) {
        this.bitRate = bitRate;
    }

    public DatatypeLiteral<Integer> getAudioChannelNumber() {
        return this.audioChannelNumber;
    }

    public void setAudioChannelNumber(DatatypeLiteral<Integer> audioChannelNumber) {
        this.audioChannelNumber = audioChannelNumber;
    }
}