package com.mindtree.graph.data.access.model;

import com.google.gson.annotations.JsonAdapter;
import com.mindtree.graph.data.access.model.adapter.PersonDataAdapter;

/**
 * This bean is not a standard Java bean because it need not to be. Its a DTO (data transfer object) which is used
 * to marshal incoming JSON data from the gremlin server.
 */
@JsonAdapter(PersonDataAdapter.class)
public class PersonData {

    public final long id;
    public final String name;
    public final int age;

    public PersonData(long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public String toString() {
        return "[" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "age = " + age +
                "]";
    }
}
