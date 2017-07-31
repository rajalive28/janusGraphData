package janus.graph.data.server.annotations;

import janus.graph.data.server.EmbeddedGremlinServerConfiguration;
import janus.graph.data.server.EmbeddedGremlinServerImportSelector;
import janus.graph.data.server.EmbeddedGremlinServerProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Auto-configures an embedded Gremlin server. See {@link EmbeddedGremlinServerConfiguration}. The auto-configuration
 * behaviour can be controlled using properties from the {@link EmbeddedGremlinServerProperties}.
 *
 * @author Abhishek Raj
 * @author Arun Patra
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EmbeddedGremlinServerImportSelector.class)
public @interface EnableEmbeddedGremlinServer {

}
