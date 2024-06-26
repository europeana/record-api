package eu.europeana.api.record.migration;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

import eu.europeana.api.model.MediaType;
import eu.europeana.api.model.MediaTypes;
import eu.europeana.api.record.model.data.EdmType;

import org.apache.jena.datatypes.xsd.XSDDatatype;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DC;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.SKOS;
import org.apache.jena.vocabulary.XSD;

/*
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
*/
import eu.europeana.jena.edm.CC;
import eu.europeana.jena.edm.DQV;
import eu.europeana.jena.edm.EBUCORE;
import eu.europeana.jena.edm.EDM;
import eu.europeana.jena.edm.ORE;
import eu.europeana.jena.edm.RDAGR2;
import eu.europeana.jena.edm.SVCS;

import static eu.europeana.api.record.migration.JenaUtils.*;

/**
 * @author Hugo
 * @since 25 Oct 2023
 */
public class RecordJenaProcessor {

    public static DateTimeFormatter DATETIME_FORMAT
        = DateTimeFormatter.ofPattern("uuuu-MM-dd'T'HH:mm:ss.SSS'Z'")
                           .withZone(ZoneOffset.UTC);

    private static String THUMBNAIL_URL = "https://api.europeana.eu/thumbnail/v3/%s/%s.%s";

    private static List<Resource> ENTITIES 
        = Arrays.asList(EDM.Agent, EDM.Place, SKOS.Concept, EDM.TimeSpan
                      , FOAF.Organization);

    private static Collection<Resource> CORE_CLASSES
        = Arrays.asList(ORE.Proxy, ORE.Aggregation, EDM.WebResource
                      , EDM.EuropeanaAggregation, EDM.ProvidedCHO);

    private MediaTypes      mediaTypes = null;
    private MigrationConfig config;

    public RecordJenaProcessor(MigrationConfig config, MediaTypes mediaTypes) {
        this.config     = config;
        this.mediaTypes = mediaTypes;
    }

    public Resource upgrade(Resource cho) {
        Model m = cho.getModel();
        m.removeNsPrefix("rdf");
        List<Resource> proxies = asList(m.listResourcesWithProperty(ORE.proxyFor, cho));
        Collections.sort(proxies, new ProxyComparator());

        if ( hasLineage(proxies) ) { 
            for ( Resource proxy : proxies ) {
                proxy.removeAll(ORE.lineage);
            }
        }
        addLineage(proxies);

        //if ( !hasLineage(proxies) ) { addLineage(proxies); }

        Resource paggr = getAggregation(proxies.get(0));
        Resource eaggr = getAggregation(proxies.get(proxies.size() - 1));
        removeCreatorAndAggregates(eaggr);
        //fixTimestamps(eaggr); //old code
        fixTimestampsToMills(eaggr);
        addTimestamps(eaggr, proxies);
        removeEuropeanaProxy(proxies);
        for ( Resource proxy : proxies ) {
            copyDuplicateLiteralValues(proxy, DC.title, DCTerms.alternative);
        }

        List<Resource> webResources = asList(m.listResourcesWithProperty(RDF.type, EDM.WebResource));
        List<Resource> previews     = getObjects(m.listStatements(null, EDM.preview, (RDFNode)null));

        //get all views
        List<Resource> allViews     = new ArrayList<>();
        getObjects(m.listStatements(null, EDM.isShownBy, (RDFNode)null), allViews);
        getObjects(m.listStatements(null, EDM.hasView, (RDFNode)null), allViews);

        removeLooseResourcesByRecursion(webResources);
        addMissingWebResourceFromSequence(allViews, paggr);
        
        cleanPartRelations(webResources);
        cleanTechMetadata(webResources);
        fixWebResourcerReference(getObjects(m.listStatements(null, EDM.object, (RDFNode)null)));
        fixWebResourcerReference(previews);
        fixWebResourcerReference(allViews);
        fixWebResourcerReference(getObjects(m.listStatements(null, EDM.isShownAt, (RDFNode)null)));
        upgradePreviewToThumbnail(eaggr, previews);

        generateThumbnails(allViews);

        removeLooseResources(asList(m.listResourcesWithProperty(RDF.type, SVCS.Service)));
        removeLooseResources(asList(m.listResourcesWithProperty(RDF.type, CC.License)));

        fixNonNegative(asList(m.listStatements(null, EDM.spatialResolution, (RDFNode)null)));
        fixNonNegative(asList(m.listStatements(null, EBUCORE.duration, (RDFNode)null)));
        fixComponentColor(asList(m.listStatements(null, EDM.componentColor, (RDFNode)null)));

        List<Resource> entities = getEntities(m);
        removeLooseResourcesByRecursion(entities);
        for ( Resource entity : entities ) {
            copyDuplicateLiteralValues(entity, SKOS.prefLabel, SKOS.altLabel);
        }
        cleanAgents(entities);
        cleanMultiTypedEntities(entities);
        cleanPartRelations(entities);

        fixTimestampsToMills(asList(m.listResourcesWithProperty(RDF.type, DQV.QualityAnnotation)));

        return cho;
    }

