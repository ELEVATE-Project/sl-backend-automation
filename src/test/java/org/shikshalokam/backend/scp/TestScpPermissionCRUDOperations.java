package org.shikshalokam.backend.scp;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.testng.Assert;

import java.net.URI;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;
import static org.shikshalokam.backend.PropertyLoader.PROP_LIST;

public class TestScpPermissionCRUDOperations extends SelfCreationPortalBaseTest {
    private static final Logger logger = LogManager.getLogger(TestScpPermissionCRUDOperations.class);
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
            createPermissionEndpoint = new URI(PROP_LIST.get("scp.create.permission.endpoint").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Create JSON object dynamically using JSONObject
        JSONObject requestBody = new JSONObject();
        String prefix = "project_"; // Define your desired prefix
        requestBody.put("code", generateRandomWord(prefix, 10));
        requestBody.put("module", "permissions");
        JSONArray requestTypeArray = new JSONArray();
        requestTypeArray.add("POST");
        requestBody.put("request_type", requestTypeArray);
        requestBody.put("api_path", "/scp/v1/projects/create");
        requestBody.put("status", "ACTIVE");

        // Use the extracted internalAccessToken in the request header
        Response response = given()
                .header("X-auth-token", "bearer " + internalAccessToken)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when().post(createPermissionEndpoint);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        logger.info("Response Status Code: " + statusCode);
        logger.info("Response Body: " + responseBody);

        // Pretty print the response to the console or logs
        response.prettyPrint();

        // Validate response code is 201
        Assert.assertEquals(statusCode, 201, "Status code should be 200");

        // Fetch ID from response and validate
        int createdId = response.jsonPath().getInt("result.id");
        Assert.assertNotNull(createdId, "Created ID should not be null");
        logger.info("Created ID: " + createdId);

        // Log the end call for createPermission API
        logger.info("Ended calling the CreatePermission API with valid payload.");
    }

    //Method to generate random word as code(Project name)
    private String generateRandomWord(String prefix, int length) {
        StringBuilder word = new StringBuilder(prefix); // Start with the prefix
        for (int i = 0; i < length; i++) {
            char letter = (char) ('a' + Math.random() * 26); // Random lowercase letter
            word.append(letter);
        }
        logger.info(word.toString());
        return word.toString();
    }

    @Test(description = "Verifies the functionality of permission creation when the payload contains invalid payload")
    public void testCreatePermissionWithInvalidPayload() {
        logger.info("Started calling the CreatePermission API:");
        try {
            createPermissionEndpoint = new URI(PROP_LIST.get("scp.create.permission.endpoint").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Create an invalid JSON object using JSONObject
        JSONObject requestBody = new JSONObject();
        requestBody.put("code", "project_test_break");
        requestBody.put("module", "projects");
        JSONArray requestTypeArray = new JSONArray();
        requestTypeArray.add("GET");
        requestBody.put("request_type", requestTypeArray);
        requestBody.put("api_path", "/scp/v1/projects/create\\");
        requestBody.put("status", "ACTIVE");

        // Use the extracted internalAccessToken in the request header
        Response response = given()
                .header("X-auth-token", "bearer " + internalAccessToken)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when().post(createPermissionEndpoint);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        logger.info("Response Status Code: " + statusCode);
        logger.info("Response Body: " + responseBody);

        // Validate response code is 400 or 422 for invalid request
        Assert.assertTrue(statusCode == 400 || statusCode == 422, "Status code should be 400 or 422 for invalid payload");

        // Log the end call for createPermission API
        logger.info("Ended calling the CreatePermission API with invalid payload.");
    }

    @Test(description = "Verifies the functionality of permission creation with empty fields payload")
    public void testCreatePermissionWithEmptyFields() {
        logger.info("Started calling the CreatePermission API:");
        try {
            createPermissionEndpoint = new URI(PROP_LIST.get("scp.create.permission.endpoint").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        // Create an empty JSON object using JSONObject
        JSONObject requestBody = new JSONObject();
        requestBody.put("code", " ");
        requestBody.put("module", " ");
        JSONArray requestTypeArray = new JSONArray();
        requestTypeArray.add(" ");
        requestBody.put("request_type", requestTypeArray);
        requestBody.put("api_path", " ");
        requestBody.put("status", " ");

        // Use the extracted internalAccessToken in the request header
        Response response = given()
                .header("X-auth-token", "bearer " + internalAccessToken)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when().post(createPermissionEndpoint);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        String responseBody = response.getBody().asString();
        logger.info("Response Status Code: " + statusCode);
        logger.info("Response Body: " + responseBody);

        // Pretty print the response to the console or logs
        response.prettyPrint();

        // Validate response code for empty fields (usually 400)
        Assert.assertEquals(statusCode, 400, "Status code should be 400 for empty fields payload");

        // Log the end call for createPermission API
        logger.info("Ended calling the CreatePermission API with empty fields payload.");
    }
}