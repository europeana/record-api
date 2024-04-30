package eu.europeana.api.format;

import java.nio.charset.Charset;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Hugo
 * @since 13 Oct 2023
 */
public enum RdfFormat {
    JSONLD("jsonld","json",null,"application/ld+json")
  , JSON("json","json",null,"application/json")
  , XML("rdf","xml","utf-8","application/rdf+xml","application/xml","text/xml","rdf/xml")
  , TURTLE("ttl",null,"utf-8","text/turtle","application/turtle","application/x-turtle")
  , N3("n3",null,"utf-8","text/n3","text/rdf+n3","application/n3")
  , NT("nt",null,null,"application/n-triples","application/ntriples","text/nt");

  public static RdfFormat getFormatByExtension(String extension) {
    for ( RdfFormat format : RdfFormat.values() ) {
      if ( format.acceptsExtension(extension) ) { return format; }
    }
    return null;
  }

  public static RdfFormat getFormatByMediaType(String mediaType) {
    if(StringUtils.isNotEmpty(mediaType)) {
      for (RdfFormat format : RdfFormat.values()) {
        if (format.acceptsMediaType(mediaType)) {
          return format;
        }
      }
    }
    return null;
  }

  private String   extension;
  private String   alternative;
  private String   charset;
  private String[] mediaTypes;

  RdfFormat(String extension, String alternative, String charset
      , String... mediaTypes) {
    this.extension   = extension;
    this.alternative = alternative;
    this.charset     = charset;
    this.mediaTypes  = mediaTypes;
  }

  public String   getExtension()   { return extension;     }

  public String   getAlternative() { return alternative;   }

  public String   getCharset()     { return charset;       }

  public String   getMediaType()   { return mediaTypes[0]; }

  public Charset getCharsetObject(){
    return StringUtils.isNotEmpty(charset)?Charset.forName(charset):null;
  }

  public boolean acceptsExtension(String extension) {
    return ( this.extension.equals(extension)
        || ( this.alternative != null && this.alternative.equals(extension)));
  }

  public boolean acceptsMediaType(String mediaType) {
    for ( String mType : mediaTypes ) {
      if (mType.equals(mediaType)) { return true; }
    }
    return false;
  }
}