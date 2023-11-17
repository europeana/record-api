/**
 * 
 */
package eu.europeana.api.record.model.media;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;
import eu.europeana.api.edm.DCTerms;
import eu.europeana.api.edm.DOAP;
import eu.europeana.api.edm.RDF;
import eu.europeana.api.edm.SVCS;
import eu.europeana.api.record.model.EDMClass;
import eu.europeana.api.record.model.data.ObjectReference;
import eu.europeana.jena.encoder.annotation.JenaId;
import eu.europeana.jena.encoder.annotation.JenaProperty;
import eu.europeana.jena.encoder.annotation.JenaResource;
import static eu.europeana.api.record.model.ModelConstants.ID;



/**
 * @author Hugo
 * @since 5 Aug 2023
 */
@JenaResource(ns = SVCS.NS, localName = SVCS.Service)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({ ID, RDF.type
                   , DCTerms.conformsTo, DOAP.impls })
@Entity(discriminator = SVCS.Service, discriminatorKey = RDF.type)
public class Service implements EDMClass {

    @JenaId
    @JsonProperty(ID)
    @Property(ID)
    protected String id;

    @JenaProperty(ns = DCTerms.NS, localName = DCTerms.conformsTo)
    @Property(DCTerms.conformsTo)
    @JsonProperty(DCTerms.conformsTo)
    private ObjectReference conformsTo;

    @JenaProperty(ns = DOAP.NS, localName = DOAP.impls)
    @Property(DOAP.impls)
    @JsonProperty(DOAP.impls)
    private ObjectReference impls;

    public Service() {}

    public Service(String id) { this.id = id; }

    public String getID() { return id; }

    public String getType() { return SVCS.Service; }

    public ObjectReference getConformsTo() {
        return this.conformsTo;
    }

    public void setConformsTo(ObjectReference conformsTo) {
        this.conformsTo = conformsTo;
    }

    public ObjectReference getImplements() {
        return this.impls;
    }

    public void setImplements(ObjectReference impls) {
        this.impls = impls;
    }

    public String toString() { return ("svcs:Service<" + id + ">"); }
}
