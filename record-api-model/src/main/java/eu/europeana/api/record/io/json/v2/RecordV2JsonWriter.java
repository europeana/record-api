/**
 * 
 */
package eu.europeana.api.record.io.json.v2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;

import eu.europeana.api.record.io.json.v2.EdmJsonV2Definitions.ClassDefinition;
import eu.europeana.api.record.io.json.v2.EdmJsonV2Definitions.FieldDefinition;
import eu.europeana.api.record.io.json.v2.EdmJsonV2Definitions.FieldType;
import eu.europeana.jena.edm.CC;
import eu.europeana.jena.edm.DQV;
import eu.europeana.jena.edm.EDM;
import eu.europeana.jena.edm.ORE;
import eu.europeana.jena.encoder.utils.JenaUtils;

import static eu.europeana.api.record.io.json.v2.EdmJsonV2Definitions.*;

/**
 * @author Hugo
 * @since 15 Nov 2023
 */
public class RecordV2JsonWriter {

    private JsonFactory   jsonFactory;
    private String        choUri;
    private Model         model;
    private JsonGenerator jgen;

    public RecordV2JsonWriter() {
        jsonFactory = new JsonFactory();
    }

    public void write(Model m, OutputStream out) throws IOException {
        generate(m, jsonFactory.createGenerator(out));
    }

    private void generate(Model m, JsonGenerator jgen) throws IOException {
        this.jgen   = jgen;
        this.model  = m;

        try {
            jgen.writeStartObject();
            jgen.writeBooleanField(success, true);
            jgen.writeNumberField(statsDuration, -1);
            jgen.writeNumberField(requestNumber, 999);
            ResIterator iter = m.listResourcesWithProperty(RDF.type, EDM.ProvidedCHO);
            try {
                if (iter.hasNext()) { generate(iter.next(), jgen); }
            }
            finally { iter.close(); }
            jgen.writeEndObject();
            jgen.flush();
        }
        finally {
            this.jgen   = null;
            this.model  = null;
            this.choUri = null;
        }
    }

    private void generate(Resource cho, JsonGenerator jgen) throws IOException {
        this.choUri = cho.getURI();

        jgen.writeFieldName(object);
        jgen.writeStartObject();
        jgen.writeStringField(about, compactURI(choUri));

        Resource eAggr = getEuropeanaAggregation();

        generateResources(getDefinition(EDM.Agent));
        generateAggregations(getDefinition(ORE.Aggregation));
        generateResources(getDefinition(SKOS.Concept));
        generateDatasetName(eAggr);
        generateSingleResource(getDefinition(EDM.EuropeanaAggregation));
        generateCollectionName(eAggr);
        generateCompleteness(eAggr);
        generateResources(getDefinition(CC.License));
        generateResources(getDefinition(FOAF.Organization));
        generateResources(getDefinition(EDM.Place));
        generateResources(getDefinition(EDM.ProvidedCHO));
        generateResources(getDefinition(ORE.Proxy));
        generateResources(getDefinition(DQV.QualityAnnotation));
        generateResources(getDefinition(EDM.TimeSpan));
        generateTimestamps(eAggr);
        generateType(eAggr);

        jgen.writeEndObject();
    }

    private Resource getEuropeanaAggregation() {
        ResIterator iter = model.listResourcesWithProperty(RDF.type
                                                         , EDM.EuropeanaAggregation);
        try {
            if ( iter.hasNext() ) { return iter.next(); }
        }
        finally { iter.close(); }
        return null;
    }

    private void generateAggregations(ClassDefinition def) 
            throws IOException {
        jgen.writeFieldName(def.name);
        jgen.writeStartArray();
        ResIterator iter = model.listResourcesWithProperty(RDF.type, ORE.Aggregation);
        try {
            while (iter.hasNext()) { generateAggregation(iter.next()); }
        }
        finally { iter.close(); }

        jgen.writeEndArray();
    }

    private void generateAggregation(Resource r) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField(about, compactURI(r.getURI()));

