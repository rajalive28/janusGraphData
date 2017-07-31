package com.mindtree.graph.data.access.tests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindtree.graph.data.access.BaseAbstractTest;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller tests.
 *
 * @author Abhishek Raj
 * @author Arun Patra
 */

public class ModernGraphControllerTest extends BaseAbstractTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModernGraphControllerTest.class);
    private static final String MODERN_API = "/api/modern";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        setupMockMvc();
    }

    @Test
    public void getPersonsKnowingSoftwareTest() throws Exception {
        assertNotNull(mockMvc);
        mockMvc.perform(get(MODERN_API + "/person/lop"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void checkMarkoExists() throws Exception {
        assertNotNull(mockMvc);
        mockMvc.perform(get(MODERN_API + "/person/checkMarkoExists"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
    }

    @Test
    public void addPersonTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<String> persons = Arrays.asList("arun2", "abhishek2");
        assertNotNull(mockMvc);
        mockMvc.perform(put(MODERN_API + "/person").content(mapper.writeValueAsString(persons)).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.status").value(containsString("success")));
        // delete
        mockMvc.perform(delete(MODERN_API + "/person").content(mapper.writeValueAsString(persons)).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.status").value(containsString("success")));
    }

    @Test
    public void deletePersonTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<String> persons = Arrays.asList("arun1", "abhishek1");
        List<String> personsToBeDeleted = Arrays.asList("arun1", "abhishek1");
        assertNotNull(mockMvc);
        //initialize data
        mockMvc.perform(put(MODERN_API + "/person").content(mapper.writeValueAsString(persons)).contentType(contentType));
        mockMvc.perform(delete(MODERN_API + "/person").content(mapper.writeValueAsString(personsToBeDeleted)).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.status").value(containsString("success")));
    }

    @Test
    public void addSoftwareTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<String> softwares = Arrays.asList("ojp", "cjp");
        assertNotNull(mockMvc);
        mockMvc.perform(post(MODERN_API + "/software").content(mapper.writeValueAsString(softwares)).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.status").value(containsString("success")));
        // now cleanup
        mockMvc.perform(delete(MODERN_API + "/software").content(mapper.writeValueAsString(softwares)).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.status").value(containsString("success")));
    }

    @Test
    public void deleteSoftwareTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<String> softwares = Arrays.asList("ojp1", "cjp1");
        List<String> softwaresToBeDeleted = Arrays.asList("ojp1", "cjp1");
        assertNotNull(mockMvc);
        mockMvc.perform(post(MODERN_API + "/software").content(mapper.writeValueAsString(softwares)).contentType(contentType));
        mockMvc.perform(delete(MODERN_API + "/software").content(mapper.writeValueAsString(softwaresToBeDeleted)).contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.status").value(containsString("success")));
    }

    /**
     * Do NOT run this test as part of the regular CI build, it will delete everything in the test setup and other
     * tests will fail.
     *
     * @throws Exception
     */
    @Test
    @Ignore
    public void deleteEntireGraphTest() throws Exception {
        assertNotNull(mockMvc);
        mockMvc.perform(delete(MODERN_API + "/graph").contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.status").value(containsString("success")));
    }
}
