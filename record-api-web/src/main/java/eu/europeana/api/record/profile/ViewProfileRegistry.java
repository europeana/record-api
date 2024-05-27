/**
 * 
 */
package eu.europeana.api.record.profile;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import dev.morphia.query.FindOptions;
import dev.morphia.query.Projection;
import dev.morphia.query.ProjectionNew;

/**
 * @author Hugo
 * @since 29 Apr 2024
 */
public class ViewProfileRegistry
{
    private static final Map<String,Profile> profiles = new HashMap();

    static {

        //Metadata profiles

        Profile metaBasic = newProfile("meta.basic").expand("id").expandBase("proxies"
         , "id", "type", "title", "description", "creator", "edmType" 
        );

        newProfile("meta.elemental").include(metaBasic).expandBase("proxies"
         , "created", "issued", "temporal", "hasMet", "subject", "format"
         , "dcType", "medium", "contributor", "publisher", "subject"
         , "spatial", "currentLocation"
        );

        newProfile("meta.full").include(metaBasic).expandBase("proxies"
          , "alternative", "description", "tableOfContents", "edmType"
          , "language", "creator", "contributor", "publisher", "created"
          , "issued", "temporal", "date", "year", "spatial", "currentLocation"
          , "coverage", "subject", "dcType", "format", "medium", "conformsTo"
          , "extent", "hasType", "hasMet", "isRelatedTo", "dcRights"
          , "provenance", "realizes", "source", "relation"
          , "isPartOf", "hasPart", "isNextInSequence", "isFormatOf", "hasFormat"
          , "isVersionOf", "hasVersion", "isReferencedBy", "references", "isReplacedBy"
          , "replaces", "isRequiredBy", "requires", "incorporates", "isDerivativeOf"
          , "isRepresentationOf", "isSimilarTo", "isSuccessorOf"
          , "identifier", "sameAs"
        );
        
        //Provenance profiles

        Profile provBasic = newProfile("prov.basic")
                                .expand("id", "proxies.id", "proxies.type")
                                .expandBase("proxies.proxyIn"
         , "id", "type", "dataProvider", "rights", "landingPage"
        );

        newProfile("prov.full").include(provBasic).expandBase("proxies.proxyIn"
          , "dataProvider", "intermediateProvider", "provider"
          , "datasetName", "country", "language", "rights", "dcRights"
          , "ugc", "created", "modified", "completeness", "hasQualityAnnotation"
         );

        //Media profiles

        Profile mediaBasic = newProfile("media.basic")
                .expand("id", "proxies.id", "proxies.type")
                .expandBase("proxies.proxyIn", 
                            "id", "type", "isShownBy.id", "isShownAt.id", "preview");

        Profile mediaAll = newProfile("media.all").include(mediaBasic).expand(
            "proxies.proxyIn.object.id"
          , "proxies.proxyIn.hasView.id"
        );

        newProfile("media.meta").include(mediaAll)
        .expandBase("proxies.proxyIn.isShownAt"
        , "description", "creator", "created", "issued", "dcType", "format"
        , "conformsTo", "extent", "rights", "dcRights", "source", "isPartOf"
        , "hasPart", "isPartOf", "isFormatOf", "isReferencedBy"
        , "isNextInSequence", "hasService", "sameAs")
        .expandBase("proxies.proxyIn.isShownBy"
        , "description", "creator", "created", "issued", "dcType", "format"
        , "conformsTo", "extent", "rights", "dcRights", "source", "isPartOf"
        , "hasPart", "isPartOf", "isFormatOf", "isReferencedBy"
        , "isNextInSequence", "hasService", "sameAs")
        .expandBase("proxies.proxyIn.object"
        , "description", "creator", "created", "issued", "dcType", "format"
        , "conformsTo", "extent", "rights", "dcRights", "source", "isPartOf"
        , "hasPart", "isPartOf", "isFormatOf", "isReferencedBy"
        , "isNextInSequence", "hasService", "sameAs")
        .expandBase("proxies.proxyIn.hasView"
        , "description", "creator", "created", "issued", "dcType", "format"
        , "conformsTo", "extent", "rights", "dcRights", "source", "isPartOf"
        , "hasPart", "isPartOf", "isFormatOf", "isReferencedBy"
        , "isNextInSequence", "hasService", "sameAs");

        

        newProfile("media.techmeta").include(mediaAll)
        .expandBase("proxies.proxyIn.isShownAt.techMeta"
         , "type", "width", "height", "hasMimeType", "fileByteSize"
         , "duration", "codecName", "sampleSize", "sampleRate", "bitRate"
         , "frameRate", "audioChannelNumber"
         , "hasColorSpace", "componentColor", "orientation"
         , "spatialResolution", "fulltext")
        .expandBase("proxies.proxyIn.isShownBy.techMeta"
         , "type", "width", "height", "hasMimeType", "fileByteSize"
         , "duration", "codecName", "sampleSize", "sampleRate", "bitRate"
         , "frameRate", "audioChannelNumber"
         , "hasColorSpace", "componentColor", "orientation"
         , "spatialResolution", "fulltext")
        .expandBase("proxies.proxyIn.object.techMeta"
         , "type", "width", "height", "hasMimeType", "fileByteSize"
         , "duration", "codecName", "sampleSize", "sampleRate", "bitRate"
         , "frameRate", "audioChannelNumber"
         , "hasColorSpace", "componentColor", "orientation"
         , "spatialResolution", "fulltext")
        .expandBase("proxies.proxyIn.hasView.techMeta"
         , "type", "width", "height", "hasMimeType", "fileByteSize"
         , "duration", "codecName", "sampleSize", "sampleRate", "bitRate"
         , "frameRate", "audioChannelNumber"
         , "hasColorSpace", "componentColor", "orientation"
         , "spatialResolution", "fulltext"
        );

        newProfile("media.full").include(mediaBasic).expand(
            "proxies.proxyIn.isShownAt"
          , "proxies.proxyIn.isShownBy"
          , "proxies.proxyIn.object"
          , "proxies.proxyIn.hasView"
        );

        // Compound profiles

        /*
        snippet = meta.basic + prov.basic + media.basic
        */
       newProfile("snippet").include(metaBasic, provBasic, mediaBasic);


    }

