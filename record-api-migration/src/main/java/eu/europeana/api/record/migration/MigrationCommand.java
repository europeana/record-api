/**
 * 
 */
package eu.europeana.api.record.migration;

import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Option.Builder;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import com.mongodb.Block;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ConnectionPoolSettings;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import static eu.europeana.api.record.migration.MigrationConstants.*;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * @author Hugo
 * @since 6 Jun 2024
 */
@SpringBootApplication(
        scanBasePackages = {"eu.europeana.api.record.migration"},
        exclude = {
                // Remove these exclusions to re-enable security
                SecurityAutoConfiguration.class,
                // DataSources are manually configured (for EM and batch DBs)
                DataSourceAutoConfiguration.class
        })
public class MigrationCommand implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(MigrationCommand.class);
        application.setWebApplicationType(WebApplicationType.NONE);
        //application.setDefaultProperties(null);
        application.run(args);
        //ConfigurableApplicationContext context = SpringApplication.run(MigrationCommand.class, args);
        //System.exit(SpringApplication.exit(context));
    }

    private PrintStream   ps = System.out;;
    private HelpFormatter formatter = new HelpFormatter();

    @Autowired
    private MigrationConfig  config;

    @Autowired
    private DataSourceConfig dsConfig;

    @Autowired
    private RunMigration    migration;

    @Override
    public void run(String... args) throws Exception {
        MigrationConfig config = process(args);
        if ( config == null ) { return; }

        migration.run(config);
    }

    protected MigrationConfig process(String[] args) {
        //Build Options
        Options opts = buildOptions();

        //Create parser
        CommandLineParser parser = DefaultParser.builder().build();
        try {
            // parse the command line arguments
            CommandLine line = parser.parse(opts, args);
            if(line.hasOption(help)) { printUsage(opts); return null; }

            printHeader();
            try                { return process(line); }
            catch(Throwable t) { printError(t); }
            printFooter();
        }
        catch(ParseException exp) { printUsage(opts, exp.getMessage()); }
        catch(Throwable      t)   { printError(t);                      }

        return null;
    }

    protected MigrationConfig process(CommandLine line) throws ParseException {
        config.setSource(getOptionValue(line, source, String.class));
        config.setLoggingDir(getOptionValue(line, logDir, String.class));
        config.setValidate(getOptionValue(line, validate, Boolean.class));
        config.setValidateDB(getOptionValue(line, validateDB, Boolean.class));
        config.setSaveCopy(getOptionValue(line, saveCopy, Boolean.class));
        config.setGenerateExternal(getOptionValue(line, external, Boolean.class));
        config.setThreads(getOptionValue(line, threads, Integer.class));
        return config;
    }

    protected <T> T getOptionValue(CommandLine line, String option, Class<T> c) 
            throws ParseException {
        return ( line.hasOption(option) ? (T)line.getParsedOptionValue(option) : null );
    }

    protected Options buildOptions()
    {
        Builder builder = Option.builder();
        Options options = new Options();
        options.addOption(builder.argName(source).hasArg().option(source)
                                  .required().type(String.class).build());
        options.addOption(builder.argName(logDir).hasArg().option(logDir)
                                  .required(false).type(String.class).build());
        options.addOption(builder.argName(validate).hasArg().option(validate)
                                 .required(false).type(Boolean.class).build());
        options.addOption(builder.argName(validateDB).hasArg().option(validateDB)
                                 .required(false).type(Boolean.class).build());
        options.addOption(builder.argName(saveCopy).hasArg().option(saveCopy)
                                 .required(false).type(Boolean.class).build());
        options.addOption(builder.argName(external).hasArg().option(external)
                                 .required(false).type(Boolean.class).build());
        options.addOption(builder.argName(threads).hasArg().option(threads)
                                 .required(false).type(Integer.class).build());
        
        return options;
    }

    protected void printUsage(Options opts)
    {
        formatter.printHelp(appName, opts, true);
    }

    protected void printUsage(Options opts, String msg)
    {
        printHeader();
        ps.println(msg);
        ps.println();
        printUsage(opts);
    }

    protected void printHeader() { }
    protected void printFooter() { }

    protected void printError(Throwable t)
    {
        ps.println("Error: " + t.getMessage());
        ps.println();
        t.printStackTrace(ps);
    }
}
