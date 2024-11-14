package org.shikshalokam.backend.ep;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.shikshalokam.backend.PropertyLoader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static io.restassured.RestAssured.given;

public class TestElevateEntityCRUDOperations extends ElevateProjectBaseTest {
    private Logger logger = LogManager.getLogger(TestElevateEntityCRUDOperations.class);
    private String randomEntityName = RandomStringUtils.randomAlphabetic(10);
    private String randomExternalId = RandomStringUtils.randomAlphabetic(10);


    @BeforeMethod
    public void setUp() {
        loginToElevate(
                PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator"),
                PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator.password"));
    }

    @Test(description = "Adding a valid entity")
    public void testAddingValidEntity() {
        Response response = addEntity(randomEntityName, PropertyLoader.PROP_LIST.getProperty("elevate.qa.automation.entitytype.Id"), randomExternalId, PropertyLoader.PROP_LIST.getProperty("elevate.qa.automation.entitytype.name"));
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("message"), "ENTITY_ADDED");
        logger.info("Validation of entity addition is successful.");
    }

    @Test(description = "Adding a invalid entity")
    public void testAddingInvalidEntity() {
        Response response = addEntity("", PropertyLoader.PROP_LIST.getProperty("elevate.qa.automation.entitytype.Id"), "", PropertyLoader.PROP_LIST.getProperty("elevate.qa.automation.entitytype.name"));
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertTrue(response.asString().contains("The name field cannot be empty."));
        logger.info("Validation of invalid entity addition is successful.");
    }

    @Test(description = "Updating the entity with a random name", dependsOnMethods = "testAddingValidEntity")
    public void testUpdateValidEntity() {
        getSystemId(randomExternalId);
        Response response = updateEntity(entity_Id, randomEntityName + "123", randomExternalId + "dfdf7gd");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.asString().contains(randomEntityName), "entity not found");
        logger.info("Entity updated successfully to: " + randomEntityName);
    }

    @Test(description = "fetching entity details based on externalId", dependsOnMethods = "testAddingValidEntity")
    public void testFetchValidEntityDetails() {
        Response response = fetchEntitydetails(randomExternalId);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.jsonPath().getString("message").contains("ASSETS_FETCHED_SUCCESSFULLY"));
        logger.info("Validations related to fetching the Valid single entity details is verified");
    }

    //This TC cannot be verified at the moment because there is an entity which is created with no name
    @Test(description = "fetching entity details based on externalId", dependsOnMethods = "testAddingValidEntity")
    public void testInvalidFetchEntityDetails() {
        Response response = fetchEntitydetails("");
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertTrue(response.jsonPath().getString("message").contains("ENTITY_NOT_FOUND"));
        logger.info("Validations related to fetching the Invalid single entity details is verified");
    }

    @Test(description = "Mapping the entities to parent entity using CSV upload")
    public void testMappingEntities() {
        addEntity(randomEntityName, PropertyLoader.PROP_LIST.getProperty("elevate.qa.automation.entitytype.Id"), randomExternalId, PropertyLoader.PROP_LIST.getProperty("elevate.qa.automation.entitytype.name"));
        testUploadEntitiesMappingCsvFile("src/main/resources/entities_mapping_ElevateProject.csv", "target/classes/entities_mapping_ElevateProject.csv");
        Response response = uploadMappingCSV("target/classes/entities_mapping_ElevateProject.csv", PropertyLoader.PROP_LIST.getProperty("elevate.qa.mapping.entity.endpoint"));
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.asString().contains("ENTITY_INFORMATION_UPDATE"));
        logger.info("Validations related to Mapping entities are verified");
    }

    @Test(description = "", dependsOnMethods = "testMappingEntities")
    public void fetchEntityListBasedOnEntityId() {
        getSystemId(PropertyLoader.PROP_LIST.getProperty("elevate.qa.entity.externalID"));
        Response response = fetchEntityListBasedOnEntityId(entity_Id, "1", "100", "Automation_Entitytype");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.asString().contains(randomEntityName), "Entity not found in the list");
    }

    // Method to add the entity
    private Response addEntity(String entityName, String entityTypeId, String externalId, String entityTypeName) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", entityName);
        requestBody.put("entityType", entityTypeId);
        requestBody.put("externalId", externalId);
        Response response = given()
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .header("x-auth-token", X_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .queryParam("type", entityTypeName)
                .post(PropertyLoader.PROP_LIST.getProperty("elevate.qa.entityadd.endpoint"));
        response.prettyPrint();
        return response;
    }

    // Method to update an entity by its ID
    private Response updateEntity(String entityid, String updatedEntityName, String updatedExternalId) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("metaInformation.externalId", updatedExternalId);
        requestBody.put("metaInformation.name", updatedEntityName);
        requestBody.put("childHierarchyPath", new JSONArray());
        requestBody.put("createdBy", "user123");
        requestBody.put("updatedBy", "user123");
        Response response = given()
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .header("x-auth-token", X_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .pathParam("_id", entityid)
                .post(PropertyLoader.PROP_LIST.getProperty("elevate.qa.entityupdate.endpoint") + "{_id}");
        response.prettyPrint();
        return response;
    }

    private Response fetchEntityListBasedOnEntityId(String entityid, String page, String limit, String entityType) {
        Response response = given()
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .header("x-auth-token", X_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .pathParam("_id", entityid)
                .queryParam("page", page)
                .queryParam("limit", limit)
                .queryParam("type", entityType)
                .get(PropertyLoader.PROP_LIST.getProperty("elevate.qa.fetchentity.list.endpoint") + "{_id}");
        response.prettyPrint();
        return response;
    }

    private void testUploadEntitiesMappingCsvFile(String sourcePath, String targetPath) {
        // Path to the CSV file

        try {
            String CSVcontent = new String(Files.readAllBytes(Paths.get(sourcePath)));
            List<String> childEntityID;
            childEntityID = Collections.singletonList(getSystemId(randomExternalId));
            logger.info("Original Content:\n" + CSVcontent);
            for (String childEntity : childEntityID) {
                CSVcontent = CSVcontent.replaceFirst("childEntityID", childEntity);
                Files.write(Paths.get(targetPath), CSVcontent.getBytes());
                logger.info("Updated Content:\n" + CSVcontent);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Response uploadMappingCSV(String targetPath, String endpoint) {
        File csvFile = new File(targetPath);
        logger.info(csvFile + "**************************8");
        if (!csvFile.exists()) {
            logger.error("CSV file does not exist: " + csvFile.getAbsolutePath());
        }
        // Send the POST request to upload the CSV file
        Response response = given()
                .multiPart("entityMap", csvFile)
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)  // Internal access token header
                .header("x-auth-token", X_AUTH_TOKEN)  // x-authenticated-token header
                .header("Content-Type", "multipart/form-data")
                .post(endpoint);
        logger.info("Response Status Code: " + response.getStatusCode());
        response.prettyPrint();
        return response;
    }
}