    public static Collection<String> getProfileNames() {
        return profiles.keySet();
    }

    public static Profile getProfile(String name) {
        return profiles.get(name);
    }

    public static Profile buildProfile(String... profileNames) {
        Profile combined = new Profile(null);
        for ( String name : profileNames ) {
            Profile prof = profiles.get(name);
            if ( prof != null ) { combined.include(prof); }
        }
        return combined.clean();
    }

    public static FindOptions getProjection(String... profileNames) {
        FindOptions opts = new FindOptions();
        Projection  proj = newProjection(opts);
        proj.include(buildProfile(profileNames).toArray(new String[] {}));
        return opts;
    }

    private static Profile newProfile(String name) {
        Profile profile = new Profile(name);
        profiles.put(name, profile);
        return profile;
    }

    private static Projection newProjection(FindOptions opts) {
        Projection  p    = new ProjectionNew(opts);
        try
        {
            Field f = FindOptions.class.getDeclaredField("projection");
            f.setAccessible(true);
            f.set(opts, p);
        }
        catch (NoSuchFieldException | SecurityException 
             | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return p;
    }

    private static class Profile extends TreeSet<String> {

        private String name;

        public Profile(String name) { this.name = name; }

        protected Profile remove(String... fields) {
            for ( String field : fields ) { this.remove(field); }
            return this;
        }

        protected Profile removeAll(String base) {
            Iterator<String> iter = this.iterator();
            while ( iter.hasNext() ) {
                String field = iter.next();
                if ( field.startsWith(base) ) { iter.remove(); }
            }
            return this;
        }

        protected Profile expand(String... fields) {
            for ( String field : fields ) { this.add(field); }
            return this;
        }

        protected Profile expandBase(String base, String... fields) {
            for ( String field : fields ) { this.add(base + "." + field); }
            return this;
        }

        protected Profile include(Profile... profiles) {
            for ( Profile p : profiles ) {
                for ( String field : p ) { this.add(field); }
            }
            return this;
        }

        protected Profile clean() {
            Iterator<String> iter = this.iterator();
            String node = ".";
            while ( iter.hasNext() ) {
                String field = iter.next();
                if ( field.startsWith(node) ) { iter.remove(); continue; }
                node = field + ".";
            }
            return this;
        }
    }
}
