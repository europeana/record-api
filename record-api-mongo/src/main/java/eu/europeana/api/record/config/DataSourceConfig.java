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
import dev.morphia.mapping.Mapper;
import eu.europeana.api.record.codec.DataValueCodec;
import eu.europeana.api.record.codec.LiteralCodec;
import eu.europeana.api.record.codec.ObjectReferenceCodec;
import eu.europeana.api.record.impl.*;
import eu.europeana.api.record.vocabulary.AppConfigConstants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.bson.codecs.StringCodec;
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
                CodecRegistries.fromCodecs(new LiteralCodec(), new ObjectReferenceCodec(), new DataValueCodec()),
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
        Datastore datastore=  createDatastore(mongoClient, recordDatabase);
        datastore.ensureIndexes();
        return datastore;
    }


    private Datastore createDatastore(MongoClient mongoClient, String databaseName) {
        Datastore morphiaDatastore = Morphia.createDatastore(mongoClient, databaseName);
        Mapper mapper = morphiaDatastore.getMapper();
        mapper.map(new Class[]{ProxyImpl.class});
        mapper.map(new Class[]{EuropeanaAggregationImpl.class});
        mapper.map(new Class[]{AggregationImpl.class});
        mapper.map(new Class[]{AgentImpl.class});
        mapper.map(new Class[]{WebResourceImpl.class});
        mapper.map(new Class[]{TechMetadataImpl.class});
        LOGGER.info("Datastore initialized");
        return morphiaDatastore;
    }
}
