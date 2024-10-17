package org.shikshalokam.backend.ep;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URI;

// Validating user role extension create scenarios
public class TestUserRoleExtensionCreate extends ElevateProjectBaseTest {
    static final Logger logger = LogManager.getLogger(TestUserRoleExtensionCreate.class);
    private String USER_ROLE_CREATE_ENDPOINT = PropertyLoader.PROP_LIST.getProperty("elevate.createuserroleext.endpoint");
    public static String BASE_URL = PropertyLoader.PROP_LIST.getProperty("elevate.qa.api.base.url");
    public static String INTERNAL_ACCESS_TOKEN = PropertyLoader.PROP_LIST.getProperty("elevate.internalaccesstoken");

    @Test(description = "Validating user role extension creation with valid payload")
    public void testUserRoleCreateWithValidPayload() {
        ElevateProjectBaseTest.loginToElevate(PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator"), PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator.password"));
        String validPayload = "{\"userRoleId\": \"114\", " +
                "\"title\": \"HTrTTe\", " +
                "\"entityTypes\": [{\"entityType\": \"state\", " +
                "\"entityTypeId\": \"6672ce0fc05aa58f89ba12f1\"}]}";

        // Taking the response from the user role creation API using method calling
        Response response = createUserRoleExtension(validPayload);
        if (response != null) {
            response.prettyPrint();
            Assert.assertEquals(response.getStatusCode(), 200, "User role successfully created.");
            Assert.assertEquals(response.jsonPath().getString("message"), "USER_ROLE_INFORMATION_CREATED");
            String userId = response.jsonPath().getString("result._id");
            System.out.println("result._id: " + userId);
            // Logging success
            logger.info("User role extension creation with valid payload is verified");
        } else {
            // Fail the test if the response is null
            Assert.fail("Response is null, user role extension creation failed.");
        }
    }

    @Test(description = "Validating user role extension creation with invalid payload")
    public void testUserRoleCreateWithInvalidPayload() {
        ElevateProjectBaseTest.loginToElevate("manju@guerrillamail.info", "Password@123");
        String invalidPayload = "{ \"userRoleId\": \" $#@ \"," +
                " \"title\": \"  RRR6RR\"," +
                " \"entityTypes\": [ { " +
                "\"entityType\": \"sta\", " +
                "\"entityTypeId\": \"6672ce0fc05aa58f89ba12f1\" } ] }";
        // Invalid entityTypeId
        ElevateProjectBaseTest.response = createUserRoleExtension(invalidPayload);
        ElevateProjectBaseTest.response.prettyPrint();
        Assert.assertEquals(ElevateProjectBaseTest.response.getStatusCode(), 400, "Invalid user role extension creation failed as expected.");
        String message = ElevateProjectBaseTest.response.jsonPath().getString("message[0]");
        Assert.assertNotNull(message, "Error message is present");
        Assert.assertFalse(message.contains("Invalid input"), "Error message contains 'Invalid input'");
        logger.info("User role extension creation with invalid payload is verified");
    }

    @Test(description = "Validating user role extension creation with empty fields")
    public void testUserRoleCreateWithEmptyFields() {
        ElevateProjectBaseTest.loginToElevate("manju@guerrillamail.info", "Password@123");
        String emptyPayload = "{}"; // Empty payload
        ElevateProjectBaseTest.response = createUserRoleExtension(emptyPayload);
        ElevateProjectBaseTest.response.prettyPrint();
        Assert.assertEquals(ElevateProjectBaseTest.response.getStatusCode(), 400, "Payload with empty fields failed as expected.");
        String message = ElevateProjectBaseTest.response.jsonPath().getString("message[0]");
        Assert.assertNotNull(message, "Error message is present");
        Assert.assertFalse(message.contains("Invalid input"), "Error message contains 'Invalid input'");
        logger.info("User role extension creation with empty fields is verified");
    }

    // Helper method to create user role extension
    private Response createUserRoleExtension(String payload) {
        String url = BASE_URL + USER_ROLE_CREATE_ENDPOINT;
        try {
            logger.info("Request payload: " + payload); // Log request details
            // Perform request to create user role extension
            Response response = RestAssured.given()
                    .header("X-auth-token", X_AUTH_TOKEN) // Use the token for authentication
                    .header("Content-Type", "application/json")
                    .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                    .body(payload)
                    .post(new URI(url));
            logger.info("Response status code: " + response.getStatusCode());
            logger.info("Response body: " + response.getBody().asString());
            return response;

        } catch (Exception e) {
            logger.error("Exception during user role extension creation: " + e.getMessage());
            return null;
        }
    }
}
