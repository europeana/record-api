package eu.europeana.api.record.migration;

import eu.europeana.api.edm.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdfxml.xmlinput.DOM2Model;
import org.w3c.dom.*;

import javax.xml.XMLConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.*;

/**
 * @author Hugo
 * @since 1 Nov 2023
 */
public class RecordDomProcessor {

    private static String PREFIX_ITEM = "http://data.europeana.eu/item";
    private static String PREFIX_AGGREGATION_EUROPEANA = "http://data.europeana.eu/aggregation/europeana";
    private static String PREFIX_PROXY_EUROPEANA = "http://data.europeana.eu/proxy/europeana";

    private MigrationConfig config;

    public RecordDomProcessor(MigrationConfig config) {
        this.config = config;
    }

    public String getRecordId(Document doc) {
        String uri = (doc.hasChildNodes()
                ? getURI(doc.getDocumentElement()
                .getElementsByTagNameNS(EDM.NS, EDM.ProvidedCHO))
                : null);
        return (uri != null ? uri.replace(PREFIX_ITEM, "") : null);
    }


    public Result process(Document doc) {
        Element root = doc.getDocumentElement();
        if (root == null) { return null; }

        String uri = getURI(root.getElementsByTagNameNS(EDM.NS, EDM.ProvidedCHO));
        List<Element> entities = getEntities(root);
        List<Element> proxies = append(root.getElementsByTagNameNS(ORE.NS, ORE.Proxy)
                , new ArrayList<Element>(3));

        Collection<String> entityIds = getIds(entities, new TreeSet<String>());

        //re-establish references
        processMetadata(root.getElementsByTagNameNS(ORE.NS, ORE.Proxy)
                      , entityIds);

        //check for URIs that must be changed to relative
        Map<String, String> newIds = improveURIs(uri, entityIds);
        if (!newIds.isEmpty()) {
            applyNewIdsToResources(entities, newIds);
            applyNewIdsToProperties(proxies, newIds);
        }

        // fix quality annotation references
        List<Element> eaggr = getElements(root, EDM.NS, EDM.EuropeanaAggregation);
        List<Element> qAnno = getElements(root, DQV.NS, DQV.QualityAnnotation);
        newIds = improveURIs(uri, getIds(qAnno, new TreeSet<String>()));
        if (!newIds.isEmpty()) {
            applyNewIdsToResources(qAnno, newIds);
            applyNewIdsToProperties(eaggr, newIds);
        }
        
        List<Element> webres = getElements(root, EDM.NS, EDM.WebResource);
        fixWebResourceURLs(webres);
        processAggregations(getElements(root, ORE.NS, ORE.Aggregation));
        processAggregations(eaggr);
        fixIdForResource(eaggr, PREFIX_AGGREGATION_EUROPEANA);

        // fix all malformed URIs recursively. 
        // It is better to do this at the end, after all the other fixes are done
        recursiveFixURIs(root.getChildNodes());

        List<String> views = getHasViews(root.getElementsByTagNameNS(ORE.NS, ORE.Aggregation)
                , new ArrayList<String>());

        if (uri == null) {
            return null;
        }

        root.setAttributeNS(XMLConstants.XML_NS_URI, "xml:base", uri + "/");
        return new Result(doc, uri, views);
    }

    private List<Element> getEntities(Element root) {
        NodeList agents = root.getElementsByTagNameNS(EDM.NS, EDM.Agent);
        NodeList times = root.getElementsByTagNameNS(EDM.NS, EDM.TimeSpan);
        NodeList places = root.getElementsByTagNameNS(EDM.NS, EDM.Place);
        NodeList concepts = root.getElementsByTagNameNS(SKOS.NS, SKOS.Concept);

        List<Element> list = new ArrayList<Element>(agents.getLength()
                + times.getLength()
                + places.getLength()
                + concepts.getLength());
        return append(concepts, append(places, append(times, append(agents, list))));
    }


    private void processMetadata(NodeList list, Collection<String> ids) {
        for (int i = 0; i < list.getLength(); i++) {
            Element elem = (Element) list.item(i);
            NodeList props = elem.getChildNodes();
            for (int e = 0; e < props.getLength(); e++) {
                Element prop = (Element) props.item(e);
                if (isReference(prop) || !canBecomeReference(prop)) {
                    continue;
                }

                if ( hasLanguage(prop) ) { continue; }
                
                String str = prop.getTextContent();
                if (StringUtils.isBlank(str)) {
                    continue;
                }

                if (ids.contains(str)) {
                    upgradeToReference(prop);
                }
            }
        }
    }