    private void fixNonNegative(List<Statement> stmts) {
        for ( Statement stmt : stmts ) {
            RDFNode obj = stmt.getObject();
            if ( !obj.isLiteral() ) { continue; }

            String str = obj.asLiteral().getString();
            if ( !str.contains("-") ) { continue; }
            config.out.println("Negative " + stmt.getPredicate().getLocalName() + ": " + str);

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
            obj = model.createTypedLiteral(str, XSD.hexBinary.getURI());
            stmt.getSubject().addProperty(EDM.componentColor, obj);
            model.remove(stmt);
        }
    }

    private void cleanTechMetadata(List<Resource> resources)
    {
        for ( Resource r : resources ) {
            Statement stmt = r.getProperty(EBUCORE.hasMimeType);
            if ( stmt == null ) { continue; }

            Optional<MediaType> mediaType = mediaTypes.getMediaType(stmt.getString());
            if ( !mediaType.isEmpty() ) { continue; }

            r.getModel().remove(r, RDF.type, EDM.FullTextResource);
        }
    }

    private Dimension getSize(Resource r) {
        Integer width  = getAsInteger(r.getProperty(EBUCORE.width));
        Integer height = getAsInteger(r.getProperty(EBUCORE.height));
        return ( width != null && height != null ? new Dimension(width,height) : null );
    }

    private Integer getAsInteger(Statement stmt) {
        return ( stmt == null ? null : stmt.getInt() );
    }

    private void generateThumbnails(List<Resource> resources) {
        String thumbURL;
        for ( Resource r : resources ) {
            Statement stmt = r.getProperty(EBUCORE.hasMimeType);
            if ( stmt == null ) { continue; }

            Optional<MediaType> mediaType = mediaTypes.getMediaType(stmt.getString());
            if ( mediaType.isEmpty() ) { continue; }
            
            MediaType mt = mediaType.get();
            if ( mt.isVideoOrSound() || !mt.isBrowserSupported() ) { continue; }

            Model m = r.getModel();
            String mediaId = HashUtils.getMD5(r.getURI());
            Dimension size = getSize(r);
            if ( size == null ) { continue; }

            thumbURL = String.format(THUMBNAIL_URL, "400", mediaId, "jpg");
            r.addProperty(EDM.preview
                        , generateThumbnailResource(m.getResource(thumbURL), size.resizeToWidth(400)));

            thumbURL = String.format(THUMBNAIL_URL, "200", mediaId, "jpg");
            r.addProperty(EDM.preview
                        , generateThumbnailResource(m.getResource(thumbURL), size.resizeToWidth(200)));
        }
    }

    private Resource generateThumbnailResource(Resource r, Dimension size) {
        r.addProperty(RDF.type, EDM.WebResource);
        r.addLiteral(EDM.type, EdmType.IMAGE.toString());
        r.addProperty(EBUCORE.width, Integer.toString(size.width), XSDDatatype.XSDinteger);
        r.addProperty(EBUCORE.height, Integer.toString(size.height), XSDDatatype.XSDinteger);
        r.addLiteral(EBUCORE.hasMimeType, "image/jpeg");
        return r;
    }

