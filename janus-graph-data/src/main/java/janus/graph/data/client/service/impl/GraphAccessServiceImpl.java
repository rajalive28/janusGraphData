package janus.graph.data.client.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import janus.graph.data.client.ClientFactory;
import janus.graph.data.client.GremlinClient;
import janus.graph.data.client.service.GraphAccessService;
import janus.graph.data.client.service.GraphDataAccessException;
import janus.graph.data.model.GraphQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements the {@link GraphAccessService} interface. It uses an underlying {@link GremlinClient} to communicate
 * with a backend gremlin server.
 *
 * @author Abhishek Raj
 * @author Arun Patra
 */
public class GraphAccessServiceImpl implements GraphAccessService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphAccessServiceImpl.class);
    private ClientFactory clientFactory;

    public GraphAccessServiceImpl(ClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    @Override
    public GraphQueryResponse<Object> submit(String gremlin) throws GraphDataAccessException {
        return submit(gremlin, new HashMap<>());
    }

    @Override
    public GraphQueryResponse<Object> submit(String gremlin, Map<String, Object> parameters) throws GraphDataAccessException {
        return submit(gremlin, parameters, null);
    }

    @Override
    public <T> GraphQueryResponse<T> submit(String gremlin, Class<T> responseType) throws GraphDataAccessException {
        return submit(gremlin, new HashMap<>(), responseType);
    }

    @Override
    public <T> GraphQueryResponse<T> submit(String gremlin, Map<String, Object> parameters, Class<T> responseType) throws GraphDataAccessException {
        validateGremlin(gremlin);
        String response;
        try {
            response = clientFactory.getClient().submit(gremlin, parameters);
        } catch (Exception e) {
            throw new GraphDataAccessException("Failed to execute gremlin script.", e);
        }
        try {
            Gson gson = (new GsonBuilder()).setLenient().create();
            GraphQueryResponse actualResponse = new GraphQueryResponse();
            actualResponse.setRawData(response);
            if (null == responseType) {
                return actualResponse;
            }
            Type type = getType(GraphQueryResponse.class, responseType);
            GraphQueryResponse<T> typedResponse = gson.fromJson("{ \"data\": " + response + "}", type);
            typedResponse.setRawData(response);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Parsed response is \n {}", gson.toJson(actualResponse, type));
            }
            return typedResponse;
        } catch (Exception e) {
            throw new GraphDataAccessException(e.getMessage());
        }
    }

    private void validateGremlin(String gremlin) throws GraphDataAccessException {
        if (null == gremlin || gremlin.length() == 0)
            throw new GraphDataAccessException("Invalid request. The gremlin script is null or empty.");
    }

    private Type getType(final Class<?> rawClass, final Class<?> parameterClass) {
        return new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{parameterClass};
            }

            @Override
            public Type getRawType() {
                return rawClass;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };
    }
}
