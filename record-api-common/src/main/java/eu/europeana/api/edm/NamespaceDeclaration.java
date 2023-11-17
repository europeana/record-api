/**
 * 
 */
package eu.europeana.api.edm;

/**
 * @author Hugo
 * @since 19 Oct 2023
 */
public class NamespaceDeclaration
{

    public String prefix;
    public String ns;

    public NamespaceDeclaration(String prefix, String ns)
    {
        this.prefix = prefix;
        this.ns     = ns;
    }

    public String toString() { return (this.prefix + ": " + this.ns); }
}