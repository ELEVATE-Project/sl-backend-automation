package org.shikshalokam.backend.scp;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;
import static org.shikshalokam.backend.PropertyLoader.PROP_LIST;

public class TestScpGetListPermission extends SelfCreationPortalBaseTest {

    private static final Logger logger = LogManager.getLogger(TestScpGetListPermission.class);
    private URI permissionListApiEndpoint;

    @BeforeTest
    public void setUp() {
        logger.info("Logging into the application:");

        // Login handled by parent class method
        loginToScp(PROP_LIST.get("scp.qa.admin.login.user").toString(),
                PROP_LIST.get("scp.qa.admin.login.password").toString());

        try {
            // Initialize permissionListApiEndpoint
            permissionListApiEndpoint = new URI(PROP_LIST.get("scp.qa.permission.list").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for permissionListApiEndpoint", e);
        }
    }

    @Test(description = "Verifies the getPermissions API for a valid user.")
    public void testGetPermissionsApi() {
        logger.info("Started calling the GetPermissions API with valid token.");

        Response response = getPermissions(X_AUTH_TOKEN);

        // Validate response code
        Assert.assertEquals(response.getStatusCode(), 200, "Failed to fetch permissions.");
        logger.info("Permissions retrieved successfully.");
    }

    @Test(description = "Verifies the getPermissions API with an invalid token.")
    public void testGetPermissionsApiWithInvalidToken() {
        logger.info("Started calling the GetPermissions API with an invalid token.");

        Response response = getPermissions("invalid token");

        // Validate response code for invalid token
        Assert.assertEquals(response.getStatusCode(), 401, "Expected 401 Unauthorized error.");
        logger.info("Received expected error for invalid token.");
    }

    // Method to handle the getPermissions API request
    private Response getPermissions(String token) {
        return given()
                .header("X-auth-token", "bearer " + token)
                .when()
                .get(permissionListApiEndpoint)
                .then()
                .extract().response();
    }
}