    private void fixIdForResource(List<Element> list, String uriPrefix) {
        for (Element elem : list ) {
            String uri = getId(elem);
            if (uri != null && uri.startsWith(uriPrefix)) {
                continue;
            }

            String recordId = getRecordID(uri);
            if (recordId == null) {
                continue;
            }

            uri = uriPrefix + recordId;
            setId(elem, uri);
            config.out.println("Fixed URI to: " + uri);
        }
    }

    private Map<String, String> improveURIs(String uri, Collection<String> ids) {
        Map<String, String> ret = new HashMap();
        for (String id : ids) {

            String newId = newRelativeURI(uri, id);
            if ( newId == null ) { continue; }

            if (ids.contains(newId)) {
                config.out.println("Conflicting id: " + newId);
                continue;
            }
            ret.put(id, newId);
        }
        return ret;
    }

    private String newRelativeURI(String uri, String id) {

        // these are the acceptable patterns
        if ( id.startsWith("#") || id.startsWith("./") ) { return null; }

        if (isFullURI(id)) {
            if ( !id.startsWith(uri) ) { return null; }
            
            if ( id.equals(uri) ) { return null; }

            //necessary to fix # based uris such as the #contentTier ids
            id = id.substring(uri.length());

            // these are the acceptable patterns
            if ( id.startsWith("#") ) { return id; }

            if ( id.startsWith("/") ) { return "." + id; }
            return null;
        }

        // remove these patterns because they dont make sense in our context
        while ( id.startsWith("../") ) { id = id.substring(3); }

        if ( id.startsWith("/") ) { return "." + id; }

        return "#" + id;
    }

    // the expectation is that Metis would have fixed any non-uri character
    // so this check only looks for the path separator
    private boolean isFullURI(String uri) {
        return URIUtils.isAbsolute(uri);
    }

    private boolean isRelativeURI(String uri) {
        char c = uri.charAt(0);
        switch (c) {
            case '.':
            case '/':
            case '#':
                return true;
        }
        return false;
    }

    private void applyNewIdsToResources(List<Element> list
            , Map<String, String> newIds) {
        for (Element elem : list) {
            applyNewIdsToResources(elem, newIds);
        }
    }

    private void applyNewIdsToResources(Element elem, Map<String, String> newIds) {
        String id = getId(elem);
        if (id == null) { return; }

        String newId = newIds.get(id);
        if (newId == null) { return; }

        config.out.println("Changed declared id: " + id + " => " + newId);
        setId(elem, newId);
    }

    private void applyNewIdsToProperties(List<Element> list
            , Map<String, String> newIds) {
        for (Element elem : list) {
            NodeList props = elem.getChildNodes();
            for (int e = 0; e < props.getLength(); e++) {
                Element prop = (Element) props.item(e);

                String ref = getReference(prop);
                if (ref == null) {
                    continue;
                }

                String newId = newIds.get(ref);
                if (newId == null) {
                    continue;
                }

                config.out.println("Changed reference: " + ref + " => " + newId);
                setReference(prop, newId);
            }
        }
    }


    private void processAggregations(List<Element> list) {
        if (list == null) {
            return;
        }

        List<Element> props = new ArrayList<>();
        for (Element aggr : list ) {
            append(aggr.getElementsByTagNameNS(EDM.NS, EDM.isShownAt), props);
            append(aggr.getElementsByTagNameNS(EDM.NS, EDM.object), props);
            append(aggr.getElementsByTagNameNS(EDM.NS, EDM.isShownBy), props);
            append(aggr.getElementsByTagNameNS(EDM.NS, EDM.hasView), props);
            append(aggr.getElementsByTagNameNS(EDM.NS, EDM.preview), props);
        }
        fixWebResourceURLs(props);
    }

    /* 
     * only fixes some web resource URLs for which the data URI was prepended by mistake
     */
    private void fixWebResourceURLs(List<Element> list) {
        for (Element prop : list) {

            String attr;

            attr = getId(prop);
            if (attr != null && attr.startsWith(PREFIX_ITEM)) { 
                String uri = attr.replace(PREFIX_ITEM, "");
                if ( !isFullURI(uri) ) { continue; }

                config.out.println("Fixed webresource URI: " + attr + " => " + uri);
                setId(prop, uri);
                continue;
            }

            attr = getReference(prop);
            if (attr != null && attr.startsWith(PREFIX_ITEM)) { 
                String uri = attr.replace(PREFIX_ITEM, "");
                if ( !isFullURI(uri) ) { continue; }

                config.out.println("Fixed webresource reference: " + attr + " => " + uri);
                setReference(prop, uri);
                continue;
            }

        }
    }

