package janus.graph.data.client.service;

/**
 * An exception to wrap all exceptions in this module.
 */
public class GraphDataAccessException extends Exception {

    public GraphDataAccessException() {
    }

    public GraphDataAccessException(String message) {
        super(message);
    }

    public GraphDataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public GraphDataAccessException(Throwable cause) {
        super(cause);
    }

    public GraphDataAccessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
