package by.klevitov.eventpersistor.healthcheck.config;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class MongoConfig {
    private static final String CONNECTION_STRING_FORMAT = "mongodb://%s:%s@%s:%d/%s?authSource=%s";
    @Value("${spring.data.mongodb.host}")
    private String host;
    @Value("${spring.data.mongodb.port}")
    private int port;
    @Value("${spring.data.mongodb.database}")
    private String database;
    @Value("${spring.data.mongodb.username}")
    private String username;
    @Value("${spring.data.mongodb.password}")
    private String password;
    @Value("${spring.data.mongodb.authentication-database}")
    private String authenticationDatabase;
    @Value("${spring.data.mongodb.connection-timeout}")
    private int connectionTimeout;
    @Value("${spring.data.mongodb.socket-timeout}")
    private int socketTimeout;
    @Value("${spring.data.mongodb.max-wait-time}")
    private int maxWaitTime;
    @Value("${spring.data.mongodb.server-selection-timeout}")
    private int serverSelectionTimeout;

    @Bean
    public MongoClient mongoClient() {
        final String connectionString = String.format(CONNECTION_STRING_FORMAT, username, password, host, port,
                database, authenticationDatabase);
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .applyToConnectionPoolSettings(builder ->
                        builder.maxWaitTime(maxWaitTime, TimeUnit.MILLISECONDS))
                .applyToSocketSettings(builder ->
                        builder.connectTimeout(connectionTimeout, TimeUnit.MILLISECONDS)
                                .readTimeout(socketTimeout, TimeUnit.MILLISECONDS))
                .applyToClusterSettings(builder ->
                        builder.serverSelectionTimeout(serverSelectionTimeout, TimeUnit.MILLISECONDS))
                .build();
        return MongoClients.create(settings);
    }

    //todo Review timeouts and update if necessary.
}
