package janus.graph.data.server;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Import selector for the {@link EmbeddedGremlinServerConfiguration} auto-configuration class.
 */
public class EmbeddedGremlinServerImportSelector implements DeferredImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{EmbeddedGremlinServerConfiguration.class.getCanonicalName()};
    }
}
