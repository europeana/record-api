package eu.europeana.api.record.model;

import eu.europeana.api.record.datatypes.Literal;

import java.util.List;
import java.util.Map;

public interface Proxy {

    Literal<String> getType();

    EuropeanaAggregation getProxyIn();

    Literal<String> getProxyFor();

    Literal<String> getTitle(String language);

    Literal<String> getCreator(String language);

    Literal<String> getAlternative(String language);

    Literal<String> getDescription(String language);

    Literal<String> getIdentifier(String language);

    List<Literal<String>> getTitles();

    List<Literal<String>> getAlternatives();

    List<Literal<String>> getCreators();

    List<Literal<String>> getIdentifiers();

    List<Literal<String>> getDescriptions();

    void setType(Literal<String> type);

    void setTitle(Map<String, List<Literal<String>>> title);

    void setAlternative(Map<String, List<Literal<String>>> alternative);

    void setDescription(Map<String, List<Literal<String>>> description);

    void setCreator(Map<String, List<Literal<String>>> creator);

    void setIdentifier(Map<String, List<Literal<String>>> identifier);

    void setProxyIn(EuropeanaAggregation proxyIn);

    void setProxyFor(Literal<String> proxyFor);

}
