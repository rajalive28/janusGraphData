package janus.graph.data.model;

import janus.graph.data.model.adapter.AbstractTypeAdapter;

import java.util.List;

/**
 * The model of a gremlin server response. The caller can ask for the response to be auto-serialized. However, suitable
 * adapters must be available. See {@link AbstractTypeAdapter}.
 *
 * @param <T>
 */
public class GraphQueryResponse<T> {
    private List<T> data;
    private String rawData;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }
}
