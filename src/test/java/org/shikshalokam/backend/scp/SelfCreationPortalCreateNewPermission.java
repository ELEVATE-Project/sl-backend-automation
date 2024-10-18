package org.shikshalokam.backend.scp;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.shikshalokam.backend.PropertyLoader;

import java.net.URI;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;
import static org.shikshalokam.backend.PropertyLoader.PROP_LIST;

public class SelfCreationPortalCreateNewPermission extends SelfCreationPortalBaseTest {
    private static final Logger logger = LogManager.getLogger(SelfCreationPortalCreateNewPermission.class);
    public static URI createPermissionEndpoint;
    public static String internalAccessToken;

    @BeforeTest
    public void init() {
        logger.info("Logging into the application:");

        // Make login request to retrieve the token

        loginToScp(PROP_LIST.get("scp.qa.admin.login.user").toString(), PROP_LIST.get("scp.qa.admin.login.password").toString());

        // Extract access token from the response
        internalAccessToken = response.body().jsonPath().get("result.access_token");

        // Log the extracted token
        logger.info("Internal Access Token: " + internalAccessToken);
        logger.info(response.prettyPrint());
    }

    @Test(description = "Verifies the functionality of creating new user's permission.")
    public void testCreateNewPermission() {
        logger.info("Started calling the CreatePermission API:");
        try {
            RestAssured.baseURI = PropertyLoader.PROP_LIST.get("scp.qa.api.base.url").toString();
            createPermissionEndpoint = new URI(PROP_LIST.get("scp.create.permission.endpoint").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Updated request body
        String requestBody = "{ " +
                "\"code\": \"project_subash_uat\", " +
                "\"module\": \"permissions\", " +
                "\"request_type\": [\"POST\"], " +
                "\"api_path\": \"/scp/v1/projects/create\", " +
                "\"status\": \"ACTIVE\" " +
                "}";

        // Use the extracted internalAccessToken in the request header
        Response response = given()
                .header("X-auth-token", "bearer " + internalAccessToken)  // Using the extracted token
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post(createPermissionEndpoint);

        // Logging the response body
        logger.info("Response Body: " + response.getBody().asString());
        logger.info("Response Status Code: " + response.getStatusCode());

        logger.info("Ended calling the CreatePermission API.");
    }
}
