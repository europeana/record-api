package eu.europeana.api.record.db.config;

import com.mongodb.Block;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ConnectionPoolSettings;
import java.util.concurrent.TimeUnit;
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.Mapper;
import eu.europeana.api.config.AppConfigConstants;
import eu.europeana.api.record.db.codec.*;
import eu.europeana.api.record.model.internal.LanguageMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

import static org.bson.codecs.configuration.CodecRegistries.*;

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

        // add codecs
        CodecRegistry myRegistry = fromRegistries(
                fromProviders(getDataValueCodecProvider()),
                fromCodecs(new EdmTypeCodec(), new DatatypeCodec()
                        , new LanguageMapCodecProvider<LanguageMap>()
                        , new LanguageMapArrayCodecProvider()),
                MongoClientSettings.getDefaultCodecRegistry()
        );


        // connection pool settings
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
        Datastore datastore=  createDatastore(mongoClient, recordDatabase, getDataValueCodecProvider());
        //datastore.ensureIndexes();
        return datastore;
    }

    @Bean
    public DataValueCodecProvider getDataValueCodecProvider() {
        return new DataValueCodecProvider();
    }

    private Datastore createDatastore(MongoClient mongoClient, String recordDatabase, DataValueCodecProvider provider) {
        Datastore datastore = Morphia.createDatastore(mongoClient, recordDatabase);
        provider.setDatastore(datastore);

        Mapper mapper = datastore.getMapper();
        mapper.mapPackage("eu.europeana.api.record.model");
        mapper.mapPackage("eu.europeana.api.record.model.media");
        mapper.mapPackage("eu.europeana.api.record.model.data");
        mapper.mapPackage("eu.europeana.api.record.model.internal");
        mapper.mapPackage("eu.europeana.api.record.model.entity");

        LOGGER.info("Datastore initialized");
        return datastore;
    }

}
