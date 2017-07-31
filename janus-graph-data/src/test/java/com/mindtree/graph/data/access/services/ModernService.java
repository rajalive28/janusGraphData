package com.mindtree.graph.data.access.services;

import janus.graph.data.client.service.GraphDataAccessException;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Service to demonstrate interaction with the "modern" graph.
 *
 * @author Abhishek Raj
 * @author Arun Patra
 */
public interface ModernService {

    List<String> getPersons() throws Exception;

    String checkMarkoExists() throws Exception;

    List<String> getSoftwares() throws Exception;

    List<String> getPersonsWhoKnowThisSoftware(String softwareName) throws Exception;

    void addPersons(List<String> persons) throws Exception;

    void deletePersons(List<String> persons) throws Exception;

    void deleteEntireGraph() throws GraphDataAccessException, ExecutionException, InterruptedException;

    void deleteSoftware(List<String> softwares) throws Exception;

    void addSoftware(List<String> softwares) throws Exception;
}
