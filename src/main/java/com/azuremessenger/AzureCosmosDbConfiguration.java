package com.azuremessenger;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.cosmos.ConsistencyLevel;
import com.azure.cosmos.CosmosClientBuilder;
import com.azure.spring.data.cosmos.config.AbstractCosmosConfiguration;
import com.azure.spring.data.cosmos.repository.config.EnableCosmosRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableCosmosRepositories(basePackages = "com.azuremessenger.database")
public class AzureCosmosDbConfiguration extends AbstractCosmosConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(AzureCosmosDbConfiguration.class);

    @Value("${azure.cosmosdb.uri}")
    private String uri = "https://ivan-synenko.documents.azure.com:443/";

    @Value("${azure.cosmosdb.key}")
    private String key = "XpF8Tyn5ZkCl5Qif8AYK1xuwQvGAma8fhsuLvDuqS31N3Jn7lDVVZbVw2m9IifCN9C6PiaKaZjOEACDbavMLTw==";

    @Value("${azure.cosmosdb.database}")
    private String dbName = "messenger";

    private AzureKeyCredential cosmosKeyCredential;

    public AzureCosmosDbConfiguration() {
        logger.info("Creating Azure Cosmos DB configuration");
        logger.info("URI: " + uri);
        logger.info("Key: " + key);
        logger.info("Database Name: " + dbName);
    }

    @Override
    protected String getDatabaseName() {
        return "messenger";
    }

    @Bean
    public CosmosClientBuilder cosmosClientBuilder() {
        return new CosmosClientBuilder()
                .endpoint(uri)
                .key(key)
                .consistencyLevel(ConsistencyLevel.EVENTUAL);
    }
}