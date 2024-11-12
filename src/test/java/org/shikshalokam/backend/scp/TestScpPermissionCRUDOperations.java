package org.shikshalokam.backend.scp;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.MentorBase;
import org.testng.annotations.Test;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.testng.Assert;

import java.net.URI;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;
import static org.shikshalokam.backend.PropertyLoader.PROP_LIST;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.BeforeMethod;


public class TestScpPermissionCRUDOperations extends SelfCreationPortalBaseTest {
    private static final Logger logger = LogManager.getLogger(TestScpPermissionCRUDOperations.class);
    private URI createPermissionEndpoint, updatePermissionEndpoint, permissionListApiEndpoint, deletePermissionEndpoint, listPermissionsByRoleEndpoint;
    private int createdId;

    @BeforeMethod
    public void init() {
        logger.info("Logging into the application:");

        // Make login request to retrieve the token
        loginToScp(PROP_LIST.get("scp.qa.admin.login.user").toString(), PROP_LIST.get("scp.qa.admin.login.password").toString());

        // Initialize the createPermissionEndpoint URI
        try {
            createPermissionEndpoint = new URI(PROP_LIST.get("scp.create.permission.endpoint").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for createPermissionEndpoint", e);
        }
    }

    @Test(priority = 1, description = "Verifies the functionality of creating new user's permission with valid payload.")
    public void testCreatePermissionWithValidPayload() {
        logger.info("Started calling the CreatePermission API with valid payload:");

        // Call createPermission with valid parameters
        Response response = createPermission("project_", "permissions", "POST", "/scp/v1/projects/create", "ACTIVE");

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 201
        assertEquals(statusCode, 201, "Status code should be 201");

        // Fetch ID from response and validate
        createdId = response.jsonPath().getInt("result.id");
        Assert.assertNotNull(createdId, "Created ID should not be null");
        logger.info("Created ID: " + createdId);

        logger.info("Ended calling the CreatePermission API with valid payload.");
    }

    @Test(description = "Verifies the functionality of permission creation when the payload contains invalid payload")
    public void testCreatePermissionWithInvalidPayload() {
        logger.info("Started calling the CreatePermission API with invalid payload:");

        // Call createPermission with invalid parameters
        Response response = createPermission("project_", "invalidModule", "INVALID", "/scp/invalid/api", "INVALID");

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 400 or 422 for invalid request
        Assert.assertTrue(statusCode == 400 || statusCode == 422, "Status code should be 400 or 422 for invalid payload");

        logger.info("Ended calling the CreatePermission API with invalid payload.");
    }

    @Test(priority = 3, description = "Verifies the functionality of permission creation with empty fields payload")
    public void testCreatePermissionWithEmptyFields() {
        logger.info("Started calling the CreatePermission API with empty fields payload:");

        // Call createPermission with empty fields
        Response response = createPermission("", "", "", "", "");

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code for empty fields (usually 400)
        assertEquals(statusCode, 400, "Status code should be 400 for empty fields payload");

        logger.info("Ended calling the CreatePermission API with empty fields payload.");
    }

    @Test(priority = 2, dependsOnMethods = "testCreatePermissionWithValidPayload", description = "Verifies the functionality of updating user's permission with valid payload.")
    public void testUpdatePermissionWithValidPayload() {
        logger.info("Started calling the UpdatePermission API with valid payload:");

        JSONObject requestBody = createRequestBodyForUpdate("updated_project_", "projects", "POST", "/scp/v1/projects/create", "ACTIVE");

        // Call updatePermission with the created requestBody
        Response response = updatePermission(requestBody);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 201 for a successful update
        assertEquals(statusCode, 201, "Status code should be 201");

        logger.info("Ended calling the UpdatePermission API with valid payload.");
    }

    @Test(dependsOnMethods = "testCreatePermissionWithInvalidPayload", priority = 5, description = "Verifies the functionality of updating user's permission with Invalid payload.")
    public void testUpdatePermissionWithInvalidPayload() {
        logger.info("Started calling the UpdatePermission API with Invalid payload:");

        JSONObject requestBody = createRequestBodyForUpdate("project_", "invalidModule", "INVALID", "/scp/invalid/api", "INVALID");


        // Call updatePermission with the created requestBody
        Response response = updatePermission(requestBody);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 400 or 422 for invalid request
        Assert.assertTrue(statusCode == 400 || statusCode == 422, "Status code should be 400 or 422 for invalid payload");

        logger.info("Ended calling the UpdatePermission API with valid payload.");
    }

    @Test(dependsOnMethods = "testCreatePermissionWithEmptyFields", priority = 6, description = "Verifies the functionality of updating user's permission with Invalid payload.")
    public void testUpdatePermissionWithEmptyFieldsPayload() {
        logger.info("Started calling the UpdatePermission API with Invalid payload:");

        JSONObject requestBody = createRequestBodyForUpdate("", "", "", "", "");


        // Call updatePermission with the created requestBody
        Response response = updatePermission(requestBody);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code for empty fields (usually 400)
        assertEquals(statusCode, 400, "Status code should be 400 for empty fields payload");

        logger.info("Ended calling the UpdatePermission API with valid payload.");
    }

    @Test(priority = 7, description = "Verifies the getPermissions API for a valid user.")
    public void testGetPermissionsWithValidPayload() {
        logger.info("Started calling the GetPermissions API with valid token.");

        Response response = getPermissions(true);
        response.prettyPrint();

        // Validate response code
        assertEquals(response.getStatusCode(), 200, "Failed to fetch permissions.");
        logger.info("Permissions retrieved successfully.");
    }

    @Test(priority = 8, description = "Verifies the getPermissions API with an invalid token.")
    public void testGetPermissionsApiWithInvalidToken() {
        logger.info("Started calling the GetPermissions API with an invalid token.");

        Response response = getPermissions(false);

        // Validate response code for invalid token
        assertEquals(response.getStatusCode(), 401, "Expected 401 Unauthorized error.");
        logger.info("Received expected error for invalid token.");
    }

    @Test(priority = 10, dependsOnMethods = "testCreatePermissionWithValidPayload", description = "Verifies the functionality of deleting user's permission by ID.")
    public void testUpdatedPermissionDeletionById() {
        logger.info("Started calling the DeletePermission API with valid ID.");

        Response response = deletePermissionById();

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 202 for a successful deletion
        Assert.assertEquals(statusCode, 202, "Status code should be 202 for successful deletion");

        logger.info("Ended calling the DeletePermission API with valid ID.");
    }

    @Test(priority = 9, description = "Verifies the functionality of retrieving permissions list based on a specific role.")
    public void testGetListPermissionsByRole() {
        logger.info("Started calling the GetListPermissionsByRole API with a valid role.");

        // Call getListPermissionsByRole with a specific role
        Response response = getListPermissionsByRole();

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 200 for a successful retrieval
        assertEquals(statusCode, 200, "Status code should be 200 for valid role.");

        logger.info("Ended calling the GetPermissionsByRole API with a valid role.");
    }

    // Method to create a permission
    private Response createPermission(String prefix, String module, String requestType, String apiPath, String status) {
        JSONObject requestBody = new JSONObject();

        // Generate random alphabetic string for "code"
        requestBody.put("code", prefix + RandomStringUtils.randomAlphabetic(8).toLowerCase());
        requestBody.put("module", module);
        JSONArray requestTypeArray = new JSONArray();
        requestTypeArray.add(requestType);
        requestBody.put("request_type", requestTypeArray);
        requestBody.put("api_path", apiPath);
        requestBody.put("status", status);

        // Make the POST request
        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .log().all()
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when().post(createPermissionEndpoint);

        return response;
    }


    // Method to create request body for update permission
    private JSONObject createRequestBodyForUpdate(String codePrefix, String module, String requestType, String apiPath, String status) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("code", codePrefix + RandomStringUtils.randomAlphabetic(8).toLowerCase());
        requestBody.put("module", module);
        JSONArray requestTypeArray = new JSONArray();
        requestTypeArray.add(requestType);
        requestBody.put("request_type", requestTypeArray);
        requestBody.put("api_path", apiPath);
        requestBody.put("status", status);

        return requestBody;
    }

