package eu.europeana.api.record.impl;

import eu.europeana.api.record.datatypes.Literal;

import java.util.List;
import java.util.Map;

public class Proxy extends EdmEntity {

    // TODO for def -> @none there are multiple values present
    private Literal<String> type;
    private Map<Literal<String>, List<Literal<String>>> title;

    // todo
    private Map<Literal<String>, List<Literal<String>>> alternative;
    private Map<Literal<String>, List<Literal<String>>> description;
    private Map<Literal<String>, List<Literal<String>>> creator;
    private Map<Literal<String>, List<Literal<String>>> identifier;
    private List<Literal<String>> proxyIn;
    private Literal<String> proxyFor;

    public Literal<String> getType() {
        return type;
    }

    public void setType(Literal<String> type) {
        this.type = type;
    }

    public Literal<String>  getTitle(String language) {
        return title.get(language).get(0);
    }

    public void setTitle(Map<Literal<String>,List<Literal<String>>> title) {
        this.title = title;
    }

    public Literal<String> getAlternative(String language) {
        return alternative.get(language).get(0);
    }

    public void setAlternative(Map<Literal<String>, List<Literal<String>>> alternative) {
        this.alternative = alternative;
    }

    public Literal<String> getDescription(String language) {
        return description.get(language).get(0);
    }

    public void setDescription(Map<Literal<String>, List<Literal<String>>> description) {
        this.description = description;
    }

    public Literal<String> getCreator(String language) {
        return creator.get(language).get(0);
    }

    public void setCreator(Map<Literal<String>, List<Literal<String>>> creator) {
        this.creator = creator;
    }

    public Literal<String> getIdentifier(String language) {
        return identifier.get(language).get(0);
    }

    public void setIdentifier(Map<Literal<String>, List<Literal<String>>> identifier) {
        this.identifier = identifier;
    }

    public List<Literal<String>> getProxyIn() {
        return proxyIn;
    }

    public void setProxyIn(List<Literal<String>> proxyIn) {
        this.proxyIn = proxyIn;
    }

    public Literal<String> getProxyFor() {
        return proxyFor;
    }

    public void setProxyFor(Literal<String> proxyFor) {
        this.proxyFor = proxyFor;
    }

    // this is for @none
    public List<Literal<String>> getTitles() {
        return title.get("@none");
    }

    public List<Literal<String>> getAlternatives() {
        return alternative.get("@none");
    }

    public List<Literal<String>> getCreators() {
        return creator.get("@none");
    }

    public List<Literal<String>> getIdentifiers() {
        return identifier.get("@none");
    }



}
