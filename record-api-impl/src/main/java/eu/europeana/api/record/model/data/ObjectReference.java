package eu.europeana.api.record.model.data;

import dev.morphia.annotations.Entity;
import eu.europeana.api.record.model.EDMObject;

@Entity(discriminatorKey = "type")
public interface ObjectReference {

    String getID();

    boolean isLocal();

    boolean isDereferenced();

    EDMObject getDereferencedObject();

}
