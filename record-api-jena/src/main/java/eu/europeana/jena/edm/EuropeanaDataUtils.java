package eu.europeana.jena.edm;

import org.apache.jena.rdf.model.Resource;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EuropeanaDataUtils
{
    public static final String NS     = "http://data.europeana.eu/";
    public static final String CHO_NS = NS + "item/";

    public static final String PATTERN_STR
        = "^http[:][/][/]data[.]europeana[.]eu[/]item[/](\\w+)[/](.+)$";
    public static final Pattern PATTERN = Pattern.compile(PATTERN_STR);

    private static final Map<Resource,String> DATA_PREFIXES = new LinkedHashMap();

    static {
        DATA_PREFIXES.put(EDM.ProvidedCHO, "item");
        DATA_PREFIXES.put(ORE.Aggregation, "aggregation");
    }

    public static String getURIforCHO(String localId)
    {
        return CHO_NS + localId;
    }

    public static String getURIforCHO(String dsID, String localId)
    {
        return CHO_NS + dsID + "/" + localId;
    }

    public static String[] getDatasetAndLocalID(String uri)
    {
        Matcher m = PATTERN.matcher(uri);
        if ( !m.find() ) { return null; }
        return new String[] { m.group(1), m.group(2) };
    }
}
