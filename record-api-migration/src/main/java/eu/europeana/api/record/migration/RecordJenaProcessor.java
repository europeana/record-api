package eu.europeana.api.record.migration;

import static org.apache.jena.rdf.model.ResourceFactory.*;

import java.util.*;

import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.model.MediaType;
import eu.europeana.api.model.MediaTypes;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.RDF;

import eu.europeana.api.edm.CC;
import eu.europeana.api.edm.DC;
import eu.europeana.api.edm.DCTerms;
import eu.europeana.api.edm.EBUCORE;
import eu.europeana.api.edm.EDM;
import eu.europeana.api.edm.FOAF;
import eu.europeana.api.edm.ORE;
import eu.europeana.api.edm.RDAGR2;
import eu.europeana.api.edm.SKOS;
import eu.europeana.api.edm.SVCS;
import eu.europeana.api.edm.XSD;
import eu.europeana.api.record.io.jena.RecordApiTemplateLibrary;

import static eu.europeana.api.record.migration.MigrationHandler.log;

/**
 * @author Hugo
 * @since 25 Oct 2023
 */
public class RecordJenaProcessor {

    private static final Resource ProvidedCHO      = createResource(EDM.NS + EDM.ProvidedCHO);
    private static final Resource Proxy            = createResource(ORE.NS + ORE.Proxy);
    private static final Resource WebResource      = createResource(EDM.NS + EDM.WebResource);
    private static final Resource Aggregation      = createResource(ORE.NS + ORE.Aggregation);
    private static final Resource EuropeanaAggregation = createResource(EDM.NS + EDM.EuropeanaAggregation);
    private static final Resource FullTextResource = createResource(EDM.NS + EDM.FullTextResource);
    private static final Resource Service          = createResource(SVCS.NS + SVCS.Service);
    private static final Resource License          = createResource(CC.NS + CC.License);

    //entities
    private static final Resource Agent          = createResource(EDM.NS + EDM.Agent);
    private static final Resource Place          = createResource(EDM.NS + EDM.Place);
    private static final Resource Concept        = createResource(SKOS.NS + SKOS.Concept);
    private static final Resource TimeSpan       = createResource(EDM.NS + EDM.TimeSpan);
    private static final Resource Organization   = createResource(FOAF.NS + FOAF.Organization);

    private static final Property aggregates     = createProperty(ORE.NS + ORE.aggregates);
    private static final Property proxyFor       = createProperty(ORE.NS + ORE.proxyFor);
    private static final Property proxyIn        = createProperty(ORE.NS + ORE.proxyIn);
    private static final Property aggregatedCHO  = createProperty(EDM.NS + EDM.aggregatedCHO);
    private static final Property lineage        = createProperty(ORE.NS + ORE.lineage);
    private static final Property created        = createProperty(DCTerms.NS + DCTerms.created);
    private static final Property modified       = createProperty(DCTerms.NS + DCTerms.modified);
    private static final Property creator        = createProperty(DC.NS + DC.creator);
    private static final Property europeanaProxy = createProperty(EDM.NS + EDM.europeanaProxy);
    private static final Property placeOfBirth   = createProperty(RDAGR2.NS + RDAGR2.placeOfBirth);
    private static final Property placeOfDeath   = createProperty(RDAGR2.NS + RDAGR2.placeOfDeath);
    private static final Property isPartOf       = createProperty(DCTerms.NS + DCTerms.isPartOf);
    private static final Property hasPart        = createProperty(DCTerms.NS + DCTerms.hasPart);
    private static final Property bioInfo        = createProperty(RDAGR2.NS + RDAGR2.biographicalInformation);
    

    private static final Property spatial        = createProperty(DCTerms.NS + DCTerms.spatial);

    private static final Property hasMimeType       = createProperty(EBUCORE.NS + EBUCORE.hasMimeType);
    private static final Property componentColor    = createProperty(EDM.NS + EDM.componentColor);
    private static final Property spatialResolution = createProperty(EDM.NS + EDM.spatialResolution);
    private static final Property duration          = createProperty(EBUCORE.NS + EBUCORE.duration);

