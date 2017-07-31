package janus.graph.data.client.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import janus.graph.data.client.GremlinClient;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Result;
import org.apache.tinkerpop.gremlin.driver.ResultSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An abstract gremlin client that can talk over webSockets with a gremlin server. For this to work, the gremlin server
 * must have the WebSocket channellizer configured.
 */
public abstract class AbstractWebSocketGremlinClient implements GremlinClient {

    protected Client client;

    @Override
    public String submit(String gremlin, Map<String, Object> bindings) throws Exception {
        List<Object> objectList = new ArrayList<>();
        ResultSet resultSet = client.submit(gremlin, bindings);
        for (Result result : resultSet.all().get()) {
            objectList.add(result.getObject());
        }
        Gson gson = (new GsonBuilder()).setLenient().create();
        return gson.toJson(objectList);
    }

    @Override
    public String submit(String gremlin) throws Exception {
        return submit(gremlin, new HashMap<>());
    }
}
