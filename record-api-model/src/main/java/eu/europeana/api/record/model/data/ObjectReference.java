package eu.europeana.api.record.model.data;

import dev.morphia.annotations.Entity;
import eu.europeana.api.edm.RDF;
import eu.europeana.api.record.model.EDMClass;

@Entity(discriminatorKey = RDF.type)
public interface ObjectReference extends DataValue {
    public String   getID();

    public boolean  isLocal();

    public boolean  isDereferenced();

    public EDMClass getDereferencedObject();
}
