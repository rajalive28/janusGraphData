package janus.graph.data.model.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;

import java.io.IOException;

/**
 * Abstract class that provides helpful base functionality. Users are free to either extend this class or
 * {@link TypeAdapter}.
 *
 * @param <T> The type of model.
 */
public abstract class AbstractTypeAdapter<T> extends TypeAdapter<T> {
    private static final String BOOLEAN = "Boolean";
    private static final String LONG = "Long";
    private static final String DOUBLE = "Double";
    private static final String INTEGER = "Integer";
    private static final String STRING = "String";
    private static final String FLOAT = "Float";

    /**
     * Extracts the named field in the current JSON element. Note that, this method should be called at the correct
     * point of time while processing the JSON tree. If an unknown type is requested, this method will throw a
     * {@link RuntimeException}.
     *
     * @param in        The JsonReader
     * @param fieldName The field name, whose actual value needs to be extracted.
     * @param type      The type of field we are expecting.
     * @param <U>       The generic type of the field
     * @return The field value
     * @throws IOException, RuntimeException
     */
    public <U> U extractField(JsonReader in, String fieldName, Class<U> type) throws IOException {
        U value = null;
        in.beginArray();
        in.beginObject();
        while (in.hasNext()) {
            if (in.nextName().equalsIgnoreCase(fieldName)) {
                switch (type.getSimpleName()) {
                    case BOOLEAN:
                        value = type.cast(in.nextBoolean());
                        break;
                    case LONG:
                        value = type.cast(in.nextLong());
                        break;
                    case INTEGER:
                        value = type.cast(in.nextInt());
                        break;
                    case DOUBLE:
                        value = type.cast(in.nextDouble());
                        break;
                    case STRING:
                        value = type.cast(in.nextString());
                        break;
                    case FLOAT:
                        value = type.cast(in.nextDouble());
                        break;
                    default:
                        throw new RuntimeException("Unhandled type: " + type.getSimpleName());
                }
            } else {
                in.skipValue(); // skip all other fields
            }
        }
        in.endObject();
        in.endArray();
        return value;
    }
}