    private static final Property preview        = createProperty(EDM.NS + EDM.preview);
    private static final Property isShownBy      = createProperty(EDM.NS + EDM.isShownBy);
    private static final Property isShownAt      = createProperty(EDM.NS + EDM.isShownAt);
    private static final Property object         = createProperty(EDM.NS + EDM.object);
    private static final Property hasView        = createProperty(EDM.NS + EDM.hasView);

    private static List<Resource> ENTITIES 
        = Arrays.asList(Agent, Place, Concept, TimeSpan, Organization);

    private static Collection<Resource> CORE_CLASSES
        = Arrays.asList(Proxy, Aggregation, WebResource, EuropeanaAggregation, ProvidedCHO);

    private MediaTypes mediaTypes;
    
    public RecordJenaProcessor(MediaTypes mediaTypes) {
        this.mediaTypes = mediaTypes;
    }

    public Resource upgrade(Resource cho) {
        
        Model m = cho.getModel();
        List<Resource> proxies = getAsList(m.listResourcesWithProperty(proxyFor, cho));
        Collections.sort(proxies, new ProxyComparator());

        if ( hasLineage(proxies) ) { 
            for ( Resource proxy : proxies ) {
                proxy.removeAll(lineage);
            }
        }
        addLineage(proxies);

        //if ( !hasLineage(proxies) ) { addLineage(proxies); }

        Resource eaggr = getAggregation(proxies.get(proxies.size() - 1));
        removeCreatorAndAggregates(eaggr);
        fixTimestamps(eaggr);
        addTimestamps(eaggr, proxies);
        removeEuropeanaProxy(proxies);

        List<Resource> webResources = getAsList(m.listResourcesWithProperty(RDF.type, WebResource));
        removeLooseResources(webResources);
        cleanPartRelations(webResources);
        cleanTechMetadata(webResources);
        fixWebResourcerReference(getObjects(m.listStatements(null, object, (RDFNode)null)));
        fixWebResourcerReference(getObjects(m.listStatements(null, preview, (RDFNode)null)));
        fixWebResourcerReference(getObjects(m.listStatements(null, isShownBy, (RDFNode)null)));
        fixWebResourcerReference(getObjects(m.listStatements(null, isShownAt, (RDFNode)null)));
        fixWebResourcerReference(getObjects(m.listStatements(null, hasView, (RDFNode)null)));

        removeLooseResources(getAsList(m.listResourcesWithProperty(RDF.type, Service)));
        removeLooseResources(getAsList(m.listResourcesWithProperty(RDF.type, License)));

        fixNonNegative(getAsList(m.listStatements(null, spatialResolution, (RDFNode)null)));
        fixNonNegative(getAsList(m.listStatements(null, duration, (RDFNode)null)));
        fixComponentColor(getAsList(m.listStatements(null, componentColor, (RDFNode)null)));

        List<Resource> entities = getEntities(m);
        removeLooseResourcesByRecursion(entities);
        cleanAgents(entities);
        cleanMultiTypedEntities(entities);
        cleanPartRelations(entities);

        return cho;
    }

    private void fixNonNegative(List<Statement> stmts) {
        for ( Statement stmt : stmts ) {
            RDFNode obj = stmt.getObject();
            if ( !obj.isLiteral() ) { continue; }

            String str = obj.asLiteral().getString();
            if ( !str.contains("-") ) { continue; }
            log("Negative " + stmt.getPredicate().getLocalName() + ": " + str);

            stmt.getModel().remove(stmt);
        }
    }

    private void fixComponentColor(List<Statement> stmts) {
        for ( Statement stmt : stmts ) { 
            RDFNode obj = stmt.getObject();
            if ( !obj.isLiteral() ) { continue; }

            String str = obj.asLiteral().getString();
            if ( !str.contains("#") ) { continue; }

            Model model = stmt.getModel();
            str = str.replaceAll("#", "");
            obj = model.createTypedLiteral(str, XSD.NS + XSD.hexBinary);
            stmt.getSubject().addProperty(componentColor, obj);
            model.remove(stmt);
        }
    }

