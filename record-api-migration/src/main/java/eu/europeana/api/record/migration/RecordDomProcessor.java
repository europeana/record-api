package eu.europeana.api.record.migration;

import eu.europeana.api.edm.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdfxml.xmlinput.DOM2Model;
import org.w3c.dom.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.util.*;

import static eu.europeana.api.record.migration.MigrationHandler.log;

/**
 * @author Hugo
 * @since 1 Nov 2023
 */
public class RecordDomProcessor {

    private static String PREFIX_ITEM = "http://data.europeana.eu/item";
    private static String PREFIX_AGGREGATION_EUROPEANA = "http://data.europeana.eu/aggregation/europeana";
    private static String PREFIX_PROXY_EUROPEANA = "http://data.europeana.eu/proxy/europeana";

    public String getRecordId(Document doc) {
        String uri = (doc.hasChildNodes()
                ? getURI(doc.getDocumentElement()
                .getElementsByTagNameNS(EDM.NS, EDM.ProvidedCHO))
                : null);
        return (uri != null ? uri.replace(PREFIX_ITEM, "") : null);
    }


    public Result process(Document doc) {
        Element root = doc.getDocumentElement();
        if (root == null) {
            return null;
        }


        List<Element> entities = getEntities(root);
        List<Element> proxies = append(root.getElementsByTagNameNS(ORE.NS, ORE.Proxy)
                , new ArrayList<Element>(3));

        Collection<String> entityIds = getIds(entities, new TreeSet<String>());

        //re-establish references
        processMetadata(root.getElementsByTagNameNS(ORE.NS, ORE.Proxy)
                , entityIds);

        //check for URIs that must be changed to relative
        Map<String, String> newIds = improveURIs(entityIds);
        if (!newIds.isEmpty()) {
            applyNewIdsToResources(entities, newIds);
            applyNewIdsToProperties(proxies, newIds);
        }

        processAggregations(root.getElementsByTagNameNS(ORE.NS, ORE.Aggregation));
        processAggregations(root.getElementsByTagNameNS(EDM.NS, EDM.EuropeanaAggregation));

        fixIdForResource(root.getElementsByTagNameNS(EDM.NS, EDM.EuropeanaAggregation)
                , PREFIX_AGGREGATION_EUROPEANA);


        List<String> views = getHasViews(root.getElementsByTagNameNS(ORE.NS, ORE.Aggregation)
                , new ArrayList<String>());

        String uri = getURI(root.getElementsByTagNameNS(EDM.NS, EDM.ProvidedCHO));
        if (uri == null) {
            return null;
        }

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

    private List<Element> append(NodeList nodeList, List<Element> list) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            list.add((Element) nodeList.item(i));
        }
        return list;
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

    private void processMetadata(NodeList list, Collection<String> ids) {
        for (int i = 0; i < list.getLength(); i++) {
            Element elem = (Element) list.item(i);
            NodeList props = elem.getChildNodes();
            for (int e = 0; e < props.getLength(); e++) {
                Element prop = (Element) props.item(e);
                if (isReference(prop) || !canBecomeReference(prop)) {
                    continue;
                }

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

    private void fixIdForResource(NodeList list, String uriPrefix) {
        if (list == null) {
            return;
        }

        for (int i = 0; i < list.getLength(); i++) {
            Element elem = (Element) list.item(i);
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
            log("Fixed URI to: " + uri);
        }
    }

    private Map<String, String> improveURIs(Collection<String> ids) {
        Map<String, String> ret = new HashMap();
        for (String id : ids) {
            if (isRelativeURI(id) || isFullURI(id)) {
                continue;
            }

            String newId = newRelativeURI(id);
            if (ids.contains(newId)) {
                log("Conflicting id: " + newId);
                continue;
            }
            ret.put(id, newId);
        }
        return ret;
    }

    private String newRelativeURI(String id) {
        return (id.contains("/") || id.contains("#") ? "/" : "#") + id;
    }

    private boolean isFullURI(String uri) {
        return uri.contains("://");
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
            String id = getId(elem);
            if (id == null) {
                continue;
            }

            String newId = newIds.get(id);
            if (newId == null) {
                continue;
            }

            log("Changed declared id: " + id + " => " + newId);
            setId(elem, newId);
        }
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

                log("Changed reference: " + ref + " => " + newId);
                setReference(prop, newId);
            }
        }
    }


    private void processAggregations(NodeList list) {
        if (list == null) {
            return;
        }

        for (int i = 0; i < list.getLength(); i++) {
            Element aggr = (Element) list.item(i);
            fixWebResourceURLs(aggr.getElementsByTagNameNS(EDM.NS, EDM.isShownAt));
            fixWebResourceURLs(aggr.getElementsByTagNameNS(EDM.NS, EDM.object));
            fixWebResourceURLs(aggr.getElementsByTagNameNS(EDM.NS, EDM.isShownBy));
            fixWebResourceURLs(aggr.getElementsByTagNameNS(EDM.NS, EDM.hasView));
            fixWebResourceURLs(aggr.getElementsByTagNameNS(EDM.NS, EDM.preview));
        }
    }

    private void fixWebResourceURLs(NodeList list) {
        for (int i = 0; i < list.getLength(); i++) {
            Element prop = (Element) list.item(i);
            String attr = getReference(prop);
            if (attr == null) {
                continue;
            }

            if (!attr.startsWith(PREFIX_ITEM)) {
                continue;
            }

            log("Fixed webresource reference: " + attr);
            setReference(prop, attr.replace(PREFIX_ITEM, ""));
        }
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

    private boolean canBecomeReference(Element prop) {
        return (!prop.getLocalName().equals(DC.identifier));
    }

    private void upgradeToReference(Element prop) {
        String str = prop.getTextContent();
        log("Upgrading reference: " + str);

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

    private String getRecordID(String uri) {
        int i = uri.lastIndexOf('/');
        if (i <= 0) {
            return null;
        }
        i = uri.lastIndexOf('/', i - 1);
        return (i < 0 ? null : uri.substring(i));
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

    public static final void main(String[] args) throws Throwable {
        File src = new File("C:\\Work\\incoming\\Record v3\\source\\urn_imss_indepth_100177.xml");
        Transformer t = TransformerFactory.newInstance().newTransformer();
        DOMResult result = new DOMResult();
        t.transform(new StreamSource(src), result);
        Result res = new RecordDomProcessor().process((Document) result.getNode());
        t.transform(new DOMSource(result.getNode()), new StreamResult(System.out));

        Model m = ModelFactory.createDefaultModel();

        DOM2Model dom2Model = DOM2Model.createD2M(res.uri, m);
        dom2Model.setProperty("allowBadURIs", "true");
        dom2Model.load(result.getNode());

        m.write(System.out, "RDF/XML");

    }
}
