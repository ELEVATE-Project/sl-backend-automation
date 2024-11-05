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
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TestElevateEntityCRUDOperations extends ElevateProjectBaseTest {
    private static final Logger logger = LogManager.getLogger(TestElevateEntityCRUDOperations.class);
  private   String randomName = RandomStringUtils.randomAlphabetic(10);
     private String randomExternalId = RandomStringUtils.randomAlphabetic(10);
    @BeforeMethod
    public void userLogin() {
        loginToElevate(PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator"),
                PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator.password"));
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
        logger.info("Validation with empty 'name' field is verified");
    }

    // Method to create an entity with specified 'name' and 'externalId'
    private Response addEntities(String entityTypeId, String name, String externalId) {
        // Create the request body as a Map
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("entityTypeId", entityTypeId);
        requestBody.put("name", name);
        requestBody.put("externalId", externalId);

        // Log the request body for debugging
        logger.info("Request body: " + requestBody);

        // Sending the request
        Response response = given()
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .header("x-auth-token", X_AUTH_TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody)  // Use the Map directly as the body
                .post(PropertyLoader.PROP_LIST.getProperty("elevate.qa.entityadd.endpoint"));

        response.prettyPrint(); // Log the response for debugging
        return response;
    }
}
