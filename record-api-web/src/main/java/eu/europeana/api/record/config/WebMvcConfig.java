package eu.europeana.api.record.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 *Setup CORS for all requests and setup default Content-type
 * @author Sristhti Singh
 * @since 23 October 2023
 */
@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "OPTIONS")
                .exposedHeaders(HttpHeaders.ALLOW, HttpHeaders.VARY, HttpHeaders.LINK, HttpHeaders.ETAG)
                .allowCredentials(false)
                .maxAge(3600);
    }
}
