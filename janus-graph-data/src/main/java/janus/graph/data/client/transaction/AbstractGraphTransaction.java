package janus.graph.data.client.transaction;

import org.apache.tinkerpop.gremlin.driver.Client;

public class AbstractGraphTransaction {

    protected Client client;

    protected Transaction.Status status = Transaction.Status.OPEN;
    protected Transaction.Type type = Transaction.Type.READ_WRITE;


}
