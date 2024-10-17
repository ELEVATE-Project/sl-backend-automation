package org.shikshalokam.backend.ep;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.shikshalokam.backend.PropertyLoader;
import org.testng.Assert;
import org.testng.annotations.Test;

// Validating user role extension create scenarios
public class TestUserRoleExtensionCRUDoperations extends ElevateProjectBaseTest {
    static final Logger logger = LogManager.getLogger(TestUserRoleExtensionCRUDoperations.class);

    @Test(description = "Validating user role extension creation and verification with find API")
    public void testUserRoleExtensionCreateAndVerify() {
        ElevateProjectBaseTest.loginToElevate(PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator"), PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator.password"));

        // Create the JSON payload using JSONObject
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("userRoleId", "210");
        jsonBody.put("title", "DEO");

        // Create an array for entityTypes
        JSONArray entityTypesArray = new JSONArray();
        JSONObject entityTypeObject = new JSONObject();
        entityTypeObject.put("entityType", "State");
        entityTypeObject.put("entityTypeId", "6672ce0fc05aa58f89ba12f1");
        entityTypesArray.add(entityTypeObject);

        // Add the entityTypes array to the jsonBody
        jsonBody.put("entityTypes", entityTypesArray);

        String validPayload = jsonBody.toString();
        logger.info("Request Payload: {}", validPayload);

        // Call the createUserRoleExtension API
        Response createResponse = createUserRoleExtension(validPayload);
        if (createResponse != null) {
            createResponse.prettyPrint();
            Assert.assertEquals(createResponse.getStatusCode(), 200, "User role successfully created.");
            Assert.assertEquals(createResponse.jsonPath().getString("message"), "USER_ROLE_INFORMATION_CREATED");

            // Extract the ID of the newly created user role
            String createdUserRoleId = createResponse.jsonPath().getString("result._id");
            logger.info("Created User Role ID: {}", createdUserRoleId);

            // Verify using the findUserRoleExtension API
            String validFindQuery = "{ \"query\": { \"_id\": \"" + createdUserRoleId + "\" }, \"projection\": [\"all\"] }";
            Response findResponse = findUserRoleExtension(validFindQuery);

            if (findResponse != null) {
                findResponse.prettyPrint();
                Assert.assertEquals(findResponse.getStatusCode(), 200, "User role found successfully.");
                logger.info("User role found successfully.");
                Assert.assertEquals(findResponse.jsonPath().getString("result[0]._id"), createdUserRoleId, "User role extension ID matches.");
                logger.info("User role extension verification is successful.");

            } else {
                Assert.fail("Response is null, failed to find the user role.");
            }
        } else {
            // Fail the test if the response is null
            Assert.fail("Response is null, user role extension creation failed.");
        }
    }


    // Define the createUserRoleExtension method
    public Response createUserRoleExtension(String payload) {
        String endpoint = PropertyLoader.PROP_LIST.getProperty("elevate.createuserroleext.endpoint");
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("X-auth-token", X_AUTH_TOKEN) // Use the token for authentication
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .body(payload)
                .post(endpoint);
        return response;
    }


    // Define the findUserRoleExtension method
    public Response findUserRoleExtension(String queryPayload) {
        String endpoint = PropertyLoader.PROP_LIST.getProperty("elevate.finduserroleext.endpoint");
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("X-auth-token", X_AUTH_TOKEN) // Use the token for authentication
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .body(queryPayload)
                .post(endpoint);
        return response;
    }
}