    private Response updatePermission(JSONObject requestBody) {
        updatePermissionEndpoint = MentorBase.createURI(PROP_LIST.get("scp.update.permission.endpoint").toString());
        // Make the POST request to update the permission
        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .log().all()
                .pathParams("id", createdId)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when().post(updatePermissionEndpoint + "/{id}");

        return response;
    }

    // Method to handle the getPermissions API request
    private Response getPermissions(Boolean validToken) {
        String temp_X_AUTH_TOKEN = X_AUTH_TOKEN;
        Response local = null;
        try {
            if (!validToken) {
                X_AUTH_TOKEN = "junk";
            }


            permissionListApiEndpoint = new URI(PROP_LIST.get("scp.qa.permission.list").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for getPermissionEndpoint", e);
        }

        local = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .get(permissionListApiEndpoint)
                .then()
                .extract().response();
        // Make the GET request without a request body
        X_AUTH_TOKEN = temp_X_AUTH_TOKEN;
        return local;
    }

    // Method to delete permission by ID
    private Response deletePermissionById() {
        deletePermissionEndpoint = MentorBase.createURI(PROP_LIST.get("scp.delete.permission.endpoint").toString());

        return given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .pathParams("id",createdId)
                .contentType(ContentType.JSON)
                .when().delete(deletePermissionEndpoint + "{id}")
                .then()
                .extract().response();
    }

    // Method to get listPermission By Role
    private Response getListPermissionsByRole() {
        try {
            // Construct the endpoint URI
            listPermissionsByRoleEndpoint = new URI(PROP_LIST.get("scp.qa.listPermissionByRole.endpoint").toString());

            // Make the GET request with role as a query parameter
            return given()
                    .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                    .contentType(ContentType.JSON)
                    .when()
                    .get(listPermissionsByRoleEndpoint)
                    .then()
                    .extract().response();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for listPermissionsByRoleEndpoint", e);
        }
    }
}
