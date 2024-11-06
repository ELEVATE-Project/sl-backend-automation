package org.shikshalokam.backend.ep;

import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    @Test(description = "Adding a valid entity")
    public void testAddingInvalidEntity() {
        Response response = addEntity("", PropertyLoader.PROP_LIST.getProperty("elevate.qa.automation.entitytype.Id"), "", PropertyLoader.PROP_LIST.getProperty("elevate.qa.automation.entitytype.name"));
        Assert.assertEquals(response.getStatusCode(), 400);
        Assert.assertTrue(response.asString().contains("The name field cannot be empty."));
        logger.info("Validation of invalidentity addition is successful.");
    }

    // Method to add the entity
    private Response addEntity(String entityName, String entityTypeId, String externalId, String entitytypename) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", entityName);
        requestBody.put("entityType", entityTypeId);
        requestBody.put("externalId", externalId);
        Response response = given()
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .header("x-auth-token", X_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .queryParam("type", entitytypename)
                .post(PropertyLoader.PROP_LIST.getProperty("elevate.qa.entityadd.endpoint"));
        response.prettyPrint();
        return response;
    }
}









