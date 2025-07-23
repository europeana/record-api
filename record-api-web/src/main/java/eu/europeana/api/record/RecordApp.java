package eu.europeana.api.record;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.jena.riot.RDFFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import dev.morphia.query.filters.Filters;
import dev.morphia.query.MorphiaCursor;
import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.format.RdfFormat;
import eu.europeana.api.record.profile.ViewProfileRegistry;
import eu.europeana.api.record.db.repository.OptimisedRecordRepositoryCopy;
import eu.europeana.api.record.db.repository.RecordRepository;
import eu.europeana.api.record.io.FormatHandlerRegistry;
import eu.europeana.api.record.io.FormatHandlerRegistryV2;
import eu.europeana.api.record.io.json.v2.JsonV2Writer;
import eu.europeana.api.record.model.ProvidedCHO;

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
public class RecordApp extends SpringBootServletInitializer {

        private static final Logger LOG = LogManager.getLogger(RecordApp.class);

        /**
         * Main entry point of this application
         *
         * @param args command-line arguments
         * @throws IOException 
         */
        public static void main(String[] args) throws IOException {
                LOG.info("No args provided to application. Starting web server");
                ConfigurableApplicationContext ctx = SpringApplication.run(RecordApp.class, args);


                RecordRepository repo = (RecordRepository)ctx.getBean(AppConfigConstants.BEAN_RECORD_REPO);

                ViewProfileRegistry vr = new ViewProfileRegistry();

                long time = System.currentTimeMillis();
                
                try ( MorphiaCursor<ProvidedCHO> cursor = repo.findByFilter(
                       Filters.eq("id", "http://data.europeana.eu/item/142/UEDIN_214")
                      // Filters.eq("proxies.proxyIn.datasetName", "9200515_NL_Photographs_Serbia")
                      //, vr.getProjection("media.full")
                       )) {

                    FormatHandlerRegistry reg = ctx.getBean(FormatHandlerRegistry.class);
    //                reg.get(RdfFormat.JSONLD).write(cho, System.out);
    
                    File dir = new File("C:\\Work\\incoming\\Record v3\\test");
                    RdfFormat[] formats = { RdfFormat.XML };
                    for ( RdfFormat format : formats ) {
                        File file = new File(dir, "records." +  format.getExtension());
                        try ( FileOutputStream fos = new FileOutputStream(file) ) {
                            reg.get(format).write(cursor, Math.min(cursor.available(), 10), fos);
                            fos.flush();
                        }
                    }
                    //new JsonV2Writer().write(cho, System.out);
                    //14719
                }

                long elapsed = System.currentTimeMillis() - time;
                System.out.println("Time: " + elapsed);

        }


}
