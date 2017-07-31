package janus.graph.data.client;

/**
 * A factory to obtain {@link GremlinClient} instances.
 */
public interface ClientFactory {

    /**
     * Get a {@link GremlinClient} instance. Clients are returned depending on the situation and how the system is
     * configured.
     *
     * @return
     */
    GremlinClient getClient();

}
