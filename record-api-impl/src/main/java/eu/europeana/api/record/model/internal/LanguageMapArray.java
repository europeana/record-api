package eu.europeana.api.record.model.internal;

import eu.europeana.api.record.model.data.LanguageTaggedLiteral;
import eu.europeana.api.record.model.data.Literal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hugo
 * @since 7 Sep 2023
 */
public class LanguageMapArray extends LanguageMap {

    public List<LanguageTaggedLiteral> getValues(String lang) {
        List<LanguageTaggedLiteral> ret = new ArrayList();
        for (Literal<String> literal : list) {
            if (!(literal instanceof LanguageTaggedLiteral)) {
                continue;
            }

            LanguageTaggedLiteral langLiteral = (LanguageTaggedLiteral) literal;
            if (langLiteral.getLanguage().equals(lang)) {
                ret.add(langLiteral);
            }
        }
        return ret;
    }
}