    private void cleanTechMetadata(List<Resource> resources)
    {
        for ( Resource r : resources ) {
            Statement stmt = r.getProperty(hasMimeType);
            if ( stmt == null ) { continue; }

            Optional<MediaType> mediaType = mediaTypes.getMediaType(stmt.getString());
            if ( !mediaType.isEmpty() ) { continue; }

            r.getModel().remove(r, RDF.type, FullTextResource);
        }
    }

    private void fixWebResourcerReference(List<Resource> resources)
    {
        for ( Resource r : resources ) {
            if ( r.hasProperty(RDF.type) ) { continue; }

            if ( r.getURI().startsWith("http://data.europeana.eu/item") ) {
                continue;
            }
            r.addProperty(RDF.type, WebResource);
        }
    }

    private void removeLooseResources(Collection<Resource> entities) {
        boolean removed = true;
        while ( removed ) {
            removed = false;
            Iterator<Resource> iter = entities.iterator();
            while ( iter.hasNext() ) {
                Resource entity = iter.next();
                Model m = entity.getModel();
                boolean contains = existsBesidesSelfReference(
                        m.listStatements(null, null, entity));
                if ( contains ) { continue; }
                log("Removing loose resource: " + entity.getURI());
                entity.removeProperties();
                iter.remove();
                removed = true;
            }
        }
    }

    private void removeLooseResourcesByRecursion(Collection<Resource> entities) {
        Iterator<Resource> iter = entities.iterator();
        Stack<Resource> stack = new Stack<Resource>();
        while ( iter.hasNext() ) {
            Resource entity = iter.next();
            stack.push(entity);
            boolean connected = isConnectedTo(stack);
            stack.clear();

            if ( connected ) { continue; }

            log("Removing loose resource: " + entity.getURI());
            entity.removeProperties();
            iter.remove();
        }
    }

    private boolean isConnectedTo(Stack<Resource> stack) {
        Resource     entity = stack.peek();
        StmtIterator iter = entity.getModel().listStatements(null, null, entity);
        while ( iter.hasNext() ) {
            Resource subject = iter.next().getSubject();
            if ( hasType(subject, CORE_CLASSES) ) { return true; }
            if ( stack.contains(subject)        ) { continue;    }

            stack.push(subject);
            if ( isConnectedTo(stack) ) { return true; }
            stack.pop();
        }
        return false;
    }

    
    
    
    private void cleanMultiTypedEntities(Collection<Resource> entities) {
        for ( Resource entity : entities ) {
            int count = count(entity.listProperties(RDF.type));
            if ( count <= 1 ) { continue; }

            String uri = entity.getURI();
            log("Entity with multiple types: " + uri);
            if ( uri.startsWith("http://vocab.getty.edu/tgn/") ) { 
                retainType(entity, Place);
                continue;
            }
            if ( uri.startsWith("http://viaf.org/viaf/") ) { 
                retainType(entity, Agent);
                continue;
            }
            if ( isPlace(entity) ) {
                retainType(entity, Place);
                continue;
            }

            entity.getModel().remove(entity, RDF.type, Concept);
            log("Removed type Concept for: " + entity.getURI());
        }
    }

    private void cleanPartRelations(Collection<Resource> entities) {
        for ( Resource entity : entities ) {
            retainOnlyReferences(entity, isPartOf);
            retainOnlyReferences(entity, hasPart);
        }
    }

    private void retainOnlyReferences(Collection<Resource> resources
                                    , Property property) {
        for ( Resource res : resources ) { retainOnlyReferences(res, property); }
    }

