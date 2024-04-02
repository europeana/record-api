package eu.europeana.api.record.migration;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import eu.europeana.api.record.model.media.WebResource;

/**
 * @author Hugo
 * @since 2 Nov 2023
 */
public class ViewComparator implements Comparator<WebResource> {

    public static void sort(List<WebResource> list, List<String> sorted) {
        if ( list.size() == 1 ) { return; }

        Collections.sort(list, new ViewComparator(sorted));
    }

    private List<String> sortedIDs;

    public ViewComparator(List<String> sortedIDs) {
        this.sortedIDs = sortedIDs;
    }

    @Override
    public int compare(WebResource wr1, WebResource wr2) {
        return ( wr1.equals(wr2) ? 0 : (getOrder(wr1) - getOrder(wr2)) );
    }

    private int getOrder(WebResource o1) { return sortedIDs.indexOf(o1.getID()); }
}
