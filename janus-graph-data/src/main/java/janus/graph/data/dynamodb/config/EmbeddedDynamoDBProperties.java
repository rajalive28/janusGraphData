package janus.graph.data.dynamodb.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("embedded.dynamodb")
public class EmbeddedDynamoDBProperties {
    private static final String DEFAULT_EMBEDDED_DYNAMO_DB_HOST = "localhost";
    private static final int DEFAULT_EMBEDDED_DYNAMO_DB_PORT = 14567;
    private String host = DEFAULT_EMBEDDED_DYNAMO_DB_HOST;
    private int port = DEFAULT_EMBEDDED_DYNAMO_DB_PORT;

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
}
