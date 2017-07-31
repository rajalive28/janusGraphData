package janus.graph.data.client.config;

import janus.graph.data.client.ClientFactory;
import janus.graph.data.client.transaction.GraphTransactionManager;
import janus.graph.data.server.EmbeddedGremlinServerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@AutoConfigureAfter({EmbeddedGremlinServerConfiguration.class})
@EnableConfigurationProperties(GremlinClientProperties.class)
@ConditionalOnProperty(prefix = "graph.data.access.gremlin.client", name = "transaction-enabled")
public class GraphTransactionConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphTransactionConfiguration.class);

    private final GremlinClientProperties properties;

    @Autowired
    public GraphTransactionConfiguration(GremlinClientProperties properties) {
        this.properties = properties;
    }


    @Bean(name = "graphTransactionManager")
    public GraphTransactionManager transactionManager(ClientFactory clientFactory) {
        LOGGER.info(">>> Configuring graphTransactionManager...");
        return new GraphTransactionManager(clientFactory);
    }

}
