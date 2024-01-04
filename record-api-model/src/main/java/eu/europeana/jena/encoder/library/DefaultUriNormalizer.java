/**
 * 
 */
package eu.europeana.jena.encoder.library;

import org.apache.commons.codec.binary.StringUtils;

/**
 * @author Hugo
 * @since 27 Oct 2023
 */
public class DefaultUriNormalizer implements ResourceUriNormalizer {

    public static final DefaultUriNormalizer INSTANCE = new DefaultUriNormalizer();

    @Override
    public String compact(String uri, String base) {
        if ( base == null ) { return uri; }

        if ( !uri.startsWith(base) ) { return uri; }

        if ( StringUtils.equals(uri, base) ) { return uri; }

        return uri.substring(base.length());
    }

    @Override
    public String expand(String uri, String base) {
        if ( base == null ) { return uri; }

        if ( uri.startsWith("#") || uri.startsWith("/") ) {
            return base + uri;
        }
        return uri;
    }
}
