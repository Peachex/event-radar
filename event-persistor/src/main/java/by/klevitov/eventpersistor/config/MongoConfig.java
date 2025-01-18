package by.klevitov.eventpersistor.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableTransactionManagement
public class MongoConfig {
    @Value("${spring.data.mongodb.connectionStr}")
    private String connectionStr;
    @Value("${spring.data.mongodb.username}")
    private String username;
    @Value("${spring.data.mongodb.password}")
    private String password;
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
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionStr))
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

    @Bean
    public MongoTransactionManager transactionManager(MongoDatabaseFactory databaseFactory) {
        return new MongoTransactionManager(databaseFactory);
    }
}