        ClassDefinition def = getDefinition(ORE.Aggregation);
        for ( Map.Entry<Property,FieldDefinition> entry : def.entrySet() ) {
            generateField(r, entry.getKey(), entry.getValue());
        }

        if ( r.hasProperty(EDM.isShownBy) || r.hasProperty(EDM.isShownAt) ) {
            generateResources(getDefinition(EDM.WebResource));
        }

        jgen.writeEndObject();
    }


    private void generateResources(ClassDefinition def) throws IOException {
        jgen.writeFieldName(def.name);
        jgen.writeStartArray();
        ResIterator iter = model.listResourcesWithProperty(RDF.type, def.clazz);
        try {
            while (iter.hasNext()) { generateResource(iter.next(), def); }
        }
        finally { iter.close(); }
        jgen.writeEndArray();
    }
    

    private void generateSingleResource(ClassDefinition def) throws IOException {
        jgen.writeFieldName(def.name);
        ResIterator iter = model.listResourcesWithProperty(RDF.type, def.clazz);
        try {
            if (iter.hasNext()) { generateResource(iter.next(), def); }
        }
        finally { iter.close(); }
    }

    private void generateResource(Resource r, ClassDefinition def) throws IOException {
        jgen.writeStartObject();
        jgen.writeStringField(about, compactURI(r.getURI()));

        for ( Map.Entry<Property,FieldDefinition> entry : def.entrySet() ) {
            generateField(r, entry.getKey(), entry.getValue());
        }

        jgen.writeEndObject();
    }

    private void generateField(Resource r, Property prop, FieldDefinition field) 
            throws IOException {
        if ( field == null ) { return; }

        StmtIterator iter = r.listProperties(prop);
        if ( !iter.hasNext() ) { 
            iter.close(); 
            return;
        }

        if ( field.type == FieldType.map_array ) {
            writeAsMap(field, iter);
            return;
        }

        if ( field.type == FieldType.array ) {
            writeAsList(field, iter);
            return;
        }

        if ( field.type == FieldType.single ) {
            writeSingle(field, iter);
        }
    }

    private void writeAsMap(FieldDefinition def, StmtIterator iter) 
            throws IOException {
        Map<String,List<String>> map = toMap(iter, def);
        jgen.writeObjectFieldStart(def.name);
        for ( Map.Entry<String, List<String>> entry : map.entrySet() ) {
            jgen.writeArrayFieldStart(entry.getKey());
            for ( String value : entry.getValue() ) {
                jgen.writeString(value);
            }
            jgen.writeEndArray();
        }
        jgen.writeEndObject();
    }

    private void writeAsList(FieldDefinition def, StmtIterator iter) 
            throws IOException {
        jgen.writeArrayFieldStart(def.name);
        try {
            while ( iter.hasNext() ) {
                Object value = toValue(iter.next().getObject(), def);
                jgen.writeObject(value);
            }
        }
        finally { iter.close(); }
        jgen.writeEndArray();
    }

    private void writeSingle(FieldDefinition def, StmtIterator iter)
            throws IOException {
        try {
            while ( iter.hasNext() ) {
                Statement stmt = iter.next();
                RDFNode   obj  = stmt.getObject();
                if ( RDF.type.equals(stmt.getPredicate()) 
                  && !EDM.FullTextResource.equals(obj) )  {
                    continue;
                }
                jgen.writeObjectField(def.name, toValue(obj, def));
                break;
            }
        }
        finally { iter.close(); }
    }

    private Map toMap(StmtIterator iter, FieldDefinition fdef) {
        Map<String,List<String>> map = new HashMap();
        try {
            while ( iter.hasNext() ) {
                RDFNode node = iter.next().getObject();
                if ( node.isResource() ) {
                    put(map, def, toValue(node.asResource(), fdef).toString());
                    continue;
                }

                if ( !node.isLiteral() ) { continue; }
                Literal literal = node.asLiteral();

                if ( JenaUtils.hasLanguage(literal) ) {
                    put(map, literal.getLanguage(), toValue(literal, fdef));
                    continue;
                }

                put(map, def, toValue(literal, fdef));
            }
        }
        finally { iter.close(); }
        return map;
    }

    private void put(Map<String,List<String>> map, String key, String value) {
        List<String> values = map.get(key);
        if ( values == null ) {
            values = new ArrayList();
            map.put(key, values);
        }
        values.add(value);
    }

    private <T> T toValue(RDFNode node, FieldDefinition def) {
        if ( node.isResource() ) {  
            return (T)compactURI(node.asResource().getURI());
        }
        return (T)def.toValue(node.asLiteral());
    }

    private String compactURI(String uri) {
        if ( uri.startsWith(choUri) ) {
            return ( uri.length() > choUri.length() 
                  ? uri.replace(choUri, "") : uri.replace(DATA_ITEM, "/")); 
        }

        for ( Map.Entry<String, String> prefix : uriPrefixes.entrySet() ) {
            if ( !uri.startsWith(prefix.getKey()) ) { continue; }

            return uri.replace(prefix.getKey(), prefix.getValue());
        }
        return uri;
    }

    private void generateCollectionName(Resource eaggr)
            throws IOException {
        Statement stmt = eaggr.getProperty(EDM.datasetName);
        if ( stmt != null && stmt.getObject().isLiteral() ) {
            jgen.writeArrayFieldStart(eCollectionName);
            jgen.writeString(stmt.getString());
            jgen.writeEndArray();
        }
    }

    private void generateDatasetName(Resource eaggr) throws IOException {
        Statement stmt = eaggr.getProperty(EDM.datasetName);
        if ( stmt != null && stmt.getObject().isLiteral() ) {
            jgen.writeArrayFieldStart(edmDatasetName);
            jgen.writeString(stmt.getString());
            jgen.writeEndArray();
        }
    }

    private void generateCompleteness(Resource eaggr) throws IOException {
        Statement stmt = eaggr.getProperty(EDM.completeness);
        if ( stmt != null && stmt.getObject().isLiteral() ) {
            jgen.writeNumberField(eCompleteness, stmt.getInt());
        }
    }

    private void generateTimestamps(Resource eaggr)
            throws IOException {
        Statement stmt;

        stmt = eaggr.getProperty(DCTerms.created);
        if ( stmt != null && stmt.getObject().isLiteral() ) {
            generateTimestamp(timestamp_created, stmt.getLiteral());
        }

        stmt = eaggr.getProperty(DCTerms.modified);
        if ( stmt != null && stmt.getObject().isLiteral() ) {
            generateTimestamp(timestamp_update, stmt.getLiteral());
        }
    }

    private void generateTimestamp(String fieldName, Literal literal) 
            throws IOException {
        String  value = literal.getLexicalForm();
        Instant date  = Instant.parse(value);
        jgen.writeStringField(fieldName, value);
        jgen.writeNumberField(fieldName + "_epoch", date.toEpochMilli());
    }

    private void generateType(Resource cho) 
            throws IOException {
        StmtIterator iter = cho.getModel().listStatements(null, EDM.type, (Literal)null);
        try {
            if ( iter.hasNext() ) { 
                jgen.writeStringField(type, iter.next().getString());
            }
        }
        finally { iter.close(); }
    }

    
    public static final void main(String[] args) throws IOException {
        File file   = new File("C:\\Work\\incoming\\Record v3\\source\\UEDIN_214.xml");
        File target = new File("C:\\Work\\incoming\\Record v3\\target\\UEDIN_214.json");
        Model m = ModelFactory.createDefaultModel();
        m.read(new FileReader(file), "http://data.europeana.eu/item/142/UEDIN_214", "RDF/XML");

        try (FileOutputStream fos = new FileOutputStream(target)) {
            new RecordV2JsonWriter().write(m, fos);
            fos.flush();
        }
    }
}
