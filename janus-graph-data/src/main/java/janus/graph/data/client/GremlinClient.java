package janus.graph.data.client;

import janus.graph.data.client.transaction.Transactions;

import java.util.Map;

/**
 * A client that can submit groovy scripts to a gremlin server.
 *
 * @author Abhishek Raj
 * @author Arun Patra
 */
public interface GremlinClient extends Transactions {

    /**
     * Submits a groovy script, commonly known as a gremlin to a gremlin server.
     *
     * @param gremlin  The gremlin. Must be a valid groovy script, e.g. "g.V()".
     * @param bindings A map of objects that contain parameter names and values.
     * @return The result of the gremlin execution at the gremlin server.
     * @throws Exception if an exception is thrown during communication with the gremlin server.
     */
    String submit(String gremlin, Map<String, Object> bindings) throws Exception;

    /**
     * Submits a groovy script, commonly known as a gremlin to a gremlin server.
     *
     * @param gremlin The gremlin. Must be a valid groovy script, e.g. "g.V()".
     * @return The result of the gremlin execution at the gremlin server.
     * @throws Exception if an exception is thrown during communication with the gremlin server.
     */
    String submit(String gremlin) throws Exception;

}
