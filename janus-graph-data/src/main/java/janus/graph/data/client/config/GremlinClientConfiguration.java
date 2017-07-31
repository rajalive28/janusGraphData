package janus.graph.data.client.config;

import janus.graph.data.client.ClientFactory;
import janus.graph.data.client.GremlinClient;
import janus.graph.data.client.impl.ClientFactoryImpl;
import janus.graph.data.client.service.GraphAccessService;
import janus.graph.data.client.service.GraphDataAccessException;
import janus.graph.data.client.service.impl.GraphAccessServiceImpl;
import janus.graph.data.client.transaction.GremlinClientTransactionConfigurator;
import janus.graph.data.client.transaction.GremlinTransactionManager;
import janus.graph.data.client.transaction.GremlinTransactionManagerImpl;
import janus.graph.data.model.DebugRequest;
import janus.graph.data.server.EmbeddedGremlinServerConfiguration;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.driver.MessageSerializer;
import org.apache.tinkerpop.gremlin.driver.ser.GraphSONMessageSerializerV1d0;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONMapper;
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PreDestroy;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_HTML_VALUE;

/**
 * Configures a {@link GraphAccessService} bean that can be used to communicate with a remote gremlin server. It
 * sets up in internal {@link GremlinClient} also, but its direct use by callers is discouraged. All interactions with
 * a remote or embedded gremlin server can and should be accomplished using the {@link GraphAccessService}. By
 * default, the {@link GraphAccessService} will submit gremlins to
 * {@link GremlinClientProperties#DEFAULT_REMOTE_GREMLIN_SERVER_ENDPOINT} which can be overridden by supplying the
 * graph.data.access.gremlin.client.gremlin-server-endpoint property.
 *
 * @author Abhishek Raj
 * @author Arun Patra
 */
@Configuration
@EnableConfigurationProperties(GremlinClientProperties.class)
@AutoConfigureAfter({EmbeddedGremlinServerConfiguration.class})
public class GremlinClientConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(GremlinClientConfiguration.class);

    private final GremlinClientProperties properties;

    private Cluster cluster;

    @Autowired
    public GremlinClientConfiguration(GremlinClientProperties properties) {
        this.properties = properties;
    }

    @Bean("gremlinServerCluster")
    public Cluster gremlinCluster() {
        try {
            cluster = Cluster.build().addContactPoint(properties.getHost())
                    .port(properties.getPort())
                    .maxWaitForConnection(5000)
                    .serializer(getGraphsonMessageSerializer())
                    .create();
            LOGGER.info("Successfully configured Gremlin cluster comprising server(s) at {}:{}", properties.getHost(), properties.getPort());
        } catch (Throwable t) {
            LOGGER.error("Could not configure Gremlin cluster.");
            t.printStackTrace();
            throw new RuntimeException(t);
        }
        return cluster;
    }

    @Bean
    public ClientFactory clientFactory(Cluster gremlinCluster) {
        return new ClientFactoryImpl(gremlinCluster, properties);
    }

    @Bean
    public GremlinClientTransactionConfigurator gremlinClientTransactionConfigurator(GremlinTransactionManager gremlinTransactionManager) {
        return new GremlinClientTransactionConfigurator(gremlinTransactionManager);
    }

    @Bean
    public GremlinTransactionManager gremlinTransactionManager(GraphAccessService graphAccessService) {
        return new GremlinTransactionManagerImpl(graphAccessService);
    }

    @Bean("graphAccessService")
    public GraphAccessService restGraphAccessService(ClientFactory clientFactory) {
        return new GraphAccessServiceImpl(clientFactory);
    }

    private MessageSerializer getGraphsonMessageSerializer() {
        GraphSONMapper graphsonMapper = GraphSONMapper.build().addRegistry(JanusGraphIoRegistry.getInstance()).create();
        return new GraphSONMessageSerializerV1d0(graphsonMapper);
    }

    @PreDestroy
    private void shutDownHandler() {
        cluster.close();
    }

    /**
     * Configures a REST endpoint available at /debug that can take a {@link DebugRequest} in JSON format. This endpoint
     * is primarily meant for debugging purposes. Use this, when you want to see the actual response sent by the gremlin
     * server in JSON format.
     */
    @RestController
    public class DebugEndpointConfigurer {

        @Autowired
        private GraphAccessService graphAccessService;

        @ResponseBody
        @ResponseStatus(HttpStatus.OK)
        @RequestMapping(value = "/debug", method = RequestMethod.POST, produces = {TEXT_HTML_VALUE}, consumes = {APPLICATION_JSON_VALUE})
        public String executeGremlin(@RequestBody DebugRequest gremlinRequest) throws GraphDataAccessException {
            String resp = "FAIL";
            try {
                resp =
                        graphAccessService
                                .submit(gremlinRequest.getGremlin(), gremlinRequest.getBindings()).getRawData();
            } catch (GraphDataAccessException e) {
                LOGGER.error("Failed. Reason is : {}", e.getMessage());
            }
            return resp;
        }
    }
}
