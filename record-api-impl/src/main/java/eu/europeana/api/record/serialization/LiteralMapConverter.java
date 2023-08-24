package eu.europeana.api.record.serialization;

import com.fasterxml.jackson.databind.util.StdConverter;
import eu.europeana.api.record.datatypes.Literal;
import eu.europeana.api.record.impl.LiteralImpl;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static eu.europeana.api.record.vocabulary.RecordFields.NON_LANGUAGE_TAGGED;
import static eu.europeana.api.record.vocabulary.RecordFields.NONE;

/**
 * Class for converting the Map present in json request to instance Literal<T> Maps
 */
public class LiteralMapConverter extends StdConverter<Map<String, List<String>>, Map<String, List<Literal<String>>>> {

    @Override
    public Map<String, List<Literal<String>>> convert(Map<String, List<String>> stringListMap) {
        Map<String, List<Literal<String>>> result = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : stringListMap.entrySet()) {
            List<Literal<String>> values= new ArrayList<>();

            entry.getValue().stream().forEach(value -> values.add(new LiteralImpl(value)));

            if (entry.getKey().isEmpty() || StringUtils.equals(entry.getKey(), NON_LANGUAGE_TAGGED)) {
                result.put(NONE, values);
            } else {
                result.put(entry.getKey(), values);
            }
        }
        return result;
    }


}
