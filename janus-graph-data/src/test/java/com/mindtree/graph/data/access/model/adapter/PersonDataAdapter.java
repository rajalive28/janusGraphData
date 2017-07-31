package com.mindtree.graph.data.access.model.adapter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.mindtree.graph.data.access.model.PersonData;
import janus.graph.data.model.adapter.AbstractTypeAdapter;

import java.io.IOException;

/**
 * A {@link TypeAdapter} for mapping PersonData.
 *
 * @author Arun Patra
 */
public class PersonDataAdapter extends AbstractTypeAdapter<PersonData> {

    @Override
    public void write(JsonWriter out, PersonData value) throws IOException {
        // NO-OP for now, we don't want to write. Implement this if you want to write JSON from a PersonData object.
    }

    @Override
    public PersonData read(JsonReader in) throws IOException {
        long id = 0L;
        String name = null;
        int age = 0;

        in.beginObject(); // start; {
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    id = in.nextLong();
                    break;
                case "properties":
                    in.beginObject();
                    while (in.hasNext()) {
                        switch (in.nextName()) {
                            case "name":
                                name = extractField(in, "value", String.class);
                                break;
                            case "age":
                                age = extractField(in, "value", Integer.class);
                                break;
                            default:
                                in.skipValue(); // skip all others
                                break;
                        }
                    }
                    in.endObject();
                    break;
                default:
                    in.skipValue(); // skip all others
                    break;
            }
        }
        in.endObject(); // end; }
        return new PersonData(id, name, age);
    }
}
