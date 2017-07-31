package janus.graph.data.client.transaction;

import org.apache.tinkerpop.gremlin.driver.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphTransactionImpl extends AbstractGraphTransaction implements Transaction {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphTransactionImpl.class);


    public GraphTransactionImpl(Client client) {
        this.client = client;
    }

    @Override
    public void rollback() {
        try {
            if (client.submit("graph.tx().isOpen()").all().get().get(0).getBoolean()) {
                client.submit("graph.tx().rollback()");
                LOGGER.info(">>> Transaction was rolled back.");
                status = Status.ROLLEDBACK;
            } else {
                String msg = ">>> You are asking to rollback the transaction bound to this session when there is no open tx bound to this session.";
                LOGGER.error(msg);
                throw new RuntimeException(msg);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void commit() {
        try {
            if (client.submit("graph.tx().isOpen()").all().get().get(0).getBoolean()) {
                client.submit("graph.tx().commit()");
                LOGGER.info(">>> Transaction was committed.");
                status = Status.COMMITTED;
            } else {
                String msg = ">>> You are asking to commit the transaction bound to this session when there is no open tx bound to this session.";
                throw new RuntimeException(msg);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to commit.", e);
        }
    }

    @Override
    public Status status() {
        try {
            boolean tranOpen = client.submit("graph.tx().isOpen()").all().get().get(0).getBoolean();
            if (tranOpen) {
                LOGGER.info(">>> Status: Transaction is open.");
                return Status.OPEN;
            } else {
                LOGGER.info(">>> Status: Transaction is not open.");
                return Status.CLOSED;
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to commit.", e);
        }
    }

    @Override
    public boolean isReadOnly() {
        return type == Type.READ_ONLY;
    }

    @Override
    public void close() {
        status = Status.CLOSED;
    }
}
