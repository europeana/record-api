package eu.europeana.api.record.serialization;

import com.fasterxml.jackson.databind.util.StdConverter;
import eu.europeana.api.record.datatypes.ObjectReference;
import eu.europeana.api.record.impl.ObjectReferenceImpl;

import java.util.ArrayList;
import java.util.List;

public class ObjectReferenceConverter extends StdConverter<List<String>, List<ObjectReference>> {

    // TODO logic for referenced object if present in request should be added

    @Override
    public List<ObjectReference> convert(List<String> strings) {
        List<ObjectReference> values = new ArrayList<>();

        strings.stream().forEach(value -> {
            ObjectReference objectReference = new ObjectReferenceImpl();
            objectReference.setId(value);
            values.add(objectReference);
        });
        return values;
    }
}
