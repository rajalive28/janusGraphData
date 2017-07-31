package janus.graph.data.client.transaction;

public interface Transactions {

    /**
     * Get the existing transaction if available
     *
     * @return an active Transaction, or null if none exists
     */
    Transaction getTransaction();

    /**
     * Begin a new READ_WRITE transaction. If an existing transaction already exists, users must
     * decide whether to commit or rollback. Only one transaction can be bound to a thread
     * at any time, so active transactions that have not been closed but are no longer bound
     * to the thread must be handled by client code.
     *
     * @return a new active Transaction
     */
    Transaction beginTransaction();

    /**
     * Begin a new transaction, passing in the required type (READ_ONLY, READ_WRITE).
     * If an existing transaction already exists, users must
     * decide whether to commit or rollback. Only one transaction can be bound to a thread
     * at any time, so active transactions that have not been closed but are no longer bound
     * to the thread must be handled by client code.
     *
     * @param type the {@link Transaction.Type} required for this transaction
     * @return a new active Transaction
     */
    Transaction beginTransaction(Transaction.Type type);
}
