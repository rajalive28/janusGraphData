package janus.graph.data.client.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import janus.graph.data.client.GremlinClient;
import janus.graph.data.client.config.GremlinClientProperties;
import janus.graph.data.client.transaction.Transaction;
import janus.graph.data.model.GremlinQueryResult;
import janus.graph.data.model.GremlinRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A {@link GremlinClient} implementation that communicates using REST over HTTP with a Gremlin Server.
 *
 * @author Abhishek Raj
 * @author Arun Patra
 */
public class RestGremlinClient implements GremlinClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestGremlinClient.class);

    private Gson gson = (new GsonBuilder()).setLenient().create();

    private GremlinClientProperties properties;

    private RestTemplate restTemplate = new RestTemplate();

    public RestGremlinClient(GremlinClientProperties properties) {
        this.properties = properties;
    }

    @Override
    public String submit(String gremlin, Map<String, Object> bindings) throws Exception {
        String gremlinServerEndpoint = properties.getChannel() + "://" + properties.getHost() + ":" + properties.getPort();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<GremlinRequest> request =
                new HttpEntity<>((new GremlinRequestBuilder())
                        .withGremlin(gremlin)
                        .withBindings(bindings)
                        .build(), requestHeaders);
        try {
            ResponseEntity<String> response = restTemplate
                    .exchange(gremlinServerEndpoint, HttpMethod.POST, request, String.class);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Response = {}", response.getBody());
            }
            // parse the response and get the data.
            RestGremlinResponse actualResponse = gson.fromJson(response.getBody(), getType(RestGremlinResponse.class, Object.class));
            return gson.toJson(actualResponse.getResult().getData());
        } catch (Exception e) {
            LOGGER.error("Failed to execute gremlin script at gremlin server. Root cause is - {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public String submit(String gremlin) throws Exception {
        return submit(gremlin, new HashMap<>());
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

    @Override
    public Transaction getTransaction() {
        throw new UnsupportedOperationException("RestGremlinClient does not support transactions.");
    }

    @Override
    public Transaction beginTransaction() {
        throw new UnsupportedOperationException("RestGremlinClient does not support transactions.");
    }

    @Override
    public Transaction beginTransaction(Transaction.Type type) {
        throw new UnsupportedOperationException("RestGremlinClient does not support transactions.");
    }

    /**
     * Wraps the response received from the gremlin server. A response from the gremlin server looks like this.
     * <p>
     * <pre>
     *     {
     *         "result":{"data":99,"meta":{}},
     *         "requestId":"ef2fe16c-441d-4e13-9ddb-3c7b5dfb10ba",
     *         "status":{"code":200,"attributes":{},"message":""}
     *     }
     * </pre>
     * <p>
     * The actual query response is available in the "data" element and depends on the gremlin script sent for execution,
     * this it can be any arbitrary data. This class has the capability of representing any arbitrary "data" result type.
     * For example,
     * <pre>
     *     RestGremlinResponse<Person> gremlinResponse = new RestGremlinResponse<>();</>
     * </pre>
     * represents a response that can be serialized to a Person class. Callers must model the response into Java types like
     * Person if they wish to consume the response. By default, we provide a mechanism to expose the actual response as a
     * string to the caller. In that case, the caller can use the following.
     * <p>
     * <pre>
     *     String result = gremlinResponse.getResult().getRawData();
     * </pre>
     *
     * @param <T> The type of response "data" expected from the server.
     * @author Abhishek Raj
     * @author Arun Patra
     */
    public static class RestGremlinResponse<T> {

        private String requestId;
        private ResponseStatus status;
        private GremlinQueryResult<T> result;

        public String getRequestId() {
            return requestId;
        }

        public void setRequestId(String requestId) {
            this.requestId = requestId;
        }

        public ResponseStatus getStatus() {
            return status;
        }

        public void setStatus(ResponseStatus status) {
            this.status = status;
        }

        public GremlinQueryResult<T> getResult() {
            return result;
        }

        public void setResult(GremlinQueryResult result) {
            this.result = result;
        }

        /**
         * Models the response status obtained from the server. The actual response from the server has the following structure.
         * <p>
         * <pre>
         *     "status":{"code":200,"attributes":{},"message":""}
         * </pre>
         */
        public static class ResponseStatus {
            private String message;
            private int code;
            private Map<String, Object> attributes;

            public Map<String, Object> getAttributes() {
                return attributes;
            }

            public void setAttributes(Map<String, Object> attributes) {
                this.attributes = attributes;
            }

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }
        }
    }

    private class GremlinRequestBuilder {

        private String gremlin;
        private Map<String, Object> bindings;

        GremlinRequestBuilder withGremlin(String gremlin) {
            this.gremlin = gremlin;
            return this;
        }

        GremlinRequestBuilder withBindings(Map<String, Object> bindings) {
            this.bindings = bindings;
            return this;
        }

        GremlinRequest build() {
            return new GremlinRequest(gremlin, bindings);
        }

    }
}
