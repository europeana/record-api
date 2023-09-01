package eu.europeana.api.record.deserialization;

import com.fasterxml.jackson.databind.util.StdConverter;
import eu.europeana.api.record.datatypes.*;
import eu.europeana.api.record.impl.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


public class DataValueConverter extends StdConverter<Object , List<? extends DataValue>> {

    private static final String KEY_VALUE = "value";
    private static final String KEY_LANG = "language";
    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";

    @Override
    public List<DataValue> convert(Object o) {
        List<DataValue> values = new ArrayList<>();

        if (List.class.isAssignableFrom(o.getClass())) {
            List<Object> objects = new ArrayList<>();
            objects.addAll(((List<?>) o).stream().toList());

            for (Object object : objects) {
                if (LinkedHashMap.class.isAssignableFrom(object.getClass())) {
                    LinkedHashMap<String, String> map = (LinkedHashMap<String, String>) object;
                    addLiterals(map, values);
                    addObjectReferences(map, values);
                }
            }
        }
        return values;
    }

    /**
     * Adds language or non language tagged Literal if present in the map
     * @param map map containing the values
     * @param values list where the literals are added
     */
    private void addLiterals(LinkedHashMap<String, String> map, List<DataValue> values) {
        if (map.containsKey(KEY_VALUE) && map.size() == 1) {
            values.add(new LiteralImpl<String>(map.get(KEY_VALUE)));
        }
        if (map.containsKey(KEY_VALUE) && map.containsKey(KEY_LANG) && map.size() == 2) {
            values.add(new LanguageTaggedLiteralImpl<>(map.get(KEY_VALUE), map.get(KEY_LANG)));
        }
    }

    /**
     * Adds object references if present in the map
     * @param map map containing the values
     * @param values list where the Object references are added
     */
    private void addObjectReferences(LinkedHashMap<String, String> map, List<DataValue> values) {
        if (map.containsKey(KEY_ID)) {
            ObjectReference objectReference = new ObjectReferenceImpl(map.get(KEY_ID));
            if (map.size() > 1) {
                 map.remove(KEY_ID);
                 objectReference.setReferencedObject(map);
            }
            values.add(objectReference);
        }
    }
}