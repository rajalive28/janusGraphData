package com.mindtree.graph.data.access.tests.services;

import com.mindtree.graph.data.access.BaseAbstractTest;
import com.mindtree.graph.data.access.services.ModernService;
import janus.graph.data.client.service.GraphAccessService;
import janus.graph.data.model.GraphQueryResponse;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for checking the {@link ModernService} reference implementation.
 *
 * @author Abhishek Raj
 * @author Arun Patra
 */
public class ModernServiceTest extends BaseAbstractTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModernServiceTest.class);

    @Autowired
    private ModernService modernService;

    @Autowired
    private GraphAccessService graphAccessService;


    @Test
    public void getPersonsTest() throws Exception {
        List<String> results = modernService.getPersons();
        for (String res : results) {
            System.out.println("### " + res);
        }
        assertEquals(4, results.size());
    }

    @Test
    public void testGetPersonsWhoKnowThisSoftware() throws Exception {
        List<String> results = modernService.getPersonsWhoKnowThisSoftware("lop");
        assertEquals(3, results.size());
        assertTrue(hasItem("marko", results) && hasItem("peter", results) && hasItem("josh", results));
    }

    @Test
    public void addPersonsTest() throws Exception {
        List<String> personsToAdd = new ArrayList<>();
        personsToAdd.add("james");
        modernService.addPersons(personsToAdd);

        // now remove this guy otherwise other tests will fail which check the number of guys
        List<String> personsToDelete = new ArrayList<>();
        personsToDelete.add("james");
        modernService.deletePersons(personsToDelete);
    }

    @Test
    public void deletePersonsTest() throws Exception {
        LOGGER.info("Adding tom");
        List<String> personsToAdd = new ArrayList<>();
        personsToAdd.add("tom");
        modernService.addPersons(personsToAdd);

        LOGGER.info("Checking addition of tom");

        List<String> allPersons = modernService.getPersons();
        assertTrue(hasItem("tom", allPersons));

        LOGGER.info("Deleting tom");

        List<String> personsToDelete = new ArrayList<>();
        personsToDelete.add("tom");
        modernService.deletePersons(personsToDelete);

        LOGGER.info("Deleted tom");

        LOGGER.info("Checking deletion of tom");
        allPersons = modernService.getPersons();
        assertTrue(!hasItem("tom", allPersons));
    }

    private boolean hasItem(String person, List<String> results) {
        return (results.stream().filter(x -> x.equals(person)).collect(Collectors.toList()).size() == 1);
    }

    @Test
//    @Transactional(transactionManager = "graphTransactionManager")
    public void addSoftwareTest() throws Exception {
        // get list and check
        List<String> softwareList = modernService.getSoftwares();
        assertTrue(!hasItem("assetplus", softwareList));

        // now add
        List<String> softwares = new ArrayList<>();
        softwares.add("assetplus");
        modernService.addSoftware(softwares);

        // get list and check
        softwareList = modernService.getSoftwares();
        assertTrue(hasItem("assetplus", softwareList));

        // now delete
        modernService.deleteSoftware(softwares);
        // check deletion
        softwareList = modernService.getSoftwares();
        assertTrue(!hasItem("assetplus", softwareList));
    }

    /**
     * Do NOT run this test as part of the standard CI process, it will delete all vertices and all other tests might
     * fail.
     *
     * @throws Exception
     */
    @Test
    @Ignore
    public void deleteEntireGraphTest() throws Exception {

        // get original count; this is the total number of vertices that the modern graph has
        GraphQueryResponse<Long> originalGremlinResponse = graphAccessService.submit("g.V().count()", Long.class);
        assertTrue(originalGremlinResponse.getData().get(0) == 6);

        modernService.deleteEntireGraph();

        // check count of vertices
        GraphQueryResponse<Long> gremlinResponse = graphAccessService.submit("g.V().count()", Long.class);
        assertTrue(gremlinResponse.getData().get(0) == 0);
    }
}
