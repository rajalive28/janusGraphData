package com.mindtree.graph.data.access.tests.services;

import com.mindtree.graph.data.access.BaseAbstractTest;
import com.mindtree.graph.data.access.model.PersonData;
import janus.graph.data.client.service.GraphAccessService;
import janus.graph.data.model.GraphQueryResponse;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @see https://github.com/apache/tinkerpop/blob/master/gremlin-driver/src/test/java/org/apache/tinkerpop/gremlin/driver/ser/GraphSONMessageSerializerV1d0Test.java
 */
public class GremlinGraphAccessServiceTest extends BaseAbstractTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GremlinGraphAccessServiceTest.class);

    @Autowired
    private GraphAccessService graphAccessService;

    @Test
    public void mathameticalCalculationTest() throws Exception {
        graphAccessService.submit("100-1");
    }

    @Test
    public void getPersonAsJavaTypeTest() throws Exception {
        GraphQueryResponse<PersonData> response = graphAccessService.submit("g.V().hasLabel('person')", PersonData.class);
        response.getData().stream().forEach(p -> {
            LOGGER.info("Person Name: {}", p.name);
        });
    }


    @Test

    public void availableFeaturesTest() throws Exception {
        GraphQueryResponse<Object> response = graphAccessService.submit("graph.features()");
        LOGGER.info("Features : \n  {}", response.getRawData());
    }

}
