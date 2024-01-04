/**
 * 
 */
package eu.europeana.jena.encoder.library;

/**
 * @author Hugo
 * @since 27 Oct 2023
 */
public interface ResourceUriNormalizer {

    public String compact(String uri, String base);

    public String expand(String uri, String base);
}
