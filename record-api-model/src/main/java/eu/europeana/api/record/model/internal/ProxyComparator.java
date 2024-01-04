package eu.europeana.api.record.model.internal;

import eu.europeana.api.record.model.Proxy;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * @author Hugo
 * @since 28 Oct 2023
 */
public class ProxyComparator implements Comparator<Proxy> {

    public static ProxyComparator INSTANCE = new ProxyComparator();

    @Override
    public int compare(Proxy p1, Proxy p2) {
        if ( p1.equals(p2) ) { return 0; }

        boolean h1 = p1.hasLineages();
        boolean h2 = p2.hasLineages();
        if ( h1 == false ) { return  1; }
        if ( h2 == false ) { return -1; }

        return ( isAfter(p1, p2) ? -1 : 1 );
    }

    private boolean isAfter(Proxy p1, Proxy p2) {
        if ( !p1.hasLineages() ) { return false; }

        for ( Proxy proxy : p1.getLineages() ) {
            if ( proxy.equals(p2) || isAfter(proxy, p2) ) { return true; }
        }
        return false;
    }

    public static final void main(String[] args) {
        Proxy p1 = new Proxy("#p1");
        Proxy p2 = new Proxy("#p2");
        Proxy p3 = new Proxy("#p3");
        Proxy p4 = new Proxy("#p4");
        Proxy p5 = new Proxy("#p5");

        p1.addLineage(p2);
        p2.addLineage(p3);
        p3.addLineage(p4);
        p4.addLineage(p5);

        Collection<Proxy> col = new TreeSet(new ProxyComparator());
        col.add(p1);
        col.add(p2);
        col.add(p3);
        col.add(p4);
        col.add(p5);
        for ( Proxy p : col ) {
            System.out.println(p);
        }
    }
}
