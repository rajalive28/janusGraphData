package janus.graph.data.server;

import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import janus.graph.data.client.config.GremlinClientConfiguration;
import janus.graph.data.dynamodb.annotations.EnableEmbeddedDynamoDB;
import org.apache.tinkerpop.gremlin.driver.ser.GraphSONMessageSerializerGremlinV1d0;
import org.apache.tinkerpop.gremlin.driver.ser.GraphSONMessageSerializerV1d0;
import org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV1d0;
import org.apache.tinkerpop.gremlin.server.GremlinServer;
import org.apache.tinkerpop.gremlin.server.Settings;
import org.apache.tinkerpop.gremlin.server.channel.HttpChannelizer;
import org.apache.tinkerpop.gremlin.server.channel.WebSocketChannelizer;
import org.apache.tinkerpop.gremlin.server.op.session.SessionOpProcessor;
import org.apache.tinkerpop.gremlin.server.op.traversal.TraversalOpProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Auto-configures an embedded gremlin server. Gryo, Graphson and Graphson-Gremlin serializers are enabled. The
 * {@link HttpChannelizer} is used which means that, gremlin clients can talk over HTTP with this server. The embedded
 * server will only be configured if the following two properties are available:
 * <p>
 * a) graph.data.access.gremlin.embedded.server.graph-path. This must resolve to a file path on the file system.
 * b) graph.data.access.gremlin.embedded.server.script-path. This must resolve to a file path on the file system.
 * <p>
 * The host and port properties default to locahost and 1882, and can be overriden using properties.
 * The above properties can be set in any way that Spring allows.
 *
 * @author Abhishek Raj
 * @author Arun Patra
 * @see EmbeddedGremlinServerProperties
 */
