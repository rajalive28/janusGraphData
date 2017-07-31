package janus.graph.data.client.transaction;

import janus.graph.data.client.service.GraphDataAccessException;

/**
 * A manager class that can setup transaction behaviour.
 */
public interface GremlinTransactionManager {

    void setOnReadWriteBehaviour(GraphTransactionReadWriteBehaviour readWriteBehaviour) throws GraphDataAccessException;

    void setOnCloseBehaviour(GraphTransactionCloseBehaviour closeBehaviour) throws GraphDataAccessException;

    void commit() throws GraphDataAccessException;

    void rollback() throws GraphDataAccessException;

    enum GraphTransactionReadWriteBehaviour {
        AUTO, MANUAL
    }

    enum GraphTransactionCloseBehaviour {
        COMMIT, ROLLBACK, MANUAL
    }

}
