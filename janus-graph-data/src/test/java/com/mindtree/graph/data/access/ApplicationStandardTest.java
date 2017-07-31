package com.mindtree.graph.data.access;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import static org.junit.Assert.assertTrue;

/**
 * Basic test to check if application loads.
 */
public class ApplicationStandardTest extends BaseAbstractTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationStandardTest.class);

    @Autowired
    private ApplicationContext context;

    @Test
    public void doCheckIfSystemInitializedProperly() {
        assertTrue(context.containsBean("embeddedGremlinServer"));
        assertTrue(context.containsBean("graphAccessService"));
    }
}
