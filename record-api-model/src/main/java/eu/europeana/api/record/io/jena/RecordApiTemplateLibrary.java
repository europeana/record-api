/**
 * 
 */
package eu.europeana.api.record.io.jena;
import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.config.MediaTypeConfig;
import eu.europeana.api.edm.Namespaces;
import eu.europeana.api.model.MediaTypes;
import eu.europeana.api.record.model.Aggregation;
import eu.europeana.api.record.model.EuropeanaAggregation;
import eu.europeana.api.record.model.ProvidedCHO;
import eu.europeana.api.record.model.Proxy;
import eu.europeana.api.record.model.entity.*;
import eu.europeana.api.record.model.media.*;
import eu.europeana.jena.encoder.codec.CodecRegistry;
import eu.europeana.jena.encoder.library.DefaultUriNormalizer;
import eu.europeana.jena.encoder.library.TemplateLibrary;

import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.IOException;


/**
 * @author Hugo
 * @since 17 Oct 2023
 */
@Configuration
public class RecordApiTemplateLibrary extends TemplateLibrary {

    public static RecordApiTemplateLibrary INSTANCE = new RecordApiTemplateLibrary();

//    @Resource(name = AppConfigConstants.BEAN_MEDIA_TYPES)
    private MediaTypeConfig mediaTypeConfig = new MediaTypeConfig();


//    @Bean(AppConfigConstants.BEAN_RECORD_TEMPLATE_LIBRARY)
//    public RecordApiTemplateLibrary getRecordApiTemplateLibrary() {
//        return new RecordApiTemplateLibrary();
//    }

    // TODO add a comment for this method and class what it does
    public RecordApiTemplateLibrary() {
        super(new CodecRegistry(), Namespaces.getNamespaceResolver()
            , DefaultUriNormalizer.INSTANCE);

        // TODO fix it in better way
        MediaTypes mediaTypes = null;
        try {
            mediaTypes = mediaTypeConfig.getMediaTypes();
            System.out.println("here media types");
        } catch (IOException e) {
            e.printStackTrace();
        }

        CodecRegistry registry = getCodecRegistry();
        registry.addCodec(EdmTypeCodec.INSTANCE);
        registry.addCodec(DatatypeLiteralCodec.INSTANCE);
        registry.addCodec(LiteralCodec.INSTANCE);
        registry.addCodec(ObjectReferenceCodec.INSTANCE);
        registry.addCodec(DataValueCodec.INSTANCE);
        registry.addCodec(LanguageLiteralCodec.INSTANCE);
        registry.addCodec(LanguageMapCodec.INSTANCE);
        registry.addCodec(LanguageMapArrayCodec.INSTANCE);
        registry.addCodec(new TechnicalMetadataCodec(mediaTypes));

        importClass(Aggregation.class);
        importClass(EuropeanaAggregation.class);
        importClass(ProvidedCHO.class);
        importClass(Proxy.class);
        //contextual classes
        importClass(Agent.class);
        importClass(Place.class);
        importClass(Concept.class);
        importClass(TimeSpan.class);
        importClass(Organization.class);
        importClass(Address.class);
        importClass(License.class);
        importClass(QualityAnnotation.class);
        //media
        importClass(WebResource.class);
        importClass(TechnicalMetadata.class);
        importClass(AudioMetadata.class);
        importClass(ImageMetadata.class);
        importClass(M3DMetadata.class);
        importClass(TextMetadata.class);
        importClass(VideoMetadata.class);
        importClass(Service.class);
    }
}
