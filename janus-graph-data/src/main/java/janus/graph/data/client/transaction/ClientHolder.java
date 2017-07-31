package janus.graph.data.client.transaction;

import janus.graph.data.client.GremlinClient;
import org.springframework.transaction.support.ResourceHolderSupport;
import org.springframework.util.Assert;

public class ClientHolder extends ResourceHolderSupport {
    private final GremlinClient client;
    private boolean transactionActive;

    public ClientHolder(GremlinClient client) {
        Assert.notNull(client, "GremlinClient must not be null");
        this.client = client;
    }

    public GremlinClient getClient() {
        return this.client;
    }

    protected boolean isTransactionActive() {
        return this.transactionActive;
    }

    protected void setTransactionActive(boolean transactionActive) {
        this.transactionActive = transactionActive;
    }

    @Override
    public void clear() {
        super.clear();
        this.transactionActive = false;
    }

}