    private void recursiveFixURIs(NodeList list) {
        for (int i = 0; i < list.getLength(); i++) {
            Node n = list.item(i);
            if ( !(n instanceof Element) ) { continue; }

            Element e = (Element)n;

            String ref = getReference(e);
            if ( ref != null && isMalformed(ref) ) {
                String nref = fixMalformed(ref);
                config.out.println("Fix malformed reference: " + ref + " => " + nref);
                setReference(e, nref);
                continue;
            }

            String id = getId(e);
            if ( id != null && isMalformed(id) ) {
                String nid = fixMalformed(id);
                config.out.println("Fix malformed id: " + id + " => " + nid);
                setId(e, nid);
            }

            recursiveFixURIs(e.getChildNodes());
        }
    }

    private boolean isMalformed(String uri) {
        return uri.contains(" ");
    }

    private String fixMalformed(String uri) {
        return uri.replace(" ", "%20");
    }

    private List<String> getHasViews(NodeList list, List<String> views) {
        if (list == null) {
            return views;
        }

        for (int i = 0; i < list.getLength(); i++) {
            Element aggr = (Element) list.item(i);
            NodeList hasViews = aggr.getElementsByTagNameNS(EDM.NS, EDM.hasView);
            for (int e = 0; e < hasViews.getLength(); e++) {
                Element hasView = (Element) hasViews.item(e);

                String attr = getReference(hasView);
                if (attr != null) {
                    views.add(attr);
                }
            }
        }
        return views;
    }

    private String getRecordID(String uri) {
        int i = uri.lastIndexOf('/');
        if (i <= 0) {
            return null;
        }
        i = uri.lastIndexOf('/', i - 1);
        return (i < 0 ? null : uri.substring(i));
    }


    // general DOM utility methods

    private List<Element> getElements(Element elem, String ns, String qname) {
        return toList(elem.getElementsByTagNameNS(ns, qname));
    }

    private List<Element> getElements(List<Element> list, String ns, String qname) {
        List<Element> ret = new ArrayList();
        for ( Element elem : list ) {
            append(elem.getElementsByTagNameNS(ns, qname), ret);
        }
        return ret;
    }

    private List<Element> append(NodeList nodeList, List<Element> list) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            list.add((Element) nodeList.item(i));
        }
        return list;
    }

    private List<Element> toList(NodeList nodeList) {
        return append(nodeList, new ArrayList());
    }

    private Collection<String> getIds(List<Element> list
            , Collection<String> ids) {
        for (Element e : list) {
            String uri = getId(e);
            if (uri != null) {
                ids.add(uri);
            }
        }
        return ids;
    }

    private String getURI(NodeList list) {
        if (list == null) {
            return null;
        }

        for (int i = 0; i < list.getLength(); i++) {
            Element cho = (Element) list.item(i);

            return getId(cho);
        }
        return null;
    }

    private String getId(Element elem) {
        String attr = elem.getAttributeNS(RDF.NS, RDF.about);
        return (StringUtils.isBlank(attr) ? null : attr);
    }

    private void setId(Element elem, String uri) {
        elem.setAttributeNS(RDF.NS, RDF.PREFIX + ":" + RDF.about, uri);
    }


    private boolean isReference(Element prop) {
        return prop.hasAttributeNS(RDF.NS, RDF.resource);
    }

    private String getReference(Element prop) {
        String attr = prop.getAttributeNS(RDF.NS, RDF.resource);
        return (StringUtils.isBlank(attr) ? null : attr);
    }

    private void setReference(Element prop, String uri) {
        prop.setAttributeNS(RDF.NS, RDF.PREFIX + ":" + RDF.resource, uri);
    }

    private boolean hasLanguage(Element prop) {
        String attr = prop.getAttributeNS(XMLConstants.XML_NS_URI, "lang");
        return StringUtils.isNotBlank(attr);
    }

    private boolean canBecomeReference(Element prop) {
        String name = prop.getLocalName();
        return (!name.equals(DC.identifier) 
             && !name.equals(EDM.year)
             && !name.equals(EDM.language) );
    }

    private void upgradeToReference(Element prop) {
        String str = prop.getTextContent();
        config.out.println("Upgrading reference: " + str);

        while (prop.hasChildNodes()) {
            prop.removeChild(prop.getFirstChild());
        }
        NamedNodeMap map = prop.getAttributes();
        while (map.getLength() > 0) {
            Node attr = map.item(0);
            map.removeNamedItem(attr.getNodeName());
        }

        setReference(prop, str);
    }


    public static class Result {
        public Document doc;
        public String uri;
        public List<String> views;

        public Result(Document doc, String uri, List<String> views) {
            this.doc = doc;
            this.uri = uri;
            this.views = views;
        }
    }
}
