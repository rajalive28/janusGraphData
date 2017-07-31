package janus.graph.data.dynamodb.config;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * Import selector for the {@link EmbeddedDynamoDBConfiguration} auto-configuration class.
 */
public class EmbeddedDynamoDBImportSelector implements DeferredImportSelector {

    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{EmbeddedDynamoDBConfiguration.class.getCanonicalName()};
    }

}
