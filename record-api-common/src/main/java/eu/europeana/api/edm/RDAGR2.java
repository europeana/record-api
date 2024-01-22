package eu.europeana.api.edm;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 14 Apr 2016
 */
public interface RDAGR2 
{
    public static final String PREFIX = "rdaGr2";
    public static final String NS     = "http://rdvocab.info/ElementsGr2/";
    public static final NamespaceDeclaration DECL = new NamespaceDeclaration(PREFIX, NS);

    public static final String biographicalInformation = "biographicalInformation";
    public static final String dateOfBirth             = "dateOfBirth";
    public static final String dateOfDeath             = "dateOfDeath";
    public static final String dateOfEstablishment     = "dateOfEstablishment";
    public static final String dateOfTermination       = "dateOfTermination";
    public static final String gender                  = "gender";
    public static final String placeOfBirth            = "placeOfBirth";
    public static final String placeOfDeath            = "placeOfDeath";
    public static final String professionOrOccupation  = "professionOrOccupation";
}
