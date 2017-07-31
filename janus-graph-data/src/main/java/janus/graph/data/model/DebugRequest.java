package janus.graph.data.model;

import java.util.HashMap;
import java.util.Map;

/**
 * A gremlin request object that wraps a gremlin query and the parameter bindings. If no bindings are necessary for a
 * query, then an empty {@link Map} should be used.
 *
 * @author Abhishek Raj
 * @author Arun Patra
 */
public class DebugRequest {

    private String gremlin;
    private Map<String, Object> bindings = new HashMap<>();

    public String getGremlin() {
        return gremlin;
    }

    public void setGremlin(String gremlin) {
        this.gremlin = gremlin;
    }

    public Map<String, Object> getBindings() {
        return bindings;
    }

    public void setBindings(Map<String, Object> bindings) {
        this.bindings = bindings;
    }
}
