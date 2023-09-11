package eu.europeana.api.record.model.internal;

import eu.europeana.api.record.model.data.LanguageTaggedLiteral;
import eu.europeana.api.record.model.data.Literal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @author Hugo
 * @since 7 Sep 2023
 */
public class LanguageMap 
{
    protected List<Literal<String>> list = new ArrayList<>();

    public List<Literal<String>> getValues()
    {
        return this.list;
    }

    public List<Literal<String>> getNonLanguageValues()
    {
        List<Literal<String>> ret = new ArrayList();
        for ( Literal<String> literal : this.list )
        {
            if ( literal instanceof LanguageTaggedLiteral) { continue; }
            ret.add(literal);
        }
        return ret;
    }

    public boolean hasNonLanguageValues()
    {
        for ( Literal<String> literal : list )
        {
            if ( !(literal instanceof LanguageTaggedLiteral) ) { return true; }
        }
        return false;
    }

    public LanguageTaggedLiteral getValue(String lang)
    {
        for (Literal<String> l : list)
        {
            if ( !(l instanceof LanguageTaggedLiteral) ) { continue; }

            LanguageTaggedLiteral ll = (LanguageTaggedLiteral)l;
            if ( ll.getLanguage().equals(lang) ) { return ll; }
        }
        return null;
    }

    public List<LanguageTaggedLiteral> getValues(String lang)
    {
        List<Literal<String>> ret = new ArrayList();
        for (Literal<String> literal : list)
        {
            if ( !(literal instanceof LanguageTaggedLiteral) ) { continue; }

            LanguageTaggedLiteral langLiteral = (LanguageTaggedLiteral)literal;
            if ( langLiteral.getLanguage().equals(lang) ) { ret.add(literal); }
        }
        return null;
    }

    public Collection<String> getLanguages()
    {
        Collection<String> col = new LinkedHashSet<String>();
        for ( Literal<String> literal : list )
        {
            if ( !(literal instanceof LanguageTaggedLiteral) ) { continue; }

            col.add(((LanguageTaggedLiteral)literal).getLanguage());
        }
        return col;
    }

    public void add(Literal<String> value) {
        list.add(value);
    }
}
