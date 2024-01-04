package eu.europeana.jena.edm;

import org.apache.jena.rdf.model.Property;

import static org.apache.jena.rdf.model.ResourceFactory.*;
import static org.apache.jena.rdf.model.ResourceFactory.createProperty;

/**
 * @author Hugo Manguinhas <hugo.manguinhas@europeana.eu>
 * @since 14 Apr 2016
 */
public class RDAGR2 
{
    public static final String PREFIX = "rdaGr2";
    public static final String NS     = "http://rdvocab.info/ElementsGr2/";

    public static final Property biographicalInformation 
        = createProperty(NS, "biographicalInformation");
    public static final Property dateOfBirth
        = createProperty(NS, "dateOfBirth");
    public static final Property dateOfDeath
        = createProperty(NS, "dateOfDeath");
    public static final Property dateOfEstablishment
        = createProperty(NS, "dateOfEstablishment");
    public static final Property dateOfTermination
        = createProperty(NS, "dateOfTermination");
    public static final Property gender
        = createProperty(NS, "gender");
    public static final Property placeOfBirth
        = createProperty(NS, "placeOfBirth");
    public static final Property placeOfDeath
        = createProperty(NS, "placeOfDeath");
    public static final Property professionOrOccupation
        = createProperty(NS, "professionOrOccupation");
}
