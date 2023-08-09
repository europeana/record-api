package eu.europeana.api.record.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@PropertySources({
        @PropertySource("classpath:record-api.properties"),
        @PropertySource(
                value = "classpath:record-api.user.properties",
                ignoreResourceNotFound = true)
})
public class RecordApiConfiguration implements InitializingBean {

    private static final Logger LOG = LogManager.getLogger(RecordApiConfiguration.class);

    @Value("${europeana.apikey.jwttoken.signaturekey}")
    private String apiKeyPublicKey;

    @Value("${europeana.apikey.serviceurl}")
    private String apiKeyUrl;

    @Value("${auth.read.enabled: true}")
    private boolean authReadEnabled;

    @Value("${auth.write.enabled: true}")
    private boolean authWriteEnabled;

    public String getApiKeyUrl() {
        return apiKeyUrl;
    }

    public boolean isAuthReadEnabled() {
        return authReadEnabled;
    }

    public boolean isAuthWriteEnabled() {
        return authWriteEnabled;
    }

    public String getApiKeyPublicKey() {
        return apiKeyPublicKey;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // TODO add validation of required properties
    }
}
