package janus.graph.data.client.transaction;

import janus.graph.data.client.ClientFactory;
import janus.graph.data.client.GremlinClient;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.*;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.ResourceTransactionManager;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class GraphTransactionManager extends AbstractPlatformTransactionManager implements ResourceTransactionManager, BeanFactoryAware, InitializingBean {

    private ClientFactory clientFactory;

    public GraphTransactionManager() {
        setTransactionSynchronization(SYNCHRONIZATION_ON_ACTUAL_TRANSACTION);
    }

    public GraphTransactionManager(ClientFactory clientFactory) {
        this();
        this.clientFactory = clientFactory;
    }

    public ClientFactory getClientFactory() {
        return this.clientFactory;
    }

    public void setClientFactory(ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (getClientFactory() == null) {
            setClientFactory(beanFactory.getBean(ClientFactory.class));
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (getClientFactory() == null) {
            throw new IllegalArgumentException("'clientFactory' is required");
        }
    }

    @Override
    public Object getResourceFactory() {
        return getClientFactory();
    }

    @Override
    protected Object doGetTransaction() throws TransactionException {
        GraphTransactionObject txObject = new GraphTransactionObject();

        ClientHolder clientHolder = (ClientHolder) TransactionSynchronizationManager.getResource(getClientFactory());
        if (clientHolder != null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Found thread-bound Session [" + clientHolder.getClient() +
                        "] for Graph transaction");
            }
            txObject.setClientHolder(clientHolder, false);
        }
        return txObject;
    }

    @Override
    protected boolean isExistingTransaction(Object transaction) {
        return ((GraphTransactionObject) transaction).hasTransaction();
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) throws TransactionException {
        GraphTransactionObject txObject = (GraphTransactionObject) transaction;

        try {
            if (txObject.getClientHolder() == null ||
                    txObject.getClientHolder().isSynchronizedWithTransaction()) {
                GremlinClient client = clientFactory.getClient();
                if (logger.isDebugEnabled()) {
                    logger.debug("Opened new Client [" + client + "] for Graph transaction");
                }
                txObject.setClientHolder(new ClientHolder(client), true);
            }

            GremlinClient client = txObject.getClientHolder().getClient();

            if (definition.getIsolationLevel() != TransactionDefinition.ISOLATION_DEFAULT) {
                // We should set a specific isolation level but are not allowed to...
                throw new InvalidIsolationLevelException(
                        "GraphTransactionManager is not allowed to support custom isolation levels.");
            }

            if (definition.getPropagationBehavior() != TransactionDefinition.PROPAGATION_REQUIRED) {
                throw new IllegalTransactionStateException(
                        "GraphTransactionManager only supports 'required' propagation.");
            }

            Transaction transactionData;
            if (definition.isReadOnly() && txObject.isNewClientHolder()) {
                transactionData = client.beginTransaction(Transaction.Type.READ_ONLY);
            } else {
                transactionData = client.beginTransaction();
            }

            txObject.setTransactionData(transactionData);
            if (logger.isDebugEnabled()) {
                logger.debug("Beginning Transaction [" + transactionData + "] on Client [" + client + "]");
            }

            // Bind the session holder to the thread.
            if (txObject.isNewClientHolder()) {
                TransactionSynchronizationManager.bindResource(getClientFactory(), txObject.getClientHolder());
            }

            if (definition.isReadOnly()) {
                TransactionSynchronizationManager.setCurrentTransactionReadOnly(true);
            }

            txObject.getClientHolder().setSynchronizedWithTransaction(true);

        } catch (Throwable t) {
            closeSessionAfterFailedBegin(txObject);
            throw new CannotCreateTransactionException("Could not open Graph Client for transaction", t);
        }
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) throws TransactionException {
        GraphTransactionObject txObject = (GraphTransactionObject) status.getTransaction();
        GremlinClient client = txObject.getClientHolder().getClient();

        try (Transaction tx = client.getTransaction()) {

            if (status.isDebug()) {
                logger.debug("Committing Graph transaction [" + tx + "] on Client [" + client + "]");
            }

            tx.commit();
        } catch (RuntimeException ex) {
//            DataAccessException dae = SessionFactoryUtils.convertOgmAccessException(ex);
//            throw (dae != null ? dae : ex);
            throw new InvalidDataAccessApiUsageException("Failed to commit transaction.", ex);
        }
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) throws TransactionException {
        GraphTransactionObject txObject = (GraphTransactionObject) status.getTransaction();
        GremlinClient client = txObject.getClientHolder().getClient();
        try (Transaction tx = client.getTransaction()) {

            if (status.isDebug()) {
                logger.debug("Rolling back Graph transaction [" + tx + "] on Client [" + client + "]");
            }
            tx.rollback();
        } catch (RuntimeException ex) {
//            DataAccessException dae = SessionFactoryUtils.convertOgmAccessException(ex);
//            throw (dae != null ? dae : ex);
            throw new InvalidDataAccessApiUsageException("Failed to rollback transaction.", ex);
        } finally {
            if (!txObject.isNewClientHolder()) {
                // TODO: Look into this
                // Clear all pending inserts/updates/deletes in the Session.
                //session.clear();
            }
        }
    }

    private void closeSessionAfterFailedBegin(GraphTransactionObject txObject) {
        if (txObject.isNewClientHolder()) {
            GremlinClient client = txObject.getClientHolder().getClient();
            try {
                client.getTransaction().rollback();
            } catch (Throwable ex) {
                logger.debug("Could not rollback Client after failed transaction begin", ex);
            } finally {
                // close session.
                //SessionFactoryUtils.closeSession(session);
            }
            txObject.setClientHolder(null, false);
        }
    }

    private static class GraphTransactionObject {
        private ClientHolder clientHolder;
        private boolean newClientHolder;
        private Transaction transactionData;

        void setClientHolder(
                ClientHolder clientHolder, boolean newClientHolder) {
            this.clientHolder = clientHolder;
            this.newClientHolder = newClientHolder;
        }

        ClientHolder getClientHolder() {
            return this.clientHolder;
        }

        boolean hasTransaction() {
            return (this.clientHolder != null && this.clientHolder.isTransactionActive());
        }

        boolean isNewClientHolder() {
            return this.newClientHolder;
        }

        Transaction getTransactionData() {
            return this.transactionData;
        }

        void setTransactionData(Transaction rawTransaction) {
            this.transactionData = rawTransaction;
            this.clientHolder.setTransactionActive(true);
        }
    }
}
