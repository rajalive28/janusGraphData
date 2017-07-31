package janus.graph.data.server;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * Embedded Gremlin Server properties.
 *
 * @author Abhishek Raj
 * @author Arun Patra
 */
@Validated
@ConfigurationProperties("graph.data.access.gremlin.embedded.server")
public class EmbeddedGremlinServerProperties {
    private static final int DEFAULT_EMBEDDED_GREMLIN_SERVER_PORT = 8182;
    private static final String DEFAULT_EMBEDDED_GREMLIN_SERVER_HOST = "localhost";
    private static final String DEFAULT_EMBEDDED_GREMLIN_SERVER_COMM_PROTOCOL = "rest";

    private String host = DEFAULT_EMBEDDED_GREMLIN_SERVER_HOST;
    private int port = DEFAULT_EMBEDDED_GREMLIN_SERVER_PORT;
    private String protocol = DEFAULT_EMBEDDED_GREMLIN_SERVER_COMM_PROTOCOL;

    @NotEmpty
    @NotBlank
    @NotNull
    private String graphPath;

    @NotEmpty
    @NotBlank
    @NotNull
    private String scriptPath;

    public String getScriptPath() {
        return scriptPath;
    }

    public void setScriptPath(String scriptPath) {
        this.scriptPath = scriptPath;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getGraphPath() {
        return graphPath;
    }

    public void setGraphPath(String graphPath) {
        this.graphPath = graphPath;
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
}
