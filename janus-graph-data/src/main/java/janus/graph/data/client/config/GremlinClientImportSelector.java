package janus.graph.data.client.config;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Import selector for {@link GremlinClientConfiguration}.
 */
public class GremlinClientImportSelector implements DeferredImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {

        return new String[]{GremlinClientConfiguration.class.getCanonicalName(),
                GraphTransactionConfiguration.class.getCanonicalName()};
    }
}
