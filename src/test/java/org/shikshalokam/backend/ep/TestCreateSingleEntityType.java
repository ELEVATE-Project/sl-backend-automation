package org.shikshalokam.backend.ep;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class TestCreateSingleEntityType extends ElevateProjectBaseTest {
    private static final Logger logger = LogManager.getLogger(TestCreateSingleEntityType.class);
    public static String internalAccessToken = "Fqn0m0HQ0gXydRtBCg5l";

    @Test(description = "creating valid entity type")
    public void testCreateValidEntityType() {
        createValidEntityType("Automation entity new 22");
    }

    //method to create the entity type - can be reusable in the future
    public void createValidEntityType(String entityName) {
        ElevateProjectBaseTest.response = loginToElevate(
                PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator"),
                PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator.password"));
        RestAssured.baseURI = PropertyLoader.PROP_LIST.getProperty("elevate.qa.api.base.url");

        // request body using a Map for valid entity creation
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", entityName);
        Map<String, Object> registryDetails = new HashMap<>();
        registryDetails.put("name", entityName);
        requestBody.put("registryDetails", registryDetails);
        requestBody.put("isObservable", true);
        requestBody.put("toBeMappedToParentEntities", true);

        // POST request to create an entity type
        Response response = given()
                .header("Authorization", internalAccessToken)
                .header("internal-access-token", internalAccessToken)
                .header("x-auth-token", X_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post(PropertyLoader.PROP_LIST.getProperty("elevate.qa.entitytypesinglecreate.endpoint"));
        response.prettyPrint();
//Checking error scenario
        if (response.getStatusCode() != 200) {
            logger.info(response.getStatusCode() + "Change or Check Entity Type Name");
            System.exit(-1);
        }
        // Validating the response
        Assert.assertEquals(response.getStatusCode(), 200, "Expected 200 status code.");
        Assert.assertEquals(response.jsonPath().getString("message"), "ENTITY_INFORMATION_CREATED");
        logger.info("Validation with correct credentials is verified.");
    }

    @Test(description = "Creating an entity type with empty request fields")
    public void testCreateInvalidEntityType() {
        ElevateProjectBaseTest.response = loginToElevate(
                PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator"),
                PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator.password"));
        RestAssured.baseURI = PropertyLoader.PROP_LIST.getProperty("elevate.qa.api.base.url");


        // Empty request body using a Map
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "");
        Map<String, Object> registryDetails = new HashMap<>();
        registryDetails.put("name", "");
        requestBody.put("registryDetails", registryDetails);
        // POST request to create an entity type with empty fields
        Response response = given()
                .header("Authorization", internalAccessToken)
                .header("internal-access-token", internalAccessToken)
                .header("x-auth-token", X_AUTH_TOKEN)
                .header("Content-Type", "application/json")
                .body(requestBody)
                .post(PropertyLoader.PROP_LIST.getProperty("elevate.qa.entitytypesinglecreate.endpoint"));
        response.prettyPrint();
        // Validating the response
        Assert.assertEquals(response.getStatusCode(), 400, "The name field cannot be empty.");
        Assert.assertEquals(response.jsonPath().getString("message"), "[[location:body, param:name, msg:The name field cannot be empty., value:]]");
        logger.info("Validations with empty request fields are verified.");
    }
}