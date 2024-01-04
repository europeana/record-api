package eu.europeana.api.config;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import eu.europeana.api.model.MediaType;
import eu.europeana.api.model.MediaTypes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.stream.Collectors;


/**
 * @author srishti singh
 * @since 18 October 2023
 *
 * To add this configuration in any other class use @Import and @Resource annotations
 * See : RecordApiTemplateLibrary.class in Record API
 */
@Configuration
public class MediaTypeConfig {

    private static final Logger LOG = LogManager.getLogger(MediaTypeConfig.class);

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXX");

    @Bean(name = AppConfigConstants.BEAN_MEDIA_TYPES)
    public MediaTypes getMediaTypes() throws IOException {

        MediaTypes mediaTypes;
        try (InputStream inputStream = getClass().getResourceAsStream("/mediacategories.xml")) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String contents = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                mediaTypes = xmlMapper().readValue(contents, MediaTypes.class);
            }
        }

        if (!mediaTypes.mediaTypeCategories.isEmpty()) {
            mediaTypes.getMap().putAll(mediaTypes.mediaTypeCategories.stream().filter(media -> !media.isEuScreen()).collect(Collectors.toMap(MediaType::getMimeType, e -> e)));
            LOG.info("Media Types configured at start up with mediacategories.xml file ..... ");
        } else {
            LOG.error("media Categories not configured at startup. mediacategories.xml file not added or is empty");
        }
        return mediaTypes;
    }

    @Bean("msXmlMapper")
    public XmlMapper xmlMapper() {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.setDateFormat(dateFormat);
        return xmlMapper;
    }
}
