package org.shikshalokam.backend.ep;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.List;

// Validating user role extension find scenarios
public class TestUserRoleExtensionFind extends ElevateProjectBaseTest {
    static final Logger logger = LogManager.getLogger(TestUserRoleExtensionFind.class);
    private String USER_ROLE_FIND_ENDPOINT = PropertyLoader.PROP_LIST.getProperty("elevate.finduserroleext.endpoint");
    public static String BASE_URL = PropertyLoader.PROP_LIST.getProperty("elevate.qa.api.base.url");
    public static String INTERNAL_ACCESS_TOKEN = PropertyLoader.PROP_LIST.getProperty("elevate.internalaccesstoken");

    @Test(description = "Validating user role extension find with valid query")
    public void testUserRoleFindWithValidPayload() {
        ElevateProjectBaseTest.loginToElevate(PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator"), PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator.password"));
        String validFindQuery = "{ \"query\": { \"status\": \"ACTIVE\" }, \"projection\": [\"all\"] }";
        // Taking the response from the user role find API using method calling
        Response response = findUserRoleExtension(validFindQuery);
        if (response != null) {
            response.prettyPrint();
            logger.info("Full Response: " + response.getBody().asString());
            Assert.assertEquals(response.getStatusCode(), 200, "User role extensions successfully found.");
            logger.info("User role extension find with valid query is verified");
        } else {
            // Fail the test if the response is null
            Assert.fail("Response is null, user role extension find failed.");
        }
    }

    @Test(description = "Validating user role extension find with invalid query")
    public void testUserRoleFindWithInvalidPayload() {
        ElevateProjectBaseTest.loginToElevate(PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator"), PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator.password"));
        String invalidFindQuery = "{ \"query\": { \"status\": \"INVALID_STATUS\" }, \"projection\": [\"all\"] }";
        ElevateProjectBaseTest.response = findUserRoleExtension(invalidFindQuery);
        ElevateProjectBaseTest.response.prettyPrint();
        Assert.assertEquals(ElevateProjectBaseTest.response.getStatusCode(), 404, "Invalid user role extension find failed as expected.");
        String message = ElevateProjectBaseTest.response.jsonPath().getString("message");
        Assert.assertEquals(message, "ROLES_NOT_FOUND", "Expected error message for invalid role.");
        logger.info("User role extension find with invalid query is verified");
    }

    @Test(description = "Validating user role extension find with empty query")
    public void testUserRoleFindWithEmptyFields() {
        ElevateProjectBaseTest.loginToElevate(PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator"),
                PropertyLoader.PROP_LIST.getProperty("elevate.login.contentcreator.password"));
        String emptyQuery = "{}"; // Empty query
        ElevateProjectBaseTest.response = findUserRoleExtension(emptyQuery);
        ElevateProjectBaseTest.response.prettyPrint();

        int statusCode = ElevateProjectBaseTest.response.getStatusCode();
        Assert.assertEquals(statusCode, 400, "Expected status code 400 for empty query, but got: " + statusCode);

        String message = ElevateProjectBaseTest.response.jsonPath().getString("message");
        Assert.assertNotNull(message, "Error message is present");
        logger.info("User role extension find with empty query is verified");
    }


    // Helper method to find user role extensions
    private Response findUserRoleExtension(String query) {
        String url = BASE_URL + USER_ROLE_FIND_ENDPOINT;
        try {
            logger.info("Request query: " + query); // Log request details
            // Perform request to find user role extensions
            Response response = RestAssured.given()
                    .header("X-auth-token", X_AUTH_TOKEN) // Use the token for authentication
                    .header("Content-Type", "application/json")
                    .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                    .body(query)
                    .post(new URI(url));
            logger.info("Response status code: " + response.getStatusCode());
            logger.info("Response body: " + response.getBody().asString());
            return response;

        } catch (Exception e) {
            logger.error("Exception during user role extension find: " + e.getMessage());
            return null;
        }
    }
}
