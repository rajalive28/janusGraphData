package janus.graph.data.client.transaction;

import janus.graph.data.client.service.GraphDataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 * On application startup, will set the transaction behaviour to Transaction.READ_WRITE_BEHAVIOR.AUTO on the
 * underlying graph database.
 */
public class GremlinClientTransactionConfigurator {

    private static final Logger LOGGER = LoggerFactory.getLogger(GremlinClientTransactionConfigurator.class);

    private GremlinTransactionManager gremlinTransactionManager;

    public GremlinClientTransactionConfigurator(GremlinTransactionManager gremlinTransactionManager) {
        this.gremlinTransactionManager = gremlinTransactionManager;
    }

    @EventListener
    private void init(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            gremlinTransactionManager.setOnReadWriteBehaviour(GremlinTransactionManager.GraphTransactionReadWriteBehaviour.AUTO);
            gremlinTransactionManager.setOnCloseBehaviour(GremlinTransactionManager.GraphTransactionCloseBehaviour.COMMIT);

        } catch (GraphDataAccessException gdae) {
            gdae.printStackTrace();
            throw new IllegalStateException("Failed to set transaction behavior.");
        }
    }
}
