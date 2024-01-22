/**
 * 
 */
package eu.europeana.api.edm;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 15 Apr 2016
 */
public interface EBUCORE
{
    public static final String PREFIX = "ebucore";
    public static final String NS
        = "http://www.ebu.ch/metadata/ontologies/ebucore/ebucore#";
    public static final NamespaceDeclaration DECL = new NamespaceDeclaration(PREFIX, NS);

    public static final String audioChannelNumber = "audioChannelNumber";
    public static final String bitRate            = "bitRate";
    public static final String duration           = "duration";
    public static final String fileByteSize       = "fileByteSize";
    public static final String frameRate          = "frameRate";
    public static final String hasMimeType        = "hasMimeType";
    public static final String height             = "height";
    public static final String orientation        = "orientation";
    public static final String sampleRate         = "sampleRate";
    public static final String sampleSize         = "sampleSize";
    public static final String width              = "width";
    
    
    
}
