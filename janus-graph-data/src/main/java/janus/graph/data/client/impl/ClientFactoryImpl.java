package janus.graph.data.client.impl;

import janus.graph.data.client.ClientFactory;
import janus.graph.data.client.GremlinClient;
import janus.graph.data.client.config.GremlinClientProperties;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;

/**
 * A factory that can return suitable clients whenever required.
 */
public class ClientFactoryImpl implements ClientFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientFactoryImpl.class);
    private final Cluster gremlinCluster;
    private final GremlinClientProperties properties;
    private SessionLessGremlinClient sessionLessGremlinClient;
    private SessionedGremlinClient sessionedGremlinClient;
    private RestGremlinClient restGremlinClient;
    @Autowired
    private ApplicationContext applicationContext;

    public ClientFactoryImpl(Cluster gremlinCluster, GremlinClientProperties properties) {
        this.properties = properties;
        this.gremlinCluster = gremlinCluster;
    }

    public GremlinClient getClient() {
        boolean transactionEnabled = applicationContext.getEnvironment()
                .getProperty("graph.data.access.gremlin.client.transaction-enabled", Boolean.class, false);

        String protocol = applicationContext.getEnvironment().getProperty("graph.data.access.gremlin.client.protocol", "webSocket");
        switch (protocol) {
            case "rest":
                return restGremlinClient;
            default:
                if (transactionEnabled) {
                    return sessionedGremlinClient;
                } else {
                    return sessionLessGremlinClient;
                }
        }
    }

    /**
     * Returns a session-less client.
     *
     * @return
     */
    private Client createSessionLessClient() {
        Client sessionLessClient = gremlinCluster.connect();
        LOGGER.info("Created new session-less client");
        return sessionLessClient;
    }

    /**
     * Returns a sessioned client with the specified session ID.
     *
     * @param sessionId
     * @return
     */
    private Client createSessionedClient(String sessionId) {
        Client sessionedClient = gremlinCluster.connect(sessionId);
        LOGGER.info("Created new sessioned client with sessionID {}", sessionId);
        return sessionedClient;
    }

    @PostConstruct
    private void init() {
        String protocol = applicationContext.getEnvironment().getProperty("graph.data.access.gremlin.client.protocol", "webSocket");
        if ("webSocket".equalsIgnoreCase(protocol)) {
            this.sessionLessGremlinClient = new SessionLessGremlinClient(createSessionLessClient());
            this.sessionedGremlinClient = properties.getTransactionEnabled() ?
                    new SessionedGremlinClient(createSessionedClient(UUID.randomUUID().toString())) : null;
        }
        this.restGremlinClient = new RestGremlinClient(properties);
    }

    @PreDestroy
    private void cleanup() {
        if (null != sessionLessGremlinClient) {
            sessionLessGremlinClient.client.close();
        }
        if (null != sessionedGremlinClient) {
            if (!sessionedGremlinClient.client.isClosing()) {
                sessionedGremlinClient.client.close();
            }
        }
    }
}
