package eu.europeana.api.edm;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 14 Apr 2016
 */
public interface CC
{
    public static final String PREFIX = "cc";
    public static final String NS     = "http://creativecommons.org/ns#";
    public static final NamespaceDeclaration DECL = new NamespaceDeclaration(PREFIX, NS);

    public static final String License      = "License";
    public static final String deprecatedOn = "deprecatedOn";
}
