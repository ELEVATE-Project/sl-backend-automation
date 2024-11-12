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
import java.util.HashMap;
import java.util.Map;
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
        logger.info("Validation of invalidentity addition is successful.");
    }

    @Test(description = "Updating the entity with a random name",dependsOnMethods ="testAddingValidEntity" )
    public void testUpdatingEntity() {
        getSystemId(randomExternalId);
        Response response = updateEntity( entity_Id,randomEntityName +"123",randomExternalId + "dfdf7gd");
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.asString().contains(randomEntityName),"entity not found");
        logger.info("Entity updated successfully to: " + randomEntityName);
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
    private Response updateEntity(String entityid ,String updatedEntityName, String updatedExternalId) {
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
                .pathParam("_id",entityid )
                .post(PropertyLoader.PROP_LIST.getProperty("elevate.qa.entityupdate.endpoint") + "{_id}");
        response.prettyPrint();
        return response;
    }
}