    private void upgradePreviewToThumbnail(Resource eaggr, List<Resource> previews) {
        eaggr.removeAll(EDM.preview);

        Model m = eaggr.getModel();
        String thumbURL;
        for ( Resource preview : previews ) {
            String mediaId = HashUtils.getMD5(preview.getURI());
            Dimension size = getSize(preview);
            if ( size == null ) { continue; }

            thumbURL = String.format(THUMBNAIL_URL, "400", mediaId, "jpg");
            eaggr.addProperty(EDM.preview
                            , generateThumbnailResource(m.getResource(thumbURL)
                                                      , size.resizeToWidth(400)));

            thumbURL = String.format(THUMBNAIL_URL, "200", mediaId, "jpg");
            eaggr.addProperty(EDM.preview
                            , generateThumbnailResource(m.getResource(thumbURL)
                                                      , size.resizeToWidth(200)));
            
        }
    }

    private void fixWebResourcerReference(List<Resource> resources)
    {
        for ( Resource r : resources ) {
            if ( r.hasProperty(RDF.type) ) { continue; }

            if ( r.getURI().startsWith("http://data.europeana.eu/item") ) {
                continue;
            }
            r.addProperty(RDF.type, EDM.WebResource);
        }
    }

    private void removeLooseResources(Collection<Resource> resources) {
        boolean removed = true;
        while ( removed ) {
            removed = false;
            Iterator<Resource> iter = resources.iterator();
            while ( iter.hasNext() ) {
                Resource r = iter.next();
                Model m = r.getModel();
                boolean contains = existsBesidesSelfReference(
                        m.listStatements(null, null, r));
                if ( contains ) { continue; }
                config.out.println("Removing loose resource: " + r.getURI());
                r.removeProperties();
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

            config.out.println("Removing loose resource: " + entity.getURI());
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

    
    /*
     * what to do with https://d-nb.info/gnd/4200334-9 ?
     */
    
    private void cleanMultiTypedEntities(Collection<Resource> entities) {
        for ( Resource entity : entities ) {
            int count = count(entity.listProperties(RDF.type));
            if ( count <= 1 ) { continue; }

            String uri = entity.getURI();
            config.out.println("Entity with multiple types: " + uri);
            if ( uri.startsWith("http://vocab.getty.edu/tgn/") ) { 
                retainType(entity, EDM.Place);
                continue;
            }
            if ( uri.startsWith("http://viaf.org/viaf/") ) { 
                retainType(entity, EDM.Agent);
                continue;
            }
            if ( isPlace(entity) ) {
                retainType(entity, EDM.Place);
                continue;
            }

            entity.getModel().remove(entity, RDF.type, SKOS.Concept);
            config.out.println("Removed type Concept for: " + entity.getURI());
        }
    }

    /*
     * Establishes a link to the Aggregation for any loose web resource 
     * that is linked to via isNextInSequence from any of the views
     * 
     * Examples: /9200201/BibliographicResource_3000005844676_source
     *           /9200217/BibliographicResource_3000045505026_source
     */
    private void addMissingWebResourceFromSequence(List<Resource> allViews
                                                 , Resource aggr) {
        
        Model m = aggr.getModel();
        for ( int i = 0; i < allViews.size(); i++ ) {
            Resource view = allViews.get(i);
            
            for ( Statement stmt : asList(m.listStatements(null, EDM.isNextInSequence, view)) ) {
                Resource subject = stmt.getSubject();
                if ( allViews.contains(subject) ) { continue; }

                allViews.add(subject);
                aggr.addProperty(EDM.hasView, subject);
                config.out.println("Added unlinked view from sequence: " 
                                 + subject.getURI());
            }

            for ( Statement stmt : asList(view.listProperties(EDM.isNextInSequence)) ) {
                RDFNode node = stmt.getObject();
                if ( !node.isResource() ) { continue; }

                Resource obj = node.asResource();
                if ( allViews.contains(obj) ) { continue; }

                allViews.add(obj);
                aggr.addProperty(EDM.hasView, obj);
                config.out.println("Added unlinked view from sequence: " 
                                 + obj.getURI());
            }
        }
    }


    private void cleanPartRelations(Collection<Resource> resources) {
        for ( Resource r : resources ) {
            retainOnlyReferences(r, DCTerms.isPartOf);
            retainOnlyReferences(r, DCTerms.hasPart);
        }
    }

    private void retainOnlyReferences(Collection<Resource> resources
                                    , Property property) {
        for ( Resource res : resources ) { retainOnlyReferences(res, property); }
    }

    private void retainOnlyReferences(Resource res
                                    , Property property) {
        Model model = res.getModel();
        for ( Statement stmt : asList(res.listProperties(property)) ) {
            if ( !stmt.getObject().isLiteral() ) { continue; }
            model.remove(stmt);
            config.out.println("Removed literal from " + property.getLocalName() + ": " 
              + stmt.getObject().asLiteral().getString());
       }
    }

    private void retainOnlyLiterals(Collection<Resource> resources
                                  , Property property) {
        for ( Resource res : resources ) { retainOnlyLiterals(res, property); }
    }

    private void retainOnlyLiterals(Resource res, Property property) {
        Model model = res.getModel();
        for ( Statement stmt : asList(res.listProperties(property)) ) {
            if ( stmt.getObject().isLiteral() ) { continue; }
            model.remove(stmt);
            config.out.println("Removed reference from " + property.getLocalName() + ": " 
              + stmt.getObject().asResource().getURI());
        }
    }

    private boolean isPlace(Resource entity) {
        Model m = entity.getModel();
        if ( exists(m.listStatements(null, DCTerms.spatial, entity)) )  { return true; }
        return false;
    }

    private void retainType(Resource entity, Resource type) {
        Model m = entity.getModel();
        for ( Statement stmt : asList(entity.listProperties(RDF.type)) ) {
            if ( stmt.getObject().isResource() &&
                 stmt.getObject().asResource().equals(type) ) { continue; }

            m.remove(stmt);
            config.out.println("Removed type " + stmt.getObject().asResource().getLocalName() 
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

    /*
    private void cleanDuplicatePrefLabels(List<Resource> entities) {
        for ( Resource entity : entities ) {
            cleanDuplicatePrefLabels(entity);
        }
    }

    private void cleanDuplicatePrefLabels(Resource entity) {
        Map<String,Literal> labels = new HashMap();

        //get all labels without duplicates and removing them along the way
        StmtIterator iter = entity.listProperties(SKOS.prefLabel);
        while ( iter.hasNext() ) {
            RDFNode node = iter.next().getObject();
            if ( !node.isLiteral() ) { continue; }

            Literal literal = node.asLiteral();
            String lang = literal.getLanguage();
            if ( lang == null ) { continue; }

            iter.remove();
            Literal prev = labels.put(lang, literal);
            if ( prev != null ) {
                config.out.println("Duplicate prefLabel removed: " + prev);
            }
        }

        //re-adding only the non-duplicate language labels
        for ( Literal literal : labels.values() ) {
            entity.addLiteral(SKOS.prefLabel, literal);
        }
    }
    */

    /* copies any duplicate value for a property that has max 1 cardinality
     * into another property
     * 
     * Example: /2048705/object_HA_149
     */
    private void copyDuplicateLiteralValues(Resource r
                                   , Property source, Property target) {

        Model m = r.getModel();
        Collection<Literal> literals = getLanguageTaggedLiterals(r.listProperties(source));
        Map<String,Literal> values = new HashMap();

        //move values between properties
        for ( Literal literal : literals ) {
            Literal prev = values.put(literal.getLanguage(), literal);
            if ( prev == null ) { continue; }

            m.remove(r, source, literal);
            m.add(r, target, literal);
            config.out.println("Duplicate " + source.getLocalName() 
                             + " copied to " + target.getLocalName() 
                             + ": " + prev);
        }
    }

    private void cleanAgents(List<Resource> entities) {
        int count = 0;
        for ( Resource entity : entities ) {
            if ( !entity.hasProperty(RDF.type, EDM.Agent) ) { continue; }

            Model m = entity.getModel();
            count = count(entity.listProperties(RDAGR2.placeOfBirth));
            if ( count > 1 ) { 
                retainFirst(asList(entity.listProperties(RDAGR2.placeOfBirth)));
            }

            count = count(entity.listProperties(RDAGR2.placeOfDeath));
            if ( count > 1 ) { 
                retainFirst(asList(entity.listProperties(RDAGR2.placeOfDeath))); 
            }

            retainOnlyLiterals(entity, RDAGR2.biographicalInformation);
        }
    }

    private void removeEuropeanaProxy(List<Resource> proxies) {
        for ( Resource proxy : proxies ) { proxy.removeAll(EDM.europeanaProxy); }
    }

    private boolean hasLineage(List<Resource> proxies) {
        boolean ret = false;
        for ( Resource proxy : proxies ) {
            ret = ret | proxy.hasProperty(ORE.lineage);
        }
        return ret;
    }

    private void addLineage(List<Resource> proxies) {
        Resource last = null;
        for ( Resource proxy : proxies ) {
            if ( last != null ) { proxy.addProperty(ORE.lineage, last); }
            last = proxy;
        }
    }


    private void removeCreatorAndAggregates(Resource eaggr) {
        eaggr.removeAll(DC.creator);
        eaggr.removeAll(ORE.aggregates);
    }

    private void fixTimestamps(Resource eaggr) {
        Model model = eaggr.getModel();
        Literal literal, newLiteral;
        
        literal = getTimestamp(eaggr, DCTerms.created);
        newLiteral = fixTimestamp(literal);
        if ( newLiteral != null ) {
            model.remove(eaggr, DCTerms.created, literal);
            model.add(eaggr, DCTerms.created, newLiteral);
        }

        literal = getTimestamp(eaggr, DCTerms.modified);
        newLiteral = fixTimestamp(literal);
        if ( newLiteral != null ) {
            model.remove(eaggr, DCTerms.modified, literal);
            model.add(eaggr, DCTerms.modified, newLiteral);
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

    private void fixTimestampsToMills(List<Resource> resources) {
        for ( Resource r : resources ) {
            fixTimestampsToMills(r);
        }
    }

    private void fixTimestampsToMills(Resource r) {
        Model model = r.getModel();
        Literal literal, newLiteral;

        literal = getTimestamp(r, DCTerms.created);
        newLiteral = fixTimestampToMills(literal);
        if ( newLiteral != null ) {
            model.remove(r, DCTerms.created, literal);
            model.add(r, DCTerms.created, newLiteral);
        }

        literal = getTimestamp(r, DCTerms.modified);
        newLiteral = fixTimestampToMills(literal);
        if ( newLiteral != null ) {
            model.remove(r, DCTerms.modified, literal);
            model.add(r, DCTerms.modified, newLiteral);
        }
    }

    private Literal fixTimestampToMills(Literal literal) {
        if ( literal == null ) { return null; }

        String timestamp = literal.getString();
        Instant ldt = Instant.parse(timestamp);
        String  dt  = DATETIME_FORMAT.format(ldt);
        if ( !timestamp.equals(dt) ) {
            config.out.println("Changed datetime: " + timestamp + " => " + dt);
        }
        return literal.getModel().createLiteral(dt);
    }

        
    private void addTimestamps(Resource eaggr, List<Resource> proxies) {
      //Literal lcreated  = getTimestamp(eaggr, created);
        Literal lmodified = getTimestamp(eaggr, DCTerms.modified);

        Iterator<Resource> iter = proxies.iterator();
        while (iter.hasNext()) {
            Resource aggr = getAggregation(iter.next());
            if ( aggr == null || !iter.hasNext() ) { continue; }

            aggr.addLiteral(DCTerms.modified, lmodified);
        }
    }

    private Resource getAggregation(Resource proxy) {
        Statement stmt = proxy.getProperty(ORE.proxyIn);
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
            config.out.println("Removed duplicate " + stmt.getPredicate().getLocalName() 
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


    /*
     * Methods for external
     */

    private Collection<Property> getProperties(List<Resource> proxies) {
        Collection<Property> ret = new HashSet();
        for ( Resource proxy : proxies ) {
            StmtIterator iter = proxy.listProperties();
            while ( iter.hasNext() ) {
                ret.add(iter.next().getPredicate());
            }
        }
        ret.removeAll(Arrays.asList(ORE.proxyFor, ORE.lineage, ORE.proxyIn, RDF.type));
        return ret;
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

    private static record Dimension(int width, int height) {

        public Dimension resizeToWidth(int width) {
            float ratio = (float)this.height / this.width;
            return new Dimension(width, Math.round(ratio * width));
        }
     }
}