    private void retainOnlyReferences(Resource res
                                    , Property property) {
        Model model = res.getModel();
        for ( Statement stmt : getAsList(res.listProperties(property)) ) {
            if ( !stmt.getObject().isLiteral() ) { continue; }
            model.remove(stmt);
            log("Removed literal from " + property.getLocalName() + ": " 
              + stmt.getObject().asLiteral().getString());
       }
    }

    private void retainOnlyLiterals(Collection<Resource> resources
                                  , Property property) {
        for ( Resource res : resources ) { retainOnlyLiterals(res, property); }
    }

    private void retainOnlyLiterals(Resource res, Property property) {
        Model model = res.getModel();
        for ( Statement stmt : getAsList(res.listProperties(property)) ) {
            if ( stmt.getObject().isLiteral() ) { continue; }
            model.remove(stmt);
            log("Removed reference from " + property.getLocalName() + ": " 
              + stmt.getObject().asResource().getURI());
        }
    }

    private boolean isPlace(Resource entity) {
        Model m = entity.getModel();
        if ( exists(m.listStatements(null, spatial, entity)) )  { return true; }
        return false;
    }

    private void retainType(Resource entity, Resource type) {
        Model m = entity.getModel();
        for ( Statement stmt : getAsList(entity.listProperties(RDF.type)) ) {
            if ( stmt.getObject().isResource() &&
                 stmt.getObject().asResource().equals(type) ) { continue; }

            m.remove(stmt);
            log("Removed type " + stmt.getObject().asResource().getLocalName() 
              + " for: " + stmt.getSubject().getURI());
        }
    }

    private List<Resource> getEntities(Model m) {
        List<Resource> list = new ArrayList();
        StmtIterator iter = m.listStatements(null, RDF.type, (Resource)null);
        while (iter.hasNext()) {
            Statement stmt = iter.next();
            Resource  r    = stmt.getObject().asResource();
            if ( ENTITIES.contains(r) ) { list.add(stmt.getSubject()); } 
        }
        return list;
    }

    private void cleanAgents(List<Resource> entities) {
        int count = 0;
        for ( Resource entity : entities ) {
            if ( !entity.hasProperty(RDF.type, Agent) ) { continue; }

            Model m = entity.getModel();
            count = count(entity.listProperties(placeOfBirth));
            if ( count > 1 ) { 
                retainFirst(getAsList(entity.listProperties(placeOfBirth)));
            }

            count = count(entity.listProperties(placeOfDeath));
            if ( count > 1 ) { 
                retainFirst(getAsList(entity.listProperties(placeOfDeath))); 
            }

            retainOnlyLiterals(entity, bioInfo);
        }
    }

    private void removeEuropeanaProxy(List<Resource> proxies) {
        for ( Resource proxy : proxies ) { proxy.removeAll(europeanaProxy); }
    }

    private boolean hasLineage(List<Resource> proxies) {
        boolean ret = false;
        for ( Resource proxy : proxies ) {
            ret = ret | proxy.hasProperty(lineage);
        }
        return ret;
    }

    private void addLineage(List<Resource> proxies) {
        Resource last = null;
        for ( Resource proxy : proxies ) {
            if ( last != null ) { proxy.addProperty(lineage, last); }
            last = proxy;
        }
    }


    private void removeCreatorAndAggregates(Resource eaggr) {
        eaggr.removeAll(creator);
        eaggr.removeAll(aggregates);
    }

    private void fixTimestamps(Resource eaggr) {
        Model model = eaggr.getModel();
        Literal literal, newLiteral;
        
        literal = getTimestamp(eaggr, created);
        newLiteral = fixTimestamp(literal);
        if ( newLiteral != null ) {
            model.remove(eaggr, created, literal);
            model.add(eaggr, created, newLiteral);
        }

        literal = getTimestamp(eaggr, modified);
        newLiteral = fixTimestamp(literal);
        if ( newLiteral != null ) {
            model.remove(eaggr, modified, literal);
            model.add(eaggr, modified, newLiteral);
        }
    }

