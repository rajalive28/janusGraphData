package com.mindtree.graph.data.access.services.impl;

import com.mindtree.graph.data.access.model.PersonData;
import com.mindtree.graph.data.access.services.ModernService;
import janus.graph.data.client.service.GraphAccessService;
import janus.graph.data.client.service.GraphDataAccessException;
import janus.graph.data.model.GraphQueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Implementation of the {@link ModernService} as a reference implementation.
 *
 * @author Abhishek Raj
 * @author Arun Patra
 */
@Service
public class ModernServiceImpl implements ModernService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModernServiceImpl.class);

    @Autowired
    private GraphAccessService graphAccessService;

    @Override
    public List<String> getPersonsWhoKnowThisSoftware(String softwareName) throws Exception {
        List<String> results = new ArrayList<>();

        String gremlinQuery = "g.V().hasLabel('software').has('name',n).in('created').values('name')";
        Map<String, Object> params = new HashMap<>();
        params.put("n", softwareName);

        try {
            GraphQueryResponse<String> gremlinResponse = graphAccessService.submit(gremlinQuery, params, String.class);
            results = gremlinResponse.getData();
        } catch (Exception e) {
            LOGGER.error("Error: {}", e.getMessage());
        }

        return results;
    }
    @Override
    public String checkMarkoExists() throws Exception {
        String results = null;
        String gremlinQuery = "g.V().has('name','marko')";

        try {
            GraphQueryResponse<PersonData> gremlinResponse = graphAccessService.submit(gremlinQuery, PersonData.class);
            results = gremlinResponse.getRawData();
        } catch (Exception e) {
            LOGGER.error("Error: {}", e.getMessage());
        }
        LOGGER.debug("Output {}",results);
        return results;
    }

    @Override
    public List<String> getSoftwares() throws Exception {
        List<String> results = new ArrayList<>();

        String gremlinQuery = "g.V().hasLabel('software').values('name')";

        try {
            GraphQueryResponse<String> gremlinResponse = graphAccessService.submit(gremlinQuery, String.class);
            results = gremlinResponse.getData();
        } catch (Exception e) {
            LOGGER.error("Error: {}", e.getMessage());
        }

        return results;
    }

    @Override
    public List<String> getPersons() throws Exception {
        List<String> results = new ArrayList<>();

        String gremlinQuery = "g.V().hasLabel('person').values('name')";

        try {
            GraphQueryResponse<String> gremlinResponse = graphAccessService.submit(gremlinQuery, String.class);
            results = gremlinResponse.getData();
        } catch (Exception e) {
            LOGGER.error("Error: {}", e.getMessage());
        }

        return results;
    }

    @Override
    public void addPersons(List<String> persons) throws Exception {
        String gremlin = "g.addV(label, 'person', 'name', personName, 'age', 27)";

        try {
            for (String person : persons) {
                Map<String, Object> params = new HashMap<>();
                params.put("personName", person);
                GraphQueryResponse<PersonData> gremlinResponse = graphAccessService.submit(gremlin, params, PersonData.class);
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.info("Person added = {}", gremlinResponse.getData().get(0).toString());
                }
            }
        } catch (Exception e) {
            LOGGER.error("Error: {}", e.getMessage());
            throw new Exception(e);
        }
    }

    @Override
    public void deletePersons(List<String> persons) throws Exception {
        String deleteGremlin = "g.V().has('person','name',p).drop()";
        try {
            for (String person : persons) {
                Map<String, Object> params = new HashMap<>();
                params.put("p", person);
                GraphQueryResponse resultSet = graphAccessService.submit(deleteGremlin, params);
                LOGGER.info(resultSet.getRawData());
            }
        } catch (Exception e) {
            LOGGER.error("Error: {}", e.getMessage());
            throw new Exception(e);
        }
    }

    @Override
    public void deleteEntireGraph() throws GraphDataAccessException, ExecutionException, InterruptedException {
        String gremlin = "g.V().drop()";
        GraphQueryResponse<Object> response = graphAccessService.submit(gremlin);
        LOGGER.info("Graph deletion response is : {}", response.getRawData());
    }

    @Override
    public void deleteSoftware(List<String> softwares) throws Exception {
        String deleteGremlin = "g.V().has('software','name',s).drop()";
        try {
            for (String software : softwares) {
                Map<String, Object> params = new HashMap<>();
                params.put("s", software);
                //performing deletion query
                GraphQueryResponse<Object> response = graphAccessService.submit(deleteGremlin, params);
                LOGGER.info("Delete query response: {}", response.getRawData());
            }
        } catch (Exception e) {
            LOGGER.error("Error: {}", e.getMessage());
            throw new Exception(e);
        }
    }


    @Override
    public void addSoftware(List<String> softwares) throws Exception {
        String gremlin = "g.addV(label, 'software', 'name', n, 'lang', 'java')";

        try {
            for (String software : softwares) {
                Map<String, Object> params = new HashMap<>();
                params.put("n", software);
                GraphQueryResponse<Object> result = graphAccessService.submit(gremlin, params);
                LOGGER.info("Response {}", result.getRawData());
            }
        } catch (Exception e) {
            LOGGER.error("Error: {}", e.getMessage());
            throw new Exception(e);
        }
    }
}