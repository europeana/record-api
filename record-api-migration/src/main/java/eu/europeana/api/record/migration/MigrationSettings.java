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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(
        value = {"classpath:migration.properties", "classpath:migration.user.properties"},
        ignoreResourceNotFound = true)
@Import({MediaTypeConfig.class, DataSourceConfig.class})
public class MigrationSettings {

    @Resource(name = AppConfigConstants.BEAN_MEDIA_TYPES)
    private MediaTypes mediaTypes;

    @Bean
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
        return  new RecordApiTemplateLibrary(new CodecRegistry(), new Namespaces(), new DefaultUriNormalizer());
    }

    public MediaTypes getMediaTypes() {
        return mediaTypes;
    }


}
