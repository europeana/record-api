package eu.europeana.api.record.model.data;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.morphia.annotations.Entity;
import eu.europeana.api.record.json.ObjectReferenceSerializer;
import eu.europeana.api.record.model.EDMClass;

@Entity(discriminatorKey = "type")
@JsonSerialize(using = ObjectReferenceSerializer.class)
public interface ObjectReference {

    String getID();

    boolean isLocal();

    boolean isDereferenced();

    EDMClass getDereferencedObject();

}
