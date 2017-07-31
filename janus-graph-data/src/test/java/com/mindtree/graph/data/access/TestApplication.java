package com.mindtree.graph.data.access;

import janus.graph.data.client.annotations.EnableGremlinClient;
import janus.graph.data.server.annotations.EnableEmbeddedGremlinServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * A test application to setup the testing environment.
 */
@EnableGremlinClient
@EnableEmbeddedGremlinServer
@SpringBootApplication
@PropertySource(value = {"classpath:graph-data-access.properties", "classpath:test-graph-data-access.properties"})
public class TestApplication extends SpringBootServletInitializer {

    @Autowired
    Environment env;

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(TestApplication.class);
    }
}
