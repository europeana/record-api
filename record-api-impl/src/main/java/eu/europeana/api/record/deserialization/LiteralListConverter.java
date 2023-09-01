package eu.europeana.api.record.deserialization;

import com.fasterxml.jackson.databind.util.StdConverter;
import eu.europeana.api.record.datatypes.Literal;
import eu.europeana.api.record.impl.LiteralImpl;

import java.util.ArrayList;
import java.util.List;

public class LiteralListConverter extends StdConverter<List<String>,  List<Literal<String>>> {

    @Override
    public List<Literal<String>> convert(List<String> strings) {
        List<Literal<String>> values = new ArrayList<>();
        strings.stream().forEach(value -> values.add(new LiteralImpl<>(value)));
        return values;
    }
}
