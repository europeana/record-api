package eu.europeana.api.record.model.internal;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import eu.europeana.api.record.model.data.LocalReference;
import eu.europeana.api.record.model.data.ObjectReference;
import eu.europeana.api.record.model.media.*;

/**
 * @author Hugo
 * @since 28 Oct 2023
 */
public class WebResourceComparator implements Comparator<WebResource> {

    @Override
    public int compare(WebResource o1, WebResource o2) {
        if ( o1.equals(o2) ) { return 0; }

        boolean h1 = o1.hasIsNextInSequences();
        boolean h2 = o2.hasIsNextInSequences();
        if ( !h1 && !h2 ) { return o1.getID().compareTo(o2.getID()); }

        if ( h1 ) {
            if ( isAfter(o1, o2) ) { return 1; }
        }
        if ( h2 ) {
            if ( isAfter(o2, o1) ) { return -1; }
        }
        return o1.getID().compareTo(o2.getID());
    }

    private boolean isAfter(WebResource o1, WebResource o2) {
        for ( ObjectReference ref : o1.getIsNextInSequences() ) {
            if ( !ref.isDereferenced() ) { continue; }

            WebResource before = (WebResource)ref.getDereferencedObject();
            if ( before.equals(o2) || isAfter(before, o2) ) { return true; }
        }
        return false;
    }

    public static final void main(String[] args) {
        WebResource wr1 = new WebResource("#wr1");
        WebResource wr2 = new WebResource("#wr2");
        WebResource wr3 = new WebResource("#wr3");
        WebResource wr4 = new WebResource("#wr4");
        WebResource wr5 = new WebResource("#wr5");
        WebResource wr6 = new WebResource("#wr6");
        WebResource wr7 = new WebResource("#wr7");
        WebResource wr8 = new WebResource("#wr8");
        WebResource wr9 = new WebResource("#wr9");
        WebResource wr10 = new WebResource("#wr10");
        WebResource wr11 = new WebResource("#wr11");

        wr10.addIsNextInSequence(new LocalReference(wr9));
        wr11.addIsNextInSequence(new LocalReference(wr10));

//        wr5.addIsNextInSequence(new LocalReference(wr6));
//        wr9.addIsNextInSequence(new LocalReference(wr11));
//        wr11.addIsNextInSequence(new LocalReference(wr5));

        Collection<WebResource> col = new TreeSet(new WebResourceComparator());
        col.add(wr1);
        col.add(wr2);
        col.add(wr3);
        col.add(wr4);
        col.add(wr5);
        col.add(wr6);
        col.add(wr7);
        col.add(wr8);
        col.add(wr9);
        col.add(wr10);
        col.add(wr11);
        for ( WebResource wr : col ) {
            System.out.println(wr);
        }
    }
}
