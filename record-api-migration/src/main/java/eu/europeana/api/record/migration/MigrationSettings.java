package eu.europeana.api.record.migration;

import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.config.MediaTypeConfig;
import eu.europeana.api.edm.Namespaces;
import eu.europeana.api.model.MediaTypes;
import eu.europeana.api.record.db.config.DataSourceConfig;
import eu.europeana.api.record.io.FormatHandlerRegistry;
import eu.europeana.api.record.io.JenaBasedFormatWriter;
import eu.europeana.api.record.io.RecordIOConfig;
import eu.europeana.api.record.io.jena.RecordApiTemplateLibrary;
import eu.europeana.api.record.io.json.JsonLdWriter;
import eu.europeana.api.record.io.xml.XmlRecordWriter;
import eu.europeana.jena.encoder.codec.CodecRegistry;
import eu.europeana.jena.encoder.library.DefaultUriNormalizer;
import eu.europeana.jena.encoder.library.TemplateLibrary;
import jakarta.annotation.Resource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

import com.fasterxml.jackson.databind.ObjectMapper;

import static eu.europeana.api.config.AppConfigConstants.*;


@Configuration
@PropertySource(
        value = {"classpath:migration.properties", "classpath:migration.user.properties"},
        ignoreResourceNotFound = true)
@Import({ MediaTypeConfig.class, DataSourceConfig.class
        , RecordIOConfig.class, JsonLdWriter.class, XmlRecordWriter.class
        , RecordApiTemplateLibrary.class, FormatHandlerRegistry.class})
public class MigrationSettings {

    @Resource(name = AppConfigConstants.BEAN_MEDIA_TYPES)
    private MediaTypes mediaTypes;

    @Resource( name = BEAN_JSON_MAPPER)
    private ObjectMapper mapper;

    /*
    @Bean
    public FormatHandlerRegistry getFormatHandlerRegistry(
            @Qualifier(BEAN_FORMAT_WRITER_JSONLD) JsonLdWriter jsonLdWriter
          , @Qualifier(BEAN_FORMAT_WRITER_XML) XmlRecordWriter xmlRecordWriter
          , @Qualifier(BEAN_FORMAT_WRITER_TURTLE) JenaBasedFormatWriter jenaBasedTurtleWriter
          , @Qualifier(BEAN_FORMAT_WRITER_N3) JenaBasedFormatWriter jenaBasedN3Writer
          , @Qualifier(BEAN_FORMAT_WRITER_NT) JenaBasedFormatWriter jenaBasedNTWriter) {
        return new FormatHandlerRegistry(
                jsonLdWriter,
                xmlRecordWriter,
                jenaBasedTurtleWriter,
                jenaBasedN3Writer,
                jenaBasedNTWriter);
    }
    public FormatHandlerRegistry getFormatHandlerRegistry() {
        return new FormatHandlerRegistry(
                new JsonLdWriter(),
                new XmlRecordWriter(),
                new JenaBasedFormatWriter("TURTLE"),
                new JenaBasedFormatWriter("N3"),
                new JenaBasedFormatWriter("NT"));
    }

    @Bean
    public TemplateLibrary getTemplateLibrary() {
        return new RecordApiTemplateLibrary(
                new CodecRegistry(), new Namespaces()
              , new DefaultUriNormalizer(), mediaTypes);
    }
    */

    public MediaTypes getMediaTypes() {
        return mediaTypes;
    }
}
