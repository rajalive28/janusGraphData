package janus.graph.data.client.service;

import janus.graph.data.model.GraphQueryResponse;

import java.util.Map;

/**
 * A service that can talk to any embedded or remote Gremlin Server. The language used to communicate is Groovy. Each
 * gremlin is sent to the configured gremlin server and a response is obtained which in-turn is returned to the caller.
 * The gremlin server abstracts any arbitrary type of graph database that implements the Apache TinkerPop specification.
 * It does not matter, what underlying graph database is used.
 *
 * @author Abhishek Raj
 * @author Arun Patra
 */
public interface GraphAccessService {

    /**
     * Submits the supplied gremlin query to the configured gremlin server, de-serializes the response and returns to
     * the caller.
     *
     * @param gremlin The gremlin query in Groovy language.
     * @return A GraphQueryResponse instance.
     * @throws GraphDataAccessException If an exception occurs.
     */
    GraphQueryResponse<Object> submit(String gremlin) throws GraphDataAccessException;

    /**
     * Submits the supplied gremlin query to the configured gremlin server, de-serializes the response and returns to
     * the caller.
     *
     * @param gremlin    The gremlin query in Groovy language.
     * @param parameters The parameters to bind, if no parameters are required supply an empty {@link Map}.
     * @return A GraphQueryResponse instance.
     * @throws GraphDataAccessException If an exception occurs.
     */
    GraphQueryResponse<Object> submit(String gremlin, Map<String, Object> parameters) throws GraphDataAccessException;

    /**
     * Submits the supplied gremlin query to the configured gremlin server, de-serializes the response and returns to
     * the caller.
     *
     * @param gremlin      The gremlin query in Groovy language.
     * @param responseType The class of the required type of results.
     * @param <T>          The type to which the returned results are to be de-serialized into. See {@link GraphQueryResponse}.
     * @return A GraphQueryResponse instance.
     * @throws GraphDataAccessException
     */
    <T> GraphQueryResponse<T> submit(String gremlin, Class<T> responseType) throws GraphDataAccessException;

    /**
     * Submits the supplied gremlin query to the configured gremlin server, de-serializes the response and returns to
     * the caller.
     *
     * @param gremlin      The gremlin query in Groovy language.
     * @param parameters   The parameters to bind, if no parameters are required supply an empty {@link Map}.
     * @param responseType The class of the required type of results.
     * @param <T>          The type to which the returned results are to be de-serialized into. See {@link GraphQueryResponse}.
     * @return A GraphQueryResponse instance.
     * @throws GraphDataAccessException
     */
    <T> GraphQueryResponse<T> submit(String gremlin, Map<String, Object> parameters, Class<T> responseType) throws GraphDataAccessException;
}
