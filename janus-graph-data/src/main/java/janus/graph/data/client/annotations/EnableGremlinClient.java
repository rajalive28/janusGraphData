package janus.graph.data.client.annotations;

import janus.graph.data.client.config.GremlinClientImportSelector;
import janus.graph.data.client.service.GraphAccessService;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Enables a gremlin server interaction mechanism. The {@link GraphAccessService} that
 * is configured, can be used by clients to submit gremlin scripts and obtain a response.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(GremlinClientImportSelector.class)
public @interface EnableGremlinClient {
}
