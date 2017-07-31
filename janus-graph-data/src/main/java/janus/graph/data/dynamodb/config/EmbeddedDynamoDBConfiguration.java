package janus.graph.data.dynamodb.config;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import janus.graph.data.server.EmbeddedGremlinServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PreDestroy;

@Configuration
@EnableConfigurationProperties(EmbeddedDynamoDBProperties.class)
@AutoConfigureBefore({EmbeddedGremlinServerConfiguration.class})
public class EmbeddedDynamoDBConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedDynamoDBConfiguration.class);

    private final EmbeddedDynamoDBProperties properties;
    private DynamoDBProxyServer server = null;

    @Autowired
    public EmbeddedDynamoDBConfiguration(EmbeddedDynamoDBProperties properties) {
        this.properties = properties;
    }

    @Bean
    public DynamoDBProxyServer embeddedDynamoDBServer() {
        System.setProperty("sqlite4java.library.path", "target/native-libs");
        final String[] localArgs = {"-inMemory", "-port", Integer.toString(properties.getPort()), "-sharedDb"};
        try {
            //Starting DynamoDBProxy Server.
            server = ServerRunner.createServerFromCommandLineArgs(localArgs);
            server.start();
            LOGGER.info("Successfully started DynamoDB at {}:{}.", properties.getHost(), properties.getPort());
            return server;
        } catch (Exception e) {
            throw new RuntimeException("Failed to start embedded DynamoDB");
        }
    }

    @PreDestroy
    public void cleanup() {
        if (null == server) {
            LOGGER.warn("DynamoDB Server was never started, can't stop.");
        } else {
            try {
                server.stop();
                LOGGER.info("Successfully stopped DynamoDB.");
            } catch (Exception e) {
                LOGGER.warn("Failed to stop DynamoDB. Reason is: {}", e.getMessage());
            }
        }
    }
}
