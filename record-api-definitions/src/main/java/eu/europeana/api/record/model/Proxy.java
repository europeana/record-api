package eu.europeana.api.record.model;

import eu.europeana.api.record.datatypes.DataValue;
import eu.europeana.api.record.datatypes.Literal;

import java.util.List;
import java.util.Map;

public interface Proxy {

    Literal<String> getType();

    EuropeanaAggregation getProxyIn();

    Literal<String> getProxyFor();

    Literal<String> getTitle(String language);

    List<? extends DataValue> getCreator();

    Literal<String> getAlternative(String language);

    List<? extends DataValue> getDescription();

    List<Literal<String>> getIdentifier();

    List<Literal<String>> getAlternatives();

    void setType(Literal<String> type);

    void setTitle(Map<String, Literal<String>> title);

    void setAlternative(Map<String, List<Literal<String>>> alternative);

    void setDescription(List<? extends DataValue> description);

    void setCreator(List<? extends DataValue> creator);

    void setIdentifier(List<Literal<String>> identifier);

    void setProxyIn(EuropeanaAggregation proxyIn);

    void setProxyFor(Literal<String> proxyFor);

}
