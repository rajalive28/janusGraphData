package janus.graph.data.client.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 * Properties to configure a gremlin client. By default, the client will try to connect lazily to
 * {@link GremlinClientProperties#DEFAULT_REMOTE_GREMLIN_SERVER_ENDPOINT}.
 */
@Validated
@ConfigurationProperties("graph.data.access.gremlin.client")
public class GremlinClientProperties {

    /**
     * Default endpoint to which this client will try to connect lazily.
     */
    private static final String DEFAULT_REMOTE_GREMLIN_SERVER_ENDPOINT = "http://localhost:8182";
    private static final String DEFAULT_REMOTE_GREMLIN_SERVER_CHANNEL = "http";
    private static final String DEFAULT_REMOTE_GREMLIN_SERVER_HOSTNAME = "localhost";
    private static final int DEFAULT_REMOTE_GREMLIN_SERVER_PORT = 8182;
    private static final String DEFAULT_REMOTE_GREMLIN_SERVER_PROTOCOL = "rest";
    private static final Boolean DEFAULT_TRANSACTION_ENABLED = false;

    private String gremlinServerEndpoint = DEFAULT_REMOTE_GREMLIN_SERVER_ENDPOINT;
    private String host = DEFAULT_REMOTE_GREMLIN_SERVER_HOSTNAME;
    private int port = DEFAULT_REMOTE_GREMLIN_SERVER_PORT;
    private String protocol = DEFAULT_REMOTE_GREMLIN_SERVER_PROTOCOL;
    private Boolean transactionEnabled = DEFAULT_TRANSACTION_ENABLED;
    private String channel = DEFAULT_REMOTE_GREMLIN_SERVER_CHANNEL;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Boolean getTransactionEnabled() {
        return transactionEnabled;
    }

    public void setTransactionEnabled(Boolean transactionEnabled) {
        this.transactionEnabled = transactionEnabled;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
