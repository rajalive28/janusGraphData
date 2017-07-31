package com.mindtree.graph.data.access.controllers;

import com.mindtree.graph.data.access.model.ModernResponse;
import com.mindtree.graph.data.access.services.ModernService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * A reference implementation to demonstrate interaction with a Graph database that complies with the Apache TinkerPop
 * specification. The "Modern" graph is used.
 *
 * @author Abhishek Raj
 * @author Arun Patra
 */
@RestController
@RequestMapping("/api/modern")
public class ModernGraphController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ModernGraphController.class);

    @Autowired
    private ModernService modernService;

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/person/{softwareName}", method = RequestMethod.GET, produces = {APPLICATION_JSON_VALUE})
    public List<String> getPersonsKnowingSoftware(@PathVariable("softwareName") String softwareName) {

        List<String> response = new ArrayList<>();
        try {
            response = modernService.getPersonsWhoKnowThisSoftware(softwareName);
        } catch (Exception e) {
            LOGGER.error("Request failed. {}", e.getMessage());
        }
        return response;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/person/checkMarkoExists", method = RequestMethod.GET, produces = {APPLICATION_JSON_VALUE})
    public String checkMarkoExists() {

        String response = null;
        try {
            response = modernService.checkMarkoExists();
        } catch (Exception e) {
            LOGGER.error("Request failed. {}", e.getMessage());
        }
        return response;
    }



    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/person/{personName}", method = RequestMethod.POST, produces = {APPLICATION_JSON_VALUE})
    public ModernResponse addSoftwareSkillsToPerson(@PathVariable("personName") String personName, @RequestBody List<String> softwareSkills) {

        return null;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/person", method = RequestMethod.PUT, produces = {APPLICATION_JSON_VALUE})
    public ModernResponse addPersons(@RequestBody List<String> persons) {
        ModernResponse response = new ModernResponse("success");
        try {
            modernService.addPersons(persons);
        } catch (Exception e) {
            response.setStatus("failed");
        }

        return response;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/person", method = RequestMethod.DELETE, produces = {APPLICATION_JSON_VALUE})
    public ModernResponse deletePersons(@RequestBody List<String> persons) {
        ModernResponse response = new ModernResponse("success");
        try {
            modernService.deletePersons(persons);
        } catch (Exception e) {
            response.setStatus("failure");
        }
        return response;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/software", method = RequestMethod.POST, produces = {APPLICATION_JSON_VALUE})
    public ModernResponse addSoftwares(@RequestBody List<String> softwares) {
        ModernResponse response = new ModernResponse("success");
        try {
            modernService.addSoftware(softwares);

        } catch (Exception e) {

            response.setStatus("failure");
        }
        return response;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/software", method = RequestMethod.DELETE, produces = {APPLICATION_JSON_VALUE})
    public ModernResponse deleteSoftwares(@RequestBody List<String> softwares) {
        ModernResponse response = new ModernResponse("success");
        try {
            modernService.deleteSoftware(softwares);
        } catch (Exception e) {
            response.setStatus("failure");
        }
        return response;
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/graph", method = RequestMethod.DELETE, produces = {APPLICATION_JSON_VALUE})
    public ModernResponse deleteEverythingInGraphDB() {
        ModernResponse response = new ModernResponse("success");
        try {
            modernService.deleteEntireGraph();
        } catch (Exception e) {
            response.setStatus("failure");
        }

        return response;
    }
}
