package janus.graph.data.client.transaction;

public interface Transaction extends AutoCloseable {

    /**
     * rollback a transaction that has pending writes
     */
    void rollback();

    /**
     * commit a transaction that has pending writes
     */
    void commit();

    /**
     * return the status of the current transaction
     *
     * @return the Status value associated with the current transaction
     */
    Status status();

    /**
     * Obtains the read-only status of a transaction.
     * Transaction are read-write by default
     *
     * @return true if this is a read-only transaction, false otherwise
     */
    boolean isReadOnly();

    /**
     * close this transaction.
     */
    void close();

    enum Status {
        OPEN, PENDING, ROLLEDBACK, COMMITTED, CLOSED, ROLLBACK_PENDING, COMMIT_PENDING
    }

    enum Type {
        READ_ONLY, READ_WRITE
    }
}
