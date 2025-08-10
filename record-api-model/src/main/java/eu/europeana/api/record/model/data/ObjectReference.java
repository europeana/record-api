package eu.europeana.api.record.model.data;

import dev.morphia.annotations.Entity;
import eu.europeana.api.edm.RDF;

@Entity(discriminatorKey = RDF.type)
public interface ObjectReference extends DataValue {

    public String   getID();

    public boolean  isLocal();

    public boolean  isDereferenced();

    public SharedObject getDereferencedObject();

    public void setDereferencedObject(SharedObject obj);
}
