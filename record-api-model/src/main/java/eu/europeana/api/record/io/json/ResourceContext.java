package eu.europeana.api.record.io.json;

/**
 * @author Hugo
 * @since 15 Sep 2023
 */
public class ResourceContext {

    private String[] importURIs;
    private String baseURI;

    public ResourceContext(String baseURI, String... importURIs) {
        this.importURIs = importURIs;
        this.baseURI    = baseURI; 
    }

    public String[] getImportURIs()  { return importURIs; }

    public boolean hasImportURIs() { 
        return ( importURIs != null && importURIs.length > 1 ); 
    }

    public String getBase() { return baseURI; }

    public boolean hasBase() { return baseURI != null; }
}