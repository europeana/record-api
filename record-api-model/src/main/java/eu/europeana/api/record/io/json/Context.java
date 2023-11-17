/**
 * 
 */
package eu.europeana.api.record.io.json;

/**
 * @author Hugo
 * @since 15 Sep 2023
 */
public class Context
{
    private String base;

    public Context(String uri) { base = uri; }

    public String getURI()  { return "https://www.europeana.eu/schemas/context/edm.jsonld"; }

    public String getBase() { return base; }
}