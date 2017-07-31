package janus.graph.data.model;

import java.util.List;
import java.util.Map;

/**
 * The gremlin query result. The actual data obtained can be of any arbitrary type <T>. The structure of a result is:
 * <p>
 * <pre>
 *     "result":{"data":[99],"meta":{}},
 * </pre>
 * <p>
 * An array of results is returned by the server in the "data" element. If the caller has not specified the type <T> to
 * which "data" should be serialized, the entire contents of "data" is made available in the "rawData" field of this
 * element.
 *
 * @param <T> The type to which "data" should be serialized.
 */
public class GremlinQueryResult<T> {
    private List<T> data;
    private Map<String, Object> meta;
    private String rawData;

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Map<String, Object> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }
}
