package eu.europeana.api.record.io;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JenaBasedFormatWriterConfig {

    @Bean("ttl")
    public JenaBasedFormatWriter ttlJenaWriter() {
        return new JenaBasedFormatWriter("TURTLE");
    }

    @Bean("N3")
    public JenaBasedFormatWriter N3JenaWriter() {
        return new JenaBasedFormatWriter("N3");
    }


    @Bean("NT")
    public JenaBasedFormatWriter NtJenaWriter() {
        return new JenaBasedFormatWriter("NT");
    }
}
