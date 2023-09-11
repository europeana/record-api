package eu.europeana.api.record.model;

import java.util.List;

public interface Aggregation {

    WebResource getIsShownAt();

    WebResource getIsShownBy();

    List<? extends WebResource> getViews();

    void setIsShownAt(WebResource isShownAt);

    void setIsShownBy(WebResource isShownBy);

}