@Configuration
@EnableEmbeddedDynamoDB
@EnableConfigurationProperties(EmbeddedGremlinServerProperties.class)
@AutoConfigureBefore({GremlinClientConfiguration.class})
@ConditionalOnProperty(prefix = "graph.data.access.gremlin.embedded.server", name = {"graph-path", "script-path"})
public class EmbeddedGremlinServerConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmbeddedGremlinServerConfiguration.class);
    private final EmbeddedGremlinServerProperties properties;
    private GremlinServer server;

    @Autowired
    private DynamoDBProxyServer dynamoDBProxyServer;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    public EmbeddedGremlinServerConfiguration(EmbeddedGremlinServerProperties properties) {
        this.properties = properties;
    }

    /**
     * Configures an embedded {@link GremlinServer} instance.
     *
     * @param settings The settings to be used by the server
     * @return
     */
    @Bean("embeddedGremlinServer")
    public GremlinServer gremlinServer(Settings settings) {
        try {
            System.out.println(GremlinServer.getHeader());
            server = new GremlinServer(settings);
            server.start().join();
            LOGGER.info("Started embedded Gremlin server.");
            return server;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create embedded Gremlin Server. Reason is {}", e);
        }
    }

    @PreDestroy
    public void handleShutDown() {
        server.stop().join();
        LOGGER.info("Stopped embedded Gremlin server.");
    }

    @Bean
    public Settings getServerSettings() {
        Settings settings = createSettings();
        String protocol = applicationContext.getEnvironment().getProperty("graph.data.access.gremlin.embedded.server.protocol", "webSocket");
        switch (protocol) {
            case "rest":
                settings.channelizer = HttpChannelizer.class.getName();
                break;
            default:
                settings.channelizer = WebSocketChannelizer.class.getName();
                break;
        }
        return settings;
    }

    private Settings createSettings() {
        Settings settings = new Settings();
        settings.host = properties.getHost();
        settings.port = properties.getPort();
        settings.graphs.put("graph", properties.getGraphPath());
        settings.plugins.add("janusgraph.imports");
        settings.scriptEngines.get("gremlin-groovy").imports.add("java.lang.Math");
        settings.scriptEngines.get("gremlin-groovy").staticImports.add("java.lang.Math.PI");
        settings.scriptEngines.get("gremlin-groovy").scripts.add(properties.getScriptPath());

        // Add Processors
        Settings.ProcessorSettings pSettings1 = new Settings.ProcessorSettings();
        pSettings1.className = SessionOpProcessor.class.getName();
        pSettings1.config = new HashMap<>();
        pSettings1.config.put("sessionTimeout", 28800000);
        //pSettings1.config.put("force-index",true);

        Settings.ProcessorSettings pSettings2 = new Settings.ProcessorSettings();
        pSettings2.className = TraversalOpProcessor.class.getName();
        pSettings2.config = new HashMap<>();
        pSettings2.config.put("cacheExpirationTime", 600000);
        pSettings2.config.put("cacheMaxSize", 1000);
        //pSettings2.config.put("force-index",true);

        settings.processors.add(pSettings1);
        settings.processors.add(pSettings2);


        // Add serializers
        settings.serializers = new ArrayList<>();
        addSerializer(SerializerType.GRYO, settings.serializers);
        addSerializer(SerializerType.GRAPHSON, settings.serializers);
        addSerializer(SerializerType.GRAPHSON_GREMLIN, settings.serializers);

        // SSL
        settings.ssl = new Settings.SslSettings();
        settings.ssl.enabled = false;

        // Metrics
        settings.metrics = new Settings.ServerMetrics();
        settings.metrics.slf4jReporter = new Settings.Slf4jReporterMetrics();
        settings.metrics.slf4jReporter.enabled = true;
        settings.metrics.slf4jReporter.interval = 180000L;


//
//        new ModifiableConfiguration(GraphDatabaseConfiguration.ROOT_NS,new CommonsConfiguration(new BaseConfiguration()), BasicConfiguration.Restriction.LOCAL)
//                .set(GraphDatabaseConfiguration.FORCE_INDEX_USAGE,true);
//        GraphDatabaseConfiguration.buildGraphConfiguration()
//                .set(GraphDatabaseConfiguration.FORCE_INDEX_USAGE,true);

       // GraphDatabaseConfiguration.

//        if(new GraphDatabaseConfiguration().hasForceIndexUsage()){
//            LOGGER.debug("Forced Index is applied");
//        }


        final Constructor constructor = new Constructor(Settings.class);
        final Yaml yaml = new Yaml(constructor);
        yaml.dump(settings);
        LOGGER.info("Initializing Embedded Gremlin Server with following settings:\n" + yaml.dump(settings));

        return settings;
    }

    private void addSerializer(SerializerType type, List<Settings.SerializerSettings> serializers) {

        switch (type) {
            case GRYO: {
                Settings.SerializerSettings settings1 = new Settings.SerializerSettings();
                settings1.className = GryoMessageSerializerV1d0.class.getName();
                settings1.config = new HashMap<>();
                settings1.config.put("useMapperFromGraph", "graph");
                // add
                serializers.add(settings1);

                Settings.SerializerSettings settings2 = new Settings.SerializerSettings();
                settings2.className = GryoMessageSerializerV1d0.class.getName();
                settings2.config = new HashMap<>();
                settings2.config.put("serializeResultToString", true);
                // add
                serializers.add(settings2);

                break;
            }
            case GRAPHSON: {
                Settings.SerializerSettings settings1 = new Settings.SerializerSettings();
                settings1.className = GraphSONMessageSerializerV1d0.class.getName();
                settings1.config = new HashMap<>();
                settings1.config.put("useMapperFromGraph", "graph");
                // add
                serializers.add(settings1);
                break;
            }
            case GRAPHSON_GREMLIN: {
                Settings.SerializerSettings settings1 = new Settings.SerializerSettings();
                settings1.className = GraphSONMessageSerializerGremlinV1d0.class.getName();
                settings1.config = new HashMap<>();
                settings1.config.put("useMapperFromGraph", "graph");
                // add
                serializers.add(settings1);
                break;
            }
            default:
                break; // NO-OP
        }
    }

    private enum SerializerType {
        GRYO, GRAPHSON, GRAPHSON_GREMLIN
    }
}
