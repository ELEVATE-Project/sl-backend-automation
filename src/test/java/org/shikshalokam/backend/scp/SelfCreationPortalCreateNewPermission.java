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
    public static String BASE_URL = PropertyLoader.PROP_LIST.getProperty("scp.qa.api.base.url");
    public static URI createPermissionEndpoint;
    public static Response response = null;

    @BeforeTest
    public void init() {
        logger.info("Logging into the application :");
        loginToScp(PROP_LIST.get("scp.qa.admin.login.user").toString(), PROP_LIST.get("scp.qa.admin.login.password").toString());
    }

    @Test(description = "Verifies the functionality of creating new user's permission.")
    public void testCreateNewPermission() {
        logger.info("Started calling the CreatePermission:");
        try {
            RestAssured.baseURI = PropertyLoader.PROP_LIST.get("scp.qa.api.base.url").toString();
            createPermissionEndpoint = new URI(PROP_LIST.get("scp.create.permission.endpoint").toString()); // Fixed: Using PROP_LIST for endpoint
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

        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)  // Using the auth token header
                .contentType(ContentType.JSON)  // JSON content type
                .body(requestBody)  // Updated request body
                .when().post(createPermissionEndpoint);  // POST request to the endpoint

        // Logging the response body
        logger.info(response.getBody().asString());

        logger.info("Ended calling the CreatePermission:");
    }

}
