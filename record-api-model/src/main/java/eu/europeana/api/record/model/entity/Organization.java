package eu.europeana.api.record.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import dev.morphia.annotations.Entity;
import eu.europeana.api.edm.FOAF;
import eu.europeana.api.edm.RDF;
import eu.europeana.api.edm.SKOS;
import eu.europeana.jena.encoder.annotation.JenaResource;

import static eu.europeana.api.record.model.ModelConstants.id;
import static eu.europeana.api.record.model.ModelConstants.ContextualEntity;

/**
 * @author Hugo
 * @since 5 Aug 2023
 */
@JenaResource(ns = FOAF.NS, localName = FOAF.Organization)
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@JsonPropertyOrder({id, RDF.type, SKOS.prefLabel, SKOS.altLabel })
@Entity(value = ContextualEntity, discriminator = FOAF.Organization
        , discriminatorKey = RDF.type)
public class Organization extends ContextualEntity {

    public Organization() {}

    public Organization(String id) { super(id); }

    public String getType() { return FOAF.Organization; }

    //note


    public String toString() { return ("foaf:Organization<" + id + ">"); }
}
