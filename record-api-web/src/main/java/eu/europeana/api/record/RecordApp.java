package eu.europeana.api.record;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main application. Allows deploying as a war and logs instance data when deployed in Cloud Foundry
 */
@SpringBootApplication(
        scanBasePackages = {"eu.europeana.api.record"},
        exclude = {
                // Remove these exclusions to re-enable security
                SecurityAutoConfiguration.class,
                ManagementWebSecurityAutoConfiguration.class,
                // DataSources are manually configured (for EM and batch DBs)
                DataSourceAutoConfiguration.class
        })
public class RecordApp {

        private static final Logger LOG = LogManager.getLogger(RecordApp.class);

        /**
         * Main entry point of this application
         *
         * @param args command-line arguments
         */
        public static void main(String[] args) {
                LOG.info("No args provided to application. Starting web server");
                SpringApplication.run(RecordApp.class, args);
        }


}
