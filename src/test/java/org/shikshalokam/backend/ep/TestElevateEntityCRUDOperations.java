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
    private String randomEntityName = RandomStringUtils.randomAlphabetic(11);
    private String randomExternalId = RandomStringUtils.randomAlphabetic(11);
    private String updatedEntityName = RandomStringUtils.randomAlphabetic(11);
    private String updatedEntityExternalId = RandomStringUtils.randomAlphabetic(11);
    private String userRolenametitle = RandomStringUtils.randomAlphabetic(11);
    private String userRoleId = RandomStringUtils.randomAlphabetic(11);
    private String childEntity;
    private String uniqueName = RandomStringUtils.randomAlphabetic(3) + randomEntityName;
    private String uniqueId = RandomStringUtils.randomAlphabetic(3) + randomExternalId;


    @BeforeMethod
    public void login() {
        loginToElevate(
                PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator"),
                PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator.password"));
    }

    @Test(description = "Adding a valid entity")
    public void testAddingValidEntity() {
        Response response = addEntity(uniqueName, uniqueId, PropertyLoader.PROP_LIST.getProperty("elevate.qa.automation.entitytype.name"));
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("message"), "ENTITY_ADDED");
        logger.info("Validation of entity addition is successful.");
    }

    @Test(description = "Adding a invalid entity")
    public void testAddingInvalidEntity() {
        Response response = addEntity("", "", PropertyLoader.PROP_LIST.getProperty("elevate.qa.automation.entitytype.name"));
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertTrue(response.asString().contains("The name field cannot be empty."));
        logger.info("Validation of invalid entity addition is successful.");
    }

    @Test(description = "Updating the entity with a random name", dependsOnMethods = "testAddingValidEntity")
    public void testUpdateValidEntity() {
        Response response = updateEntity(entity_Id, updatedEntityName, updatedEntityExternalId, PropertyLoader.PROP_LIST.getProperty("elevate.qa.automation.entitytype.name"));
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.asString().contains(updatedEntityName), "entity not found");
        logger.info("Validation related to updating an single entity is verified");
    }

    @Test(description = "Updating the entity to a different entity type", dependsOnMethods = "testAddingValidEntity")
    public void testUpdateEntityToDifferentEntityType() {
        getEntityId(uniqueId);
        Response response = updateEntity(entity_Id, updatedEntityName, updatedEntityExternalId, PropertyLoader.PROP_LIST.getProperty("elevate.qa.update.entitytype.name"));
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.asString().contains(updatedEntityName), "entity not found");
        // Reverting the entity back to its original entity type
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
        getEntityId(uniqueId);
        Response response = fetchEntitydetails(uniqueId);
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
    public void testBulkMappingEntities() {
        addEntity(randomEntityName, randomExternalId, PropertyLoader.PROP_LIST.getProperty("elevate.qa.automation.entitytype.name"));
        updateCSVWithChildAndParentEntityNames("src/main/resources/entities_mapping_ElevateProject.csv", "target/classes/entities_mapping_ElevateProject.csv", PropertyLoader.PROP_LIST.getProperty("elevate.qa.parent.entity.externalId"));
        Response response = bulkUploadMappingCSV("target/classes/entities_mapping_ElevateProject.csv", PropertyLoader.PROP_LIST.getProperty("elevate.qa.mapping.entity.endpoint"));
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.asString().contains("ENTITY_INFORMATION_UPDATE"));
        Assert.assertEquals(response.jsonPath().getString("result.success"), "true", "String message");
        logger.info("Validations related to Mapping entities are verified");
    }

    @Test(description = "Fetching the list of entities by using the entity Id")
    public void fetchEntityListBasedOnEntityId() {
        getEntityId(String.valueOf(PropertyLoader.PROP_LIST.getProperty("elevate.qa.parent.entity.externalId")));
        Response response = fetchEntityListBasedOnEntityId(entity_Id, "1", "100", "Automation_Entitytype");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.asString().contains("ENTITY_INFORMATION_FETCHED"), "Entity not found in the list");
        logger.info("Entity count = " + response.jsonPath().getString("count"));
        logger.info("Validations related to fetching entity list based on entity Id is verified");
    }

    @Test(description = "Fetch targeted roles for updated entity")
    public void testFetchTargetedRolesForEntity() {
        createUserRoleExtension(userRolenametitle, userRoleId, PropertyLoader.PROP_LIST.getProperty("elevate.qa.automation.entitytype.name"), PropertyLoader.PROP_LIST.getProperty("elevate.qa.automation.entitytype.Id"));
        getEntityId(PropertyLoader.PROP_LIST.getProperty("elevate.qa.parent.entity.externalId"));
        Response response = fetchTargetedRoles(entity_Id);
        Assert.assertEquals(response.getStatusCode(), 200, "roles not found");
        Assert.assertTrue(response.asString().contains(userRolenametitle));
        Assert.assertEquals(response.jsonPath().getString("message"), "ROLES_FETCHED_SUCCESSFULLY", "Roles fetching failed");
        List<?> rolesData = response.jsonPath().getList("result.data");
        Assert.assertTrue(rolesData.size() > 0, "Expected non-empty roles data for the entity");
        logger.info("validation related to entity targeted roles is verified");
    }

    @Test(description = "Test case for fetching entity related entities")
    public void testValidRelatedEntitiesBasedOnEntity() {
        getEntityId(PropertyLoader.PROP_LIST.getProperty("elevate.qa.parent.entity.externalId"));
        Response response = relatedEntitiesBasedOnEntityId(entity_Id);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().get("message"), "ENTITY_FETCHED");
        logger.info("Validations related to fetching entity related entities is verified");
    }

    @Test(description = "Negative test case for fetching entity related entities with out the path parameter")
    public void testInvalidRelatedEntitiesBasedOnEntity() {
        Response response = given()
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .header("x-auth-token", X_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .get("entity-management/v1/entities/relatedEntities");
        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertTrue(response.asString().contains("required Entity id"));
        logger.info("Validation related to Invalid fetching of entity related entities is verified");
    }


    @Test(dependsOnMethods = "testAddingValidEntity", description = "Test case to get entity details using entity Id")
    public void testValidEntityDetails() {
        Response response = EntitydetailsBasedonEntityId(entity_Id);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("message"), "ENTITY_INFORMATION_FETCHED");
        logger.info("Validations related to getting entity details using entity Id is verified");
    }

    @Test(description = "Negative test case to get entity details without path parameter")
    public void testInvalidEntityDetails() {
        Response response = given()
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .header("x-auth-token", X_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .get("entity-management/v1/entities/details");
        response.prettyPrint();
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertTrue(response.asString().contains("required id"));
        logger.info("Validations related to getting entity details without path parameter is verified");
    }

    @Test(dependsOnMethods = "testBulkMappingEntities")
    public void testFetchEntitySubListUsingParentEntityID() {
        getEntityId(PropertyLoader.PROP_LIST.getProperty("elevate.qa.parent.entity.externalId"));
        Response response = fetchEntitySubListBasedOnEntityId(entity_Id, "4", "400", PropertyLoader.PROP_LIST.getProperty("elevate.qa.automation.entitytype.name"));
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.asString().contains("ENTITIES_FETCHED"));
        int count = response.jsonPath().getInt("result.count");
        Assert.assertTrue(response.asString().contains(String.valueOf(childEntity)), "check pagination, each page contains 100 entities, so keep the pagination as \n Example 1 page = 1, limit = 100 \n example 2 page = 2, limit = 200");
        logger.info("Count of entities is " + count);
        logger.info("Validations related to fetching entity sublist is verified ");
    }

    @Test(description = "Test to get entities based on location ID")
    public void testfetchEntityDetailByLocationId() {
        getLocationID("Automation_ExternalId123");
        Response response = fetchEntityBasedOnLocationId(location_Id);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.asString().contains(location_Id), "Entity Details not fetched");
        logger.info("Validations related to fetching entities by using location ID is verified");
    }

    @Test(description = "test to fetch sub list of entities based on entity Id")
    public void testFetchSublistOfEntitiesByEntityId() {
        getEntityId(PropertyLoader.PROP_LIST.getProperty("elevate.qa.parent.entity.externalId"));
        Response response = listAllEntitiesBasedOnEntityId(entity_Id, "entityType");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.asString().contains(entity_Id));
        logger.info("Validations related to fetching the sub list of entities based on entity Id is verified");
    }

    // Method to add the entity
    private Response addEntity(String entityName, String externalId, String entityTypeName) {
        getEntitytype_Id(entityTypeName);
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", entityName);
        requestBody.put("entityType", entityType_Id);
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
    private void updateCSVWithChildAndParentEntityNames(String sourcePath, String targetPath, String parentEntityID) {
        try {
            // Read the initial CSV content
            String CSVcontent = new String(Files.readAllBytes(Paths.get(sourcePath)));
            logger.info("Original Content:\n" + CSVcontent);
            String parentEntityIdSystem = getEntityId(parentEntityID);
            CSVcontent = CSVcontent.replaceAll("parentEntity_Id", parentEntityIdSystem);

            Map<String, String> externalEntityIDs = new HashMap<>();
            for (int i = 1; i <= 3; i++) {
                String mappingEntityName = randomEntityName + RandomStringUtils.randomAlphanumeric(3);
                String mappingEntityExternalId = randomExternalId + RandomStringUtils.randomAlphanumeric(3);

                addEntity(mappingEntityName, mappingEntityExternalId, "Automation_Entitytype");
                externalEntityIDs.put("External_Id_" + i, mappingEntityExternalId);
            }
            logger.info(externalEntityIDs);
            Map<String, String> childEntityIds = new HashMap<>();
            for (Map.Entry<String, String> entry : externalEntityIDs.entrySet()) {
                String externalId = entry.getValue();
                String systemId = getEntityId(externalId);
                childEntityIds.put(entry.getKey().replace("External_Id", "childEntity_ID"), systemId);
            }
            for (Map.Entry<String, String> entry : childEntityIds.entrySet()) {
                String childEntity_ID = entry.getKey();
                childEntity = entry.getValue();
                CSVcontent = CSVcontent.replaceAll(childEntity_ID, childEntity);
            }
            Files.write(Paths.get(targetPath), CSVcontent.getBytes());
            logger.info("Final Updated Content:\n" + CSVcontent);
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Method to Map the child entity to parent entity using an CSV
    private Response bulkUploadMappingCSV(String targetPath, String endpoint) {
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

    // Method to fetch targeted roles for a given entity ID
    private Response fetchTargetedRoles(String entityId) {
        Response response = given()
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .header("x-auth-token", X_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .pathParam("entity_id", entityId)
                .get(PropertyLoader.PROP_LIST.getProperty("elevate.qa.targetedroles.endpoint") + "{entity_id}");
        response.prettyPrint();
        return response;
    }

    //Method to fetch related entities based on entity ID
    private Response relatedEntitiesBasedOnEntityId(String entityid) {
        Response response = given()
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .header("x-auth-token", X_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .pathParam("_id", entityid)
                .get(PropertyLoader.PROP_LIST.getProperty("elevate.qa.entity.realated.entities") + "{_id}");
        response.prettyPrint();
        return response;
    }

    //Method to fetch entity details based on entity ID
    private Response EntitydetailsBasedonEntityId(String entityid) {
        Response response = given()
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .header("x-auth-token", X_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .pathParam("_id", entityid)
                .get(PropertyLoader.PROP_LIST.getProperty("elevate.qa.entity.detailsbasedon.entityid.endpoint") + "{_id}");
        response.prettyPrint();
        return response;
    }

    //Method to fetch entity sublist using parent entity ID and entity type
    private Response fetchEntitySubListBasedOnEntityId(String entityid, String page, String limit, String entityType) {
        Response response = given()
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .header("x-auth-token", X_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .pathParam("_id", entityid)
                .queryParam("page", page)
                .queryParam("limit", limit)
                .queryParam("type", entityType)
                .get(PropertyLoader.PROP_LIST.getProperty("elevate.qa.entity.sublist.endpoint") + "{_id}");
        response.prettyPrint();
        return response;
    }

    private Response fetchEntityBasedOnLocationId(String locationId) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("locationIds", new String[]{locationId});
        Response response = given()
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .header("x-auth-token", X_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post(PropertyLoader.PROP_LIST.getProperty("elevate.qa.entities.listbylocatioId"));
        response.prettyPrint();
        return response;
    }

    private Response listAllEntitiesBasedOnEntityId(String entityId, String requiredField) {
        JSONObject requestBody = new JSONObject();
        JSONArray entities = new JSONArray();
        entities.add(entityId);
        requestBody.put("entities", entities);
        JSONArray field = new JSONArray();
        field.add(requiredField);
        requestBody.put("fields", field);
        Response response = given()
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .header("x-auth-token", X_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post("entity-management/v1/entities/listByIds");
        response.prettyPrint();
        return response;
    }

}