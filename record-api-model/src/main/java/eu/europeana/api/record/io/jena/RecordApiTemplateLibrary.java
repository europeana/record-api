package eu.europeana.api.record.io.jena;

import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.config.MediaTypeConfig;
import eu.europeana.api.edm.NamespaceResolver;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author Hugo
 * @since 17 Oct 2023
 * @refractored Srishti Singh on 17 November 2023
 */
@Component(AppConfigConstants.BEAN_RECORD_TEMPLATE_LIBRARY)
@Import(MediaTypeConfig.class)
public class RecordApiTemplateLibrary extends TemplateLibrary {

    private static final Logger LOG = LogManager.getLogger(RecordApiTemplateLibrary.class);

    @Resource(name = AppConfigConstants.BEAN_MEDIA_TYPES)
    private MediaTypes mediaTypes;

    // TODO add a comment for this method and class what it does
    @Autowired
    public RecordApiTemplateLibrary(CodecRegistry codecRegistry, NamespaceResolver namespaceResolver, DefaultUriNormalizer defaultUriNormalizer) {
        super(codecRegistry, namespaceResolver, defaultUriNormalizer);

        codecRegistry.addCodec(EdmTypeCodec.INSTANCE);
        codecRegistry.addCodec(DatatypeLiteralCodec.INSTANCE);
        codecRegistry.addCodec(LiteralCodec.INSTANCE);
        codecRegistry.addCodec(ObjectReferenceCodec.INSTANCE);
        codecRegistry.addCodec(DataValueCodec.INSTANCE);
        codecRegistry.addCodec(LanguageLiteralCodec.INSTANCE);
        codecRegistry.addCodec(LanguageMapCodec.INSTANCE);
        codecRegistry.addCodec(LanguageMapArrayCodec.INSTANCE);
        codecRegistry.addCodec(new TechnicalMetadataCodec(mediaTypes));

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

        LOG.info("RecordApiTemplateLibrary configuration added ....");
    }
}
