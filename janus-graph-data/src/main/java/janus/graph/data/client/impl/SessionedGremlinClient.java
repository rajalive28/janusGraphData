package janus.graph.data.client.impl;

import janus.graph.data.client.GremlinClient;
import janus.graph.data.client.transaction.GraphTransactionImpl;
import janus.graph.data.client.transaction.Transaction;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A sessioned client that should be used when transactions are to be manually handled by the caller. This is for
 * more advanced use cases. For simple cases, where all your work is done in a single script, use the
 * {@link SessionLessGremlinClient}.
 */
public class SessionedGremlinClient extends AbstractWebSocketGremlinClient implements GremlinClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionedGremlinClient.class);

    public SessionedGremlinClient(Client client) {
        this.client = client;
    }

    @Override
    public Transaction getTransaction() {
        return null;
    }

    @Override
    public Transaction beginTransaction() {
        LOGGER.info("Dummy impl of beginTransaction()");
        return new GraphTransactionImpl(this.client);
    }

    @Override
    public Transaction beginTransaction(Transaction.Type type) {
        return null;
    }
}
