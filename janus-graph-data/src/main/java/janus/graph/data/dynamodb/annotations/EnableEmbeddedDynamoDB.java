package janus.graph.data.dynamodb.annotations;

import janus.graph.data.dynamodb.config.EmbeddedDynamoDBConfiguration;
import janus.graph.data.dynamodb.config.EmbeddedDynamoDBImportSelector;
import janus.graph.data.dynamodb.config.EmbeddedDynamoDBProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * Auto-configures an embedded DynamoDB server. See {@link EmbeddedDynamoDBConfiguration }.
 * The auto-configuration behaviour can be controlled using properties from the
 * {@link EmbeddedDynamoDBProperties}.
 *
 * @author Abhishek Raj
 * @author Arun Patra
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(EmbeddedDynamoDBImportSelector.class)
public @interface EnableEmbeddedDynamoDB {
}
