/**
 * 
 */
package eu.europeana.api.record.migration;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;

/**
 * @author Hugo
 * @since 11 Jun 2024
 */
public class ModelComparator
{

    public Model compare(Model m1, Model m2) {
        Model diff = m1.difference(m2);
        return cleanBlankNodes(diff);
    }

    private Model cleanBlankNodes(Model m) {
        ResIterator iter = m.listSubjects();
        while ( iter.hasNext() ) {
            Resource r = iter.next();
            if ( r.isAnon() ) { 
                r.removeProperties();
                m.removeAll(null, null, r);
            }
        }
        return m;
    }
}
