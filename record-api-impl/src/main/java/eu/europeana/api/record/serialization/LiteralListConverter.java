package eu.europeana.api.record.serialization;

import com.fasterxml.jackson.databind.util.StdConverter;
import eu.europeana.api.record.datatypes.Literal;
import eu.europeana.api.record.impl.LiteralImpl;

import java.util.HashMap;
import java.util.Map;

public class LiteralListConverter extends StdConverter<Map<String, String>, Map<String, Literal<String>>> {

    @Override
    public Map<String, Literal<String>> convert(Map<String, String> map) {
        Map<String, Literal<String>> result = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            result.put(entry.getKey(),new LiteralImpl(entry.getValue()));
        }
        return result;
    }


}
