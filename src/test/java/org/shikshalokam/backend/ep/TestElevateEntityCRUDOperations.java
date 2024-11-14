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
    private String updatedEntityName = RandomStringUtils.randomAlphabetic(10);
    private String updatedEntityExternalId = RandomStringUtils.randomAlphabetic(10);


    @BeforeMethod
    public void setUp() {
        loginToElevate(
                PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator"),
                PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator.password"));
    }

    @Test(description = "Adding a valid entity")
    public void testAddingValidEntity() {
        logger.info("**************************" + randomExternalId);
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
        logger.info("**************************" + randomExternalId);
        getSystemId(randomExternalId);
        Response response = updateEntity(entity_Id, updatedEntityName, updatedEntityExternalId, PropertyLoader.PROP_LIST.getProperty("elevate.qa.automation.entitytype.name"));
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.asString().contains(updatedEntityName), "entity not found");
        logger.info("Validation related to updating an single entity is verified");
    }

    @Test(description = "Updating the entity to a different entity type", dependsOnMethods = "testAddingValidEntity")
    public void testUpdateEntityToDifferentEntityType() {
        getSystemId(randomExternalId);
        Response response = updateEntity(entity_Id, updatedEntityName, updatedEntityExternalId, PropertyLoader.PROP_LIST.getProperty("elevate.qa.update.entitytype.name"));
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.asString().contains(updatedEntityName), "entity not found");
        // Reverting the entity back to it's original entity type
        updateEntity(entity_Id, updatedEntityName, updatedEntityExternalId, PropertyLoader.PROP_LIST.getProperty("elevate.qa.automation.entitytype.name"));
        logger.info("Validations related to updating an entity to a different entity type is verified");
    }

    @Test(description = "Updating an Entity without passing the _Id as a parameter")
    public void testUpdateInvalidEntity() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("entityType", "Automation_Entitytype");
        requestBody.put("metaInformation.externalId", "");
        requestBody.put("metaInformation.name", "");
        requestBody.put("childHierarchyPath", new JSONArray());
        requestBody.put("createdBy", "user123");
        requestBody.put("updatedBy", "user123");
        Response response = given()
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .header("x-auth-token", X_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post("entity-management/v1/entities/update");
        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertTrue(response.asString().contains("required _id"));
        logger.info("Validation related to Invalid Updating of entity is verified");
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
        updateCSVWithChildAndParentEntityNames("src/main/resources/entities_mapping_ElevateProject.csv", "target/classes/entities_mapping_ElevateProject.csv", PropertyLoader.PROP_LIST.getProperty("elevate.qa.parent.entity.externalId"), randomExternalId);
        Response response = uploadMappingCSV("target/classes/entities_mapping_ElevateProject.csv", PropertyLoader.PROP_LIST.getProperty("elevate.qa.mapping.entity.endpoint"));
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.asString().contains("ENTITY_INFORMATION_UPDATE"));
        logger.info("Validations related to Mapping entities are verified");
    }

    @Test(description = "Fetching the list of entities by using the entity Id", dependsOnMethods = "testMappingEntities")
    public void fetchEntityListBasedOnEntityId() {
        getSystemId(PropertyLoader.PROP_LIST.getProperty("elevate.qa.parent.entity.externalId"));
        Response response = fetchEntityListBasedOnEntityId(entity_Id, "1", "100", "Automation_Entitytype");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.asString().contains(randomEntityName), "Entity not found in the list");
        logger.info("Entity count = " + response.jsonPath().getString("count"));
        logger.info("Validations related to fetching entity list based on entity Id is verified");
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
    private Response updateEntity(String entityid, String updatedEntityName, String updatedExternalId, String updatedEntityTypeName) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("entityType", updatedEntityTypeName);
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

    // Method to fetch Entity List based on entity Id
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

    // Method to fill the mapping CSV with child entity ID's
    private void updateCSVWithChildAndParentEntityNames(String sourcePath, String targetPath, String parentEntityID, String ChildEntityId) {
        try {
            String CSVcontent = new String(Files.readAllBytes(Paths.get(sourcePath)));
            logger.info("Original Content:\n" + CSVcontent);
            List<String> parentEntityId;
            parentEntityId = Collections.singletonList(getSystemId(parentEntityID));
            for (String parentEntity_Id : parentEntityId) {
                CSVcontent = CSVcontent.replaceFirst("parentEntity_Id", parentEntity_Id);
                Files.write(Paths.get(targetPath), CSVcontent.getBytes());
                logger.info("Updated Content:\n" + CSVcontent);
            }

            String CSVcontentChild = new String(Files.readAllBytes(Paths.get(targetPath)));
            logger.info("Original Content:\n" + CSVcontentChild);
            List<String> childEntityID;
            childEntityID = Collections.singletonList(getSystemId(ChildEntityId));
            for (String childEntity : childEntityID) {
                CSVcontentChild = CSVcontentChild.replaceFirst("childEntity_ID", childEntity);
                Files.write(Paths.get(targetPath), CSVcontentChild.getBytes());
                logger.info("Updated Content:\n" + CSVcontentChild);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Method to Map the child entity to parent entity using an CSV
    private Response uploadMappingCSV(String targetPath, String endpoint) {
        File csvFile = new File(targetPath);
        if (!csvFile.exists()) {
            logger.error("CSV file does not exist: " + csvFile.getAbsolutePath());
        }
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













