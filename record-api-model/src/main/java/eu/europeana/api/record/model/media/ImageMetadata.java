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
import eu.europeana.api.record.model.data.DatatypeLiteral;
import eu.europeana.api.record.model.data.EdmType;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.jena.encoder.annotation.JenaClass;
import eu.europeana.jena.encoder.annotation.JenaProperty;

import java.util.ArrayList;
import java.util.List;

import static eu.europeana.api.record.model.ModelConstants.IMAGE;

/**
 * @author Hugo
 * @since 8 Aug 2023
 */
@JenaClass
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@Entity(discriminator = IMAGE, discriminatorKey = RDF.type)
public class ImageMetadata extends TechnicalMetadata {

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

    @JenaProperty(ns = EDM.NS, localName = EDM.hasColorSpace)
    @Property(EDM.hasColorSpace)
    @JsonProperty(EDM.hasColorSpace)
    @JsonSerialize(using = CompactSerializer.class)
    private Literal<String> hasColorSpace;

    //should we use java.awt.Color? 
    //Color.decode()
    //String.format("#%06x", color.getRGB() & 0x00FFFFFF)
    @JenaProperty(ns = EDM.NS, localName = EDM.componentColor)
    @Property(EDM.componentColor)
    @JsonProperty(EDM.componentColor)
    @JsonSerialize(using = CompactSerializer.class)
    private List<Literal<String>> componentColor;

    @JenaProperty(ns = EBUCORE.NS, localName = EBUCORE.orientation)
    @Property(EBUCORE.orientation)
    @JsonProperty(EBUCORE.orientation)
    @JsonSerialize(using = CompactSerializer.class)
    private Literal<ImageOrientation> orientation;

    public EdmType getType() { return EdmType.IMAGE; }

    public DatatypeLiteral<Integer> getWidth()
    {
        return this.width;
    }

    public void setWidth(DatatypeLiteral<Integer> width)
    {
        this.width = width;
    }

    public DatatypeLiteral<Integer> getHeight()
    {
        return this.height;
    }

    public void setHeight(DatatypeLiteral<Integer> height)
    {
        this.height = height;
    }

    public Literal<String> getHasColorSpace()
    {
        return this.hasColorSpace;
    }

    public void setHasColorSpace(Literal<String> hasColorSpace)
    {
        this.hasColorSpace = hasColorSpace;
    }


    public List<Literal<String>> getComponentColors()
    {
        return ( componentColor != null ? componentColor
                                     : (componentColor = new ArrayList<Literal<String>>()) );
    }

    public void addComponentColor(Literal<String> componentColor)
    {
        getComponentColors().add(componentColor);
    }


    public Literal<ImageOrientation> getOrientation()
    {
        return this.orientation;
    }

    public void setOrientation(Literal<ImageOrientation> orientation)
    {
        this.orientation = orientation;
    }
}
