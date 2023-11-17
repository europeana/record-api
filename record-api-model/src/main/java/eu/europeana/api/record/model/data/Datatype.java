/**
 * 
 */
package eu.europeana.api.record.model.data;

/**
 * @author Hugo
 * @since 9 Nov 2023
 */
public class Datatype {

    private String uri;

    private String qname;

    public Datatype(String uri, String qname) {
        this.uri   = uri;
        this.qname = qname;
    }

    public String getURI()   { return uri;   }

    public String getQName() { return qname; }

    public String toString() { return uri;   }
}
