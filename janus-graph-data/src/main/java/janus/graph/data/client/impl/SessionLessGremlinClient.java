package janus.graph.data.client.impl;

import janus.graph.data.client.GremlinClient;
import janus.graph.data.client.transaction.Transaction;
import org.apache.tinkerpop.gremlin.driver.Client;

/**
 * A session-less client that is is suitable for most case. But when you need greater transaction control and are
 * willing to open and close transactions yourself, you would need to use {@link SessionedGremlinClient}. If all
 * your work can be done in a single gremlin script, this should suffice.
 */
public class SessionLessGremlinClient extends AbstractWebSocketGremlinClient implements GremlinClient {

    public SessionLessGremlinClient(Client client) {
        this.client = client;
    }

    @Override
    public Transaction getTransaction() {
        throw new UnsupportedOperationException("SessionLessGremlinClient does not support transactions.");
    }

    @Override
    public Transaction beginTransaction() {
        throw new UnsupportedOperationException("SessionLessGremlinClient does not support transactions.");
    }

    @Override
    public Transaction beginTransaction(Transaction.Type type) {
        throw new UnsupportedOperationException("SessionLessGremlinClient does not support transactions.");
    }
}
