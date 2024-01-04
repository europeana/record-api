/**
 * 
 */
package eu.europeana.api.edm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hugo
 * @since 12 Oct 2023
 */
public class Namespaces implements NamespaceResolver
{
    private static Map<String,NamespaceDeclaration> PREFIXES   = new HashMap<String, NamespaceDeclaration>();
    private static Map<String,NamespaceDeclaration> NAMESPACES = new HashMap<String, NamespaceDeclaration>();

    static {
        addDeclaration(CC.DECL);
        addDeclaration(DC.DECL);
        addDeclaration(DCTerms.DECL);
        addDeclaration(DOAP.DECL);
        addDeclaration(DQV.DECL);
        addDeclaration(EBUCORE.DECL);
        addDeclaration(EDM.DECL);
        addDeclaration(FOAF.DECL);
        addDeclaration(OA.DECL);
        addDeclaration(ODRL.DECL);
        addDeclaration(ORE.DECL);
        addDeclaration(OWL.DECL);
        addDeclaration(RDAGR2.DECL);
        addDeclaration(RDF.DECL);
        addDeclaration(SKOS.DECL);
        addDeclaration(SVCS.DECL);
        addDeclaration(VCARD.DECL);
        addDeclaration(WGS84.DECL);
        addDeclaration(XSD.DECL);
    }

    private static void addDeclaration(NamespaceDeclaration decl) {
        PREFIXES.put(decl.prefix, decl);
        NAMESPACES.put(decl.ns, decl);
    }

    public static NamespaceResolver getNamespaceResolver() {
        return new Namespaces();
    }

    public static Collection<NamespaceDeclaration> getNamespaces() {
        return NAMESPACES.values();
    }

    @Override
    public NamespaceDeclaration getDeclarationByPrefix(String prefix) {
        return PREFIXES.get(prefix);
    }

    @Override
    public NamespaceDeclaration getDeclarationByNamespace(String nsURI) {
        return NAMESPACES.get(nsURI);
    }
}