    private Literal fixTimestamp(Literal literal) {
        if ( literal == null ) { return null; }

        String str = literal.getString();
        if ( str.endsWith(".000Z") ) {
            return literal.getModel().createTypedLiteral(str.replace(".000Z","Z")
                                                       , literal.getDatatypeURI());
        }
        return null;
    }

        
    private void addTimestamps(Resource eaggr, List<Resource> proxies) {
      //Literal lcreated  = getTimestamp(eaggr, created);
        Literal lmodified = getTimestamp(eaggr, modified);

        Iterator<Resource> iter = proxies.iterator();
        while (iter.hasNext()) {
            Resource aggr = getAggregation(iter.next());
            if ( aggr == null || !iter.hasNext() ) { continue; }

            aggr.addLiteral(modified, lmodified);
        }
    }

    private Resource getAggregation(Resource proxy) {
        Statement stmt = proxy.getProperty(proxyIn);
        return ( stmt == null ? null : stmt.getObject().asResource() );
    }

    private Literal getTimestamp(Resource aggr, Property prop) {
        Statement stmt = aggr.getProperty(prop);
        return ( stmt == null || !stmt.getObject().isLiteral() 
               ? null : stmt.getObject().asLiteral() );
    }

    private void retainFirst(List<Statement> stmts) {
        Iterator<Statement> iter = stmts.iterator();
        Model model = iter.next().getModel();
        while ( iter.hasNext() ) { 
            Statement stmt = iter.next();
            log("Removed duplicate " + stmt.getPredicate().getLocalName() 
                                     + " from " + stmt.getSubject().getURI());
            model.remove(stmt); 
        }
    }

    private int count(StmtIterator iter) {
        int count = 0;
        try {
            while ( iter.hasNext() ) { iter.next(); count++; }
            return count;
        }
        finally { iter.close(); }
    }

    private boolean existsBesidesSelfReference(StmtIterator iter) {
        try {
            while ( iter.hasNext() ) { 
                Statement stmt = iter.next();
                Resource  subj = stmt.getSubject();
                Resource  obj  = (Resource)stmt.getObject();

                //self or cycle reference
                if ( subj.equals(obj) /*|| obj.hasProperty(null, subj)*/ ) { continue; }

                return true;
            }
        }
        finally { iter.close(); }
        return false;
    }

    private boolean hasType(Resource r, Collection<Resource> types) {
        StmtIterator iter = r.listProperties(RDF.type);
        while ( iter.hasNext() ) {
            if ( types.contains(iter.next().getObject()) ) { return true; }
        }
        return false;
    }

    private boolean exists(StmtIterator iter) {
        try {
            return ( iter.hasNext() );
        }
        finally { iter.close(); }
    }

    private List<Resource> getObjects(StmtIterator iter) {
        List<Resource> ret = new ArrayList();
        try {
            while ( iter.hasNext() ) { 
                RDFNode node = iter.next().getObject();
                if ( node.isResource() ) { ret.add(node.asResource()); }
            }
            return ret;
        }
        finally { iter.close(); }
    }

    private List<Statement> getAsList(StmtIterator iter) {
        List<Statement> ret = new ArrayList();
        try {
            while ( iter.hasNext() ) { ret.add(iter.next()); }
            return ret;
        }
        finally { iter.close(); }
    }

    private List<Resource> getAsList(ResIterator iter) {
        List<Resource> ret = new ArrayList();
        try {
            while ( iter.hasNext() ) { ret.add(iter.next()); }
            return ret;
        }
        finally { iter.close(); }
    }

    private static class ProxyComparator implements Comparator<Resource> {

        @Override
        public int compare(Resource r1, Resource r2)
        {
            return (getOrder(r1) - getOrder(r2));
        }

        private int getOrder(Resource r) {
            String uri = r.getURI();
            if ( uri.contains("/proxy/europeana/") ) { return 10; }
            if ( uri.contains("/proxy/provider/" ) ) { return 1;  }
            return 2;
        }
    }
}
