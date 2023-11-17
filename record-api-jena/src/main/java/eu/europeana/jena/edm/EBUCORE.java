/**
 * 
 */
package eu.europeana.jena.edm;

import org.apache.jena.rdf.model.Property;

import static org.apache.jena.rdf.model.ResourceFactory.createProperty;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 15 Apr 2016
 */
public class EBUCORE
{
    public static final String PREFIX = "ebucore";
    public static final String NS
        = "http://www.ebu.ch/metadata/ontologies/ebucore/ebucore#";

    public static final Property audioChannelNumber
        = createProperty(NS + "audioChannelNumber");
    public static final Property bitRate      = createProperty(NS + "bitRate");
    public static final Property duration     = createProperty(NS + "duration");
    public static final Property fileByteSize = createProperty(NS + "fileByteSize");
    public static final Property frameRate    = createProperty(NS + "frameRate");
    public static final Property hasMimeType  = createProperty(NS + "hasMimeType");
    public static final Property height       = createProperty(NS + "height");
    public static final Property orientation  = createProperty(NS + "orientation");
    public static final Property sampleRate   = createProperty(NS + "sampleRate");
    public static final Property sampleSize   = createProperty(NS + "sampleSize");
    public static final Property width        = createProperty(NS + "width");
    
    
    
}
