package eu.europeana.api.record.serialization;

import com.fasterxml.jackson.databind.util.StdConverter;
import eu.europeana.api.record.datatypes.Literal;
import eu.europeana.api.record.impl.LiteralImpl;

/**
 * Class for converting the String values present in json request to instance Literal<T>
 */
public class LiteralStringConverter extends StdConverter<String, Literal<String>> {

    @Override
    public Literal<String> convert(String s) {
        Literal<String> literal = new LiteralImpl<>();
        literal.setValue(s);
        return literal;
    }
}
