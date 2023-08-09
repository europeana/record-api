package eu.europeana.api.record.config;

import com.mongodb.Block;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ConnectionPoolSettings;
import java.util.concurrent.TimeUnit;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
import eu.europeana.api.record.codec.LiteralCodec;
import eu.europeana.api.record.codec.LiteralMapCodec;
import eu.europeana.api.record.vocabulary.AppConfigConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

@Configuration
@PropertySource(
        value = {"classpath:record-api.properties", "classpath:record-api.user.properties"},
        ignoreResourceNotFound = true)
public class DataSourceConfig {

    private static final Logger LOGGER = LogManager.getLogger(DataSourceConfig.class);

    @Value("${mongo.connectionUrl}")
    private String hostUri;

    @Value("${mongo.max.idle.time.millisec: 10000}")
    private long mongoMaxIdleTimeMillisec;

    @Value("${mongo.record.database}")
    private String recordDatabase;


    @Bean
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(hostUri);

        CodecRegistry myRegistry = fromRegistries(
                CodecRegistries.fromCodecs(new LiteralMapCodec(), new LiteralCodec()),
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(
                        PojoCodecProvider.builder()
                                .automatic(true)
                                .build()
                )
        );

        Block<ConnectionPoolSettings.Builder> connectionPoolSettingsBlockBuilder =
                (ConnectionPoolSettings.Builder builder) ->
                        builder.maxConnectionIdleTime(mongoMaxIdleTimeMillisec, TimeUnit.MILLISECONDS);

        return MongoClients.create(
                MongoClientSettings.builder()
                        .applyConnectionString(connectionString)
                        .codecRegistry(myRegistry)
                        .applyToConnectionPoolSettings(connectionPoolSettingsBlockBuilder)
                        .build());
    }

    @Primary
    @Bean(name = AppConfigConstants.BEAN_RECORD_DATA_STORE)
    public Datastore emDataStore(MongoClient mongoClient) {
        LOGGER.info("Configuring Record API database: {}", recordDatabase);
        Datastore datastore = Morphia.createDatastore(mongoClient, recordDatabase, MapperOptions.builder().mapSubPackages(true).build());

        // EA-2520: explicit package mapping required to prevent EntityDecoder error
        // TODO switch the package to interfaces later , once ready
        datastore.getMapper().mapPackage("eu.europeana.api.record.impl");
        datastore.ensureIndexes();
        return datastore;
    }

}
