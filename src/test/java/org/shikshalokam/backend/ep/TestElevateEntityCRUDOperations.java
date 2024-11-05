package org.shikshalokam.backend.ep;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class TestElevateEntityCRUDOperations extends ElevateProjectBaseTest {
    private String randomName = RandomStringUtils.randomAlphabetic(10);
    private String randomExternalId = RandomStringUtils.randomAlphabetic(10);
    private static final Logger logger = LogManager.getLogger(TestElevateEntityCRUDOperations.class);

    @BeforeMethod
    public void userLogin() {
        loginToElevate(PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator"),
                PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator.password"));
    }

    // Method to create an entity with random 'name' and 'externalId'
    private Response addEntities(String entityTypeId, String name, String externalId) {
        Response response = given()
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .header("x-auth-token", X_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .body("{ \"entityTypeId\": \"" + entityTypeId + "\", " +
                        "\"name\": \"" + name + "\", " +
                        "\"externalId\": \"" + externalId + "\" }")
                .post(PropertyLoader.PROP_LIST.getProperty("elevate.qa.entityadd.endpoint"));

        response.prettyPrint();
        return response;
    }

    @Test(description = "adding valid entity with random name and externalId")
    public void testAddingSingleEntity() {
        Response response = addEntities("someEntityTypeId", randomName, randomExternalId);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(response.jsonPath().getString("message"), "ENTITY_ADDED");
        logger.info("New entity created with name: " + randomName + " and externalId: " + randomExternalId);
    }

    @Test(description = "adding an entity with empty required fields")
    public void testInvalidAddingSingleEntity() {
        Response response = addEntities("someEntityTypeId", "", "");
        Assert.assertEquals(response.getStatusCode(), 400);

        // Check for the specific message regarding the empty 'name' field
        String expectedMessage = "The name field cannot be empty.";
        String actualMessage = response.jsonPath().getString("message[0].msg");

        Assert.assertEquals(actualMessage, expectedMessage);
        logger.info("Validation testInvalidAddingSingleEntity is verified");
    }
}









