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
    }

    @Test(description = "Verifies the functionality of creating new user's permission with valid payload.")
    public void testCreatePermissionWithValidPayload() {
        logger.info("Started calling the CreatePermission API:");
        try {
            RestAssured.baseURI = PropertyLoader.PROP_LIST.get("scp.qa.api.base.url").toString();
            createPermissionEndpoint = new URI(PROP_LIST.get("scp.create.permission.endpoint").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Updated request body
        String requestBody = "{ " +
                "\"code\": \"project_test_break\", " +
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

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        logger.info("Response Status Code: " + statusCode);
        logger.info("Response Body: " + responseBody);
        // Pretty print the response to the console or logs
        response.prettyPrint();

        //log the end call for createPermission API
        logger.info("Ended calling the CreatePermission API with valid payload.");

    }

    @Test(description = "Verifies the functionality of permission creation when the payload contains invalid payload")
    public void testCreatePermissionWithInvalidPayload() {
        logger.info("Started calling the CreatePermission API:");
        try {
            RestAssured.baseURI = PropertyLoader.PROP_LIST.get("scp.qa.api.base.url").toString();
            createPermissionEndpoint = new URI(PROP_LIST.get("scp.create.permission.endpoint").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Updated request body
        String requestBody = "{ " +
                "\"code\": \"project_test_break\", " +
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

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        logger.info("Response Status Code: " + statusCode);
        logger.info("Response Body: " + responseBody);
        // Pretty print the response to the console or logs
        response.prettyPrint();

        //log the end call for createPermission API
        logger.info("Ended calling the CreatePermission API with invalid payload.");

    }

    @Test(description = "Verifies the functionality of permission creation when the payload contains empty fields")
    public void testCreatePermissionWithEmptyFields() {
        logger.info("Started calling the CreatePermission API:");
        try {
            RestAssured.baseURI = PropertyLoader.PROP_LIST.get("scp.qa.api.base.url").toString();
            createPermissionEndpoint = new URI(PROP_LIST.get("scp.create.permission.endpoint").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Updated request body
        String requestBody = "{ " +
                "\"code\": \" \", " +
                "\"module\": \" \", " +
                "\"request_type\": [\" \"], " +
                "\"api_path\": \" \", " +
                "\"status\": \" \" " +
                "}";

        // Use the extracted internalAccessToken in the request header
        Response response = given()
                .header("X-auth-token", "bearer " + internalAccessToken)  // Using the extracted token
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post(createPermissionEndpoint);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        logger.info("Response Status Code: " + statusCode);
        logger.info("Response Body: " + responseBody);
        // Pretty print the response to the console or logs
        response.prettyPrint();

        //log the end call for createPermission API
        logger.info("Ended calling the CreatePermission API with empty fields payload.");

    }

}
