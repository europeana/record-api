package eu.europeana.api.config;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import eu.europeana.api.model.MediaType;
import eu.europeana.api.model.MediaTypes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author srishti singh
 * @since 18 October 2023
 */
@Configuration
public class MediaTypeConfig {

    private static final Logger LOG = LogManager.getLogger(MediaTypeConfig.class);

    @Resource(name = "msXmlMapper")
    private XmlMapper xmlMapper;


    @Bean(name = "msMediaTypes")
    public MediaTypes getMediaTypes() throws IOException {

        MediaTypes mediaTypes;
        try (InputStream inputStream = getClass().getResourceAsStream("/mediacategories.xml")) {
            assert inputStream != null;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String contents = reader.lines().collect(Collectors.joining(System.lineSeparator()));
                mediaTypes = xmlMapper.readValue(contents, MediaTypes.class);
            }
        }

        if (!mediaTypes.mediaTypeCategories.isEmpty()) {
            mediaTypes.getMap().putAll(mediaTypes.mediaTypeCategories.stream().filter(media -> !media.isEuScreen()).collect(Collectors.toMap(MediaType::getMimeType, e-> e)));
        } else {
            LOG.error("media Categories not configured at startup. mediacategories.xml file not added or is empty");
        }
        return mediaTypes;
    }
}
