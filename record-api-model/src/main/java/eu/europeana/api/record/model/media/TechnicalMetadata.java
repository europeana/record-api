/**
 * 
 */
package eu.europeana.api.record.model.media;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import eu.europeana.api.edm.EBUCORE;
import eu.europeana.api.edm.RDF;
import eu.europeana.api.record.io.json.CompactSerializer;
import eu.europeana.api.record.model.data.EdmType;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.jena.encoder.annotation.JenaClass;
import eu.europeana.jena.encoder.annotation.JenaProperty;

/**
 * @author Hugo
 * @since 8 Aug 2023
 */
@JenaClass
@Entity(discriminatorKey = RDF.type)
public class TechnicalMetadata {

    @JenaProperty(ns = EBUCORE.NS, localName = EBUCORE.hasMimeType)
    @Property(EBUCORE.hasMimeType)
    @JsonProperty(EBUCORE.hasMimeType)
    @JsonSerialize(using = CompactSerializer.class)
    private Literal<String> mimetype;

    @JenaProperty(ns = EBUCORE.NS, localName = EBUCORE.fileByteSize)
    @Property(EBUCORE.fileByteSize)
    @JsonProperty(EBUCORE.fileByteSize)
    @JsonSerialize(using = CompactSerializer.class)
    private Literal<Long>   fileByteSize;

    public TechnicalMetadata() {}

    public EdmType getType() { return null; }

    public Literal<String> getMimetype() {
        return this.mimetype;
    }

    public void setMimetype(Literal<String> mimetype) {
        this.mimetype = mimetype;
    }

    public void setFileByteSize(Literal<Long> fileByteSize) {
        this.fileByteSize = fileByteSize;
    }

    public Literal<Long> getFileByteSize() {
        return this.fileByteSize;
    }

    public String toString() { 
        EdmType type = getType();
        return ((type == null ? "TechnicalMetadata" : type.toString())
             + "<" + this.mimetype + ">"); 
    }
}