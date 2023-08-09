package eu.europeana.api.record.config;

import eu.europeana.api.record.vocabulary.AppConfigConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Configuration
public class AppConfig extends AppConfigConstants {

    private static final Logger LOG = LogManager.getLogger(AppConfig.class);

    @Resource
    private RecordApiConfiguration recordApiConfiguration;

    public AppConfig() {
        LOG.info("Initializing RecordApiConfiguration bean as: configuration");
    }


    // TODO jwt authentication will be added later
    @PostConstruct
    public void init() {
        if (recordApiConfiguration.isAuthReadEnabled() || recordApiConfiguration.isAuthWriteEnabled()) {
            String jwtTokenSignatureKey = recordApiConfiguration.getApiKeyPublicKey();
            if (jwtTokenSignatureKey == null || jwtTokenSignatureKey.isBlank()) {
                throw new IllegalStateException("The jwt token signature key cannot be null or empty.");
            }
        }
    }

}
