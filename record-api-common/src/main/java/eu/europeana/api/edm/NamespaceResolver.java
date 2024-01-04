/**
 * 
 */
package eu.europeana.api.edm;

/**
 * @author Hugo
 * @since 19 Oct 2023
 */
public interface NamespaceResolver
{
    public NamespaceDeclaration getDeclarationByPrefix(String prefix);

    public NamespaceDeclaration getDeclarationByNamespace(String nsURI);
}
