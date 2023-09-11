package eu.europeana.api.record.test;

import eu.europeana.api.record.model.*;
import eu.europeana.api.record.model.data.LanguageTaggedLiteral;
import eu.europeana.api.record.model.data.Literal;
import eu.europeana.api.record.model.data.LocalReference;
import eu.europeana.api.record.model.data.SharedReference;
import eu.europeana.api.record.model.entity.Agent;
import eu.europeana.api.record.model.entity.Place;
import eu.europeana.api.record.model.media.AudioMetadata;

import java.util.ArrayList;
import java.util.List;


/**
 * @author Hugo
 * @since 6 Aug 2023
 */
public class TestDataBuilder {

    public ProvidedCHO newRecord() {

        // add provided cho
        ProvidedCHO cho = new ProvidedCHO();
        cho.setId("http://data.europeana.eu/item/651/ABC");

        // add isagregatedBy
        Aggregation aggr = new Aggregation();
        aggr.setIsShownAt(new WebResource("http://www.memoriademadrid.es/buscador.php?accion=VerFicha&id=9982"));
        aggr.setIsShownBy(new WebResource("http://www.memoriademadrid.es/buscador.php?accion=VerFicha&id=9982"));
        cho.setIsAggregatedBy(aggr);

        // add proxies
        List<Proxy> proxies = new ArrayList<>();
        Proxy proxy1 = fillWithObjectMetadata(new Proxy());
        proxy1.setProxyIn(newAggregation());

        proxies.add(proxy1);

        Proxy proxy2 = fillWithSecondObjectMetadata(new Proxy());
        proxy2.setProxyIn(newEuropeanaAggregation());
        proxies.add(proxy2);

        cho.setProxies(proxies);

        // add contextual claases
        List<Agent> agents = new ArrayList<>();
        agents.add(newAgent1());

        return cho;
    }

    private Aggregation newAggregation() {
        Aggregation aggr = new Aggregation();
        aggr.setIsShownBy(newWebResource());
        return aggr;
    }

    private Aggregation newEuropeanaAggregation() {
        EuropeanaAggregation aggr = new EuropeanaAggregation();
        return aggr;
    }

    private WebResource newWebResource() {
        WebResource wr = new WebResource("http://www.bacarchive.org.uk/files/original/a49063bf0b5aa51fc03c0db014a04732.m4a");
        AudioMetadata techMeta = new AudioMetadata();
        techMeta.setMimetype(new Literal<>("audio/mp4"));
        techMeta.setFileByteSize(new Literal((long) 1234567));
        wr.setTechnicalMetadata(techMeta);
        return wr;
    }

    private Agent newAgent() {
        Agent agent = new Agent("http://www.wikidata.org/entity/Q41264");
        agent.addPrefLabel(new LanguageTaggedLiteral("Johannes Vermeer", "en"));
        agent.addPrefLabel(new LanguageTaggedLiteral("Johannes Vermeer", "nl"));
        agent.addPrefLabel(new Literal<>("Johannes Vermeer"));
        agent.addPrefLabel(new Literal<>("Johannes Vermeer"));
        return agent;
    }

    private Agent newAgent1() {
        Agent agent = new Agent("http://www.wikidata.org/entity/Q43675743");
        agent.addPrefLabel(new LanguageTaggedLiteral("happy ring", "en"));
        agent.addPrefLabel(new LanguageTaggedLiteral("happier", "nl"));
        return agent;
    }

    private Place newPlace() {
        Place place = new Place("http://data.europeana.eu/place/178793");
        place.addPrefLabel(new LanguageTaggedLiteral("Combe Motte", "en"));
        place.addPrefLabel(new LanguageTaggedLiteral("Combe Motte", "sv"));

        return place;
    }

    private <T extends ObjectMetadata> T fillWithObjectMetadata(T meta) {
        meta.addTitle(new Literal("Title"));
        meta.addTitle(new Literal("Título"));
        meta.addTitle(new LanguageTaggedLiteral("Title", "en"));
        meta.addTitle(new LanguageTaggedLiteral("Título", "pt"));

        meta.addAlternativeTitle(new Literal("Alternative title"));
        meta.addAlternativeTitle(new Literal("Título alternative"));
        meta.addAlternativeTitle(new LanguageTaggedLiteral("Alternative title", "en"));
        meta.addAlternativeTitle(new LanguageTaggedLiteral("Título alternative", "pt"));

        meta.addCreator(new Literal("Vermeer"));
        meta.addCreator(new Literal("Johanes Vermeer"));
        meta.addCreator(new SharedReference(newAgent()));

        meta.getDescriptions().add(new Literal("Descricao"));
        meta.getDescriptions().add(new LanguageTaggedLiteral("Description", "en"));
        meta.getDescriptions().add(new SharedReference("https://en.wikipedia.org/wiki/Buccin"));

        meta.getIdentifiers().add(new Literal("12345"));
        return meta;
    }

    private <T extends ObjectMetadata> T fillWithSecondObjectMetadata(T meta) {
        meta.addTitle(new LanguageTaggedLiteral("Trombone buccin. Pas nominal", "fr"));
        meta.addTitle(new LanguageTaggedLiteral("Trombone whelk. Pitch nominal", "en"));

        meta.addCreator(new SharedReference(newPlace()));
        meta.addCreator(new LocalReference("#Cortious_agent"));

        Agent edmObject = new Agent("#helloTestingLocalReference");
        edmObject.addPrefLabel(new LanguageTaggedLiteral("paris", "fr"));
        edmObject.addPrefLabel(new LanguageTaggedLiteral("paris", "en"));
        meta.addCreator(new LocalReference(edmObject));

        meta.getDescriptions().add(new Literal("Technical description: Brass; ligature fitting on bell section at joint; stockings on main slides. Bell with one coil"));
        meta.getDescriptions().add(new LanguageTaggedLiteral("Description technique: Laiton; raccord de ligature sur la section de cloche au joint", "fr"));

        return meta;
    }
}
