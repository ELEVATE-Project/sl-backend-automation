package org.shikshalokam.backend.scp;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.json.simple.JSONObject;
import org.testng.Assert;

import java.util.HashMap;
import java.util.Map;

import java.net.URI;
import java.net.URISyntaxException;


import static io.restassured.RestAssured.given;
import static org.shikshalokam.backend.PropertyLoader.PROP_LIST;

public class TestScpEntityTypesAndEntitiesCRUDOperations extends SelfCreationPortalBaseTest {
    private static final Logger logger = LogManager.getLogger(TestScpEntityTypesAndEntitiesCRUDOperations.class);
    private URI entityTypeCreateEndpoint, entityTypeUpdatePermissionEndpoint, entityTypeReadEndpoint, entityTypeDeleteEndpoint, entityCreateEndpoint;
    private int createdId;
    private Map<String, String> map;
    private String entityTypeValue;

    @BeforeTest
    public void init() {
        logger.info("Logging into the application:");

        // Make login request to retrieve the token
        loginToScp(PROP_LIST.get("scp.qa.admin.login.user").toString(), PROP_LIST.get("scp.qa.admin.login.password").toString());

        // Initialize the entityTypeCreateEndpoint URI
        try {
            entityTypeCreateEndpoint = new URI(PROP_LIST.get("scp.create.entitytype.endpoint").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for createEntityTypeEndpoint", e);
        }
    }

    @Test(description = "Verifies the functionality of creating new entityType with valid payload.")
    public void testCreateEntityTypeWithValidPayload() {
        logger.info("Started calling the CreateEntityType API with valid payload:");

        // Call entityTypeCreation with valid parameters
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("value", "entityTypeValue" + RandomStringUtils.randomAlphabetic(8).toLowerCase());
        requestBody.put("label", "entityTypeLabel" + RandomStringUtils.randomAlphabetic(8).toLowerCase());
        requestBody.put("status", "ACTIVE");
        requestBody.put("type", "SYSTEM");
        requestBody.put("data_type", "STRING");
        requestBody.put("has_entities", "true");

        JSONObject createRequestBody = createEntityType(requestBody);

        // Call updatePermission with the created requestBody
        Response response = createEntityTypeRequest(createRequestBody);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 201
        Assert.assertEquals(statusCode, 201, "Status code should be 201");

        // Fetch ID from response and validate
        createdId = response.jsonPath().getInt("result.id");
        Assert.assertNotNull(createdId, "Created ID should not be null");
        logger.info("Created ID: " + createdId);

        logger.info("Ended calling the EntityTypeCreation API with valid payload.");
    }

    @Test(description = "Verifies the functionality of creating new entityType with Invalid payload.")
    public void testCreateEntityTypeWithInValidPayload() {
        logger.info("Started calling the CreateEntityType API with Invalid payload:");

        // Call entityTypeCreation with valid parameters
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("value", "WEFW_@$#@");
        requestBody.put("label", "_Label_");
        requestBody.put("status", "INACTIVE");
        requestBody.put("type", "IT_SYSTEM");
        requestBody.put("data_type", "STRING_ARRAY");
        requestBody.put("has_entities", "false");

        JSONObject createRequestBody = createEntityType(requestBody);
        // Call updatePermission with the created requestBody
        Response response = createEntityTypeRequest(createRequestBody);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 400 or 422 for invalid request
        Assert.assertTrue(statusCode == 400 || statusCode == 422, "Status code should be 400 or 422 for invalid payload");

        logger.info("Ended calling the EntityTypeCreation API with valid payload.");
    }

    @Test(description = "Verifies the functionality of creating new entityType with empty fields payload.")
    public void testCreateEntityTypeWithEmptyFields() {
        logger.info("Started calling the CreatePermission API with empty fields payload:");

        // Call entityTypeCreation with valid parameters
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("value", "");
        requestBody.put("label", "");
        requestBody.put("status", "");
        requestBody.put("type", "");
        requestBody.put("data_type", "");
        requestBody.put("has_entities", "");

        JSONObject createRequestBody = createEntityType(requestBody);
        // Call updatePermission with the created requestBody
        Response response = createEntityTypeRequest(createRequestBody);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code for empty fields (usually 400)
        Assert.assertEquals(statusCode, 400, "Status code should be 400 for empty fields payload");

        logger.info("Ended calling the EntityTypeCreation API with valid payload.");
    }

    @Test(dependsOnMethods = "testCreateEntityTypeWithValidPayload", description = "Verifies the functionality of updating entity types with valid payload.")
    public void testUpdateEntityTypesWithValidPayload() {
        logger.info("Started calling the update entity types API with valid payload:");

        // Call entityTypeUpdation with valid parameters
        Map<String, String> requestBody = new HashMap<>();
        entityTypeValue = "updatedentitytypevalue" + RandomStringUtils.randomAlphabetic(8).toLowerCase(); // Generate entityTypeValue
        requestBody.put("value", entityTypeValue);
        requestBody.put("label", "updatedentityTypeLabel" + RandomStringUtils.randomAlphabetic(8).toLowerCase());
        requestBody.put("status", "ACTIVE");
        requestBody.put("type", "SYSTEM");
        requestBody.put("allow_filtering", "false");
        requestBody.put("data_type", "string");

        JSONObject updatedRequestBody = entityTypeForUpdate(requestBody);

        // Call updatePermission with the created requestBody
        Response response = entityTypeUpdateRequest(updatedRequestBody);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 201 for a successful update
        Assert.assertEquals(statusCode, 202, "Status code should be 202");

        logger.info("Ended calling the update entity types API with valid payload.");
    }

    @Test(dependsOnMethods = "testCreateEntityTypeWithInValidPayload", description = "Verifies the functionality of updating entity types with Invalid payload.")
    public void testUpdateEntityTypesWithInvalidPayload() {
        logger.info("Started calling the entity types API with Invalid payload:");

        // Call entityTypeUpdation with invalid parameters
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("value", "updatedentity#@$TypeValue_");
        requestBody.put("label", "_updatedentityTypeLabel");
        requestBody.put("status", "INACTIVE");
        requestBody.put("type", "SYSTEM_ADMIN");
        requestBody.put("allow_filtering", "true");
        requestBody.put("data_type", "string-array");

        JSONObject updatedRequestBody = entityTypeForUpdate(requestBody);

        // Call updateEntityType with the created requestBody
        Response response = entityTypeUpdateRequest(updatedRequestBody);


        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 400 or 422 for invalid request
        Assert.assertTrue(statusCode == 400 || statusCode == 422, "Status code should be 400 or 422 for invalid payload");

        logger.info("Ended calling the entity types API with valid payload.");
    }

    @Test(description = "Verifies the functionality of updating entity types with empty fields payload.")
    public void testUpdateEntityTypesWithEmptyFields() {
        logger.info("Started calling the entity types API with empty fields payload:");

        // Call entityTypeUpdation with valid parameters
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("value", "");
        requestBody.put("label", "");
        requestBody.put("status", "");
        requestBody.put("type", "");
        requestBody.put("allow_filtering", "");
        requestBody.put("data_type", "");

        JSONObject updatedRequestBody = entityTypeForUpdate(requestBody);

        // Call updatePermission with the created requestBody
        Response response = entityTypeUpdateRequest(updatedRequestBody);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code for empty fields (usually 400)
        Assert.assertEquals(statusCode, 400, "Status code should be 400 for empty fields payload");

        logger.info("Ended calling the entity types API with empty fields.");
    }

    @Test(dependsOnMethods = "testUpdateEntityTypesWithValidPayload", description = "Verifies the functionality of reading entity types with valid payload.")
    public void testReadEntityTypesWithValidPayload() {
        logger.info("Started calling the read entity types API with valid payload:");

        // Ensure that entityTypeValue is set from the previous test
        JSONObject readRequestBody = entityTypeForRead(entityTypeValue.toLowerCase());

        Response response = entityTypeReadRequest(readRequestBody);

        int statusCode = response.getStatusCode();
        response.prettyPrint();
        Assert.assertEquals(statusCode, 200, "Status code should be 200");

        logger.info("Ended calling the read entity types API with valid payload.");
    }

    @Test(dependsOnMethods = "testReadEntityTypesWithValidPayload", description = "Verifies the functionality of deleting entity types with valid payload.")
    public void testDeleteEntityTypesWithValidPayload() {
        logger.info("Started calling the delete entity types API with valid payload:");

        // Call delete entityType with the created requestBody
        Response response = entityTypeDeleteRequest();

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 202 for a successful update
        Assert.assertEquals(statusCode, 202, "Status code should be 202");
        response = entityTypeDeleteRequest();
        Assert.assertEquals(response.getStatusCode(), 400, "Entity type not found");

        logger.info("Ended calling the delete entity types API with valid payload.");
    }

    @Test(dependsOnMethods = "testCreateEntityTypeWithValidPayload", description = "Verifies the functionality of creating new entity with valid payload.")
    public void testCreateEntityWithValidPayload() {
        logger.info("Started calling the CreateEntity API with valid payload:");

        // Call entityCreation with valid parameters
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("value", "entityValue" + RandomStringUtils.randomAlphabetic(8).toLowerCase());
        requestBody.put("label", "entityLabel" + RandomStringUtils.randomAlphabetic(8).toLowerCase());
        requestBody.put("status", "ACTIVE");
        requestBody.put("type", "SYSTEM");
        requestBody.put("entity_type_id", String.valueOf(createdId));

        JSONObject createRequestBody = createEntity(requestBody);

        // Call updatePermission with the created requestBody
        Response response = createEntityRequest(createRequestBody);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 201
        Assert.assertEquals(statusCode, 201, "Status code should be 201");

        logger.info("Ended calling the EntityCreation API with valid payload.");
    }
    @Test(description = "Verifies the functionality of creating new entity with valid payload.")
    public void testCreateEntityWithInValidPayload() {
        logger.info("Started calling the CreateEntity API with invalid payload:");

        // Call entityCreation with valid parameters
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("value", "$@#@!#@!");
        requestBody.put("label", "R#RW@@@@@@@@@@");
        requestBody.put("status", "INACTIVE");
        requestBody.put("type", "SYSTEM_ADMIN");
        requestBody.put("entity_type_id", String.valueOf("createdID"));

        JSONObject createRequestBody = createEntity(requestBody);

        // Call updatePermission with the created requestBody
        Response response = createEntityRequest(createRequestBody);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 201
        // Validate response code is 400 or 422 for invalid request
        Assert.assertTrue(statusCode == 400 || statusCode == 422, "Status code should be 400 or 422 for invalid payload");

        logger.info("Ended calling the EntityCreation API with invalid payload.");
    }
    @Test(description = "Verifies the functionality of creating new entity with valid payload.")
    public void testCreateEntityWithEmptyFields() {
        logger.info("Started calling the CreateEntity API with empty fields payload:");

        // Call entityCreation with valid parameters
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("value", "");
        requestBody.put("label", "");
        requestBody.put("status", "");
        requestBody.put("type", "");
        requestBody.put("entity_type_id", "");

        JSONObject createRequestBody = createEntity(requestBody);

        // Call updatePermission with the created requestBody
        Response response = createEntityRequest(createRequestBody);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code for empty fields (usually 400)
        Assert.assertEquals(statusCode, 400, "Status code should be 400 for empty fields payload");

        logger.info("Ended calling the EntityCreation API with empty fields payload.");
    }


    //Method to create request body for entityTypes
    private JSONObject createEntityType(Map<String, String> map) {
        JSONObject requestBody = new JSONObject();
        requestBody.putAll(map);
        return requestBody;
    }

    private Response createEntityTypeRequest(JSONObject requestBody) {
        try {
            entityTypeCreateEndpoint = new URI(PROP_LIST.get("scp.create.entitytype.endpoint").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for entityTypeCreatEndpoint", e);
        }

        // Make the POST request to update the permission
        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when().post(entityTypeCreateEndpoint);

        // Pretty-print the response for debugging
        response.prettyPrint();

        return response;
    }

    // Method to create request body for update entityTypes
    private JSONObject entityTypeForUpdate(Map<String, String> map) {
        JSONObject requestBody = new JSONObject();
        requestBody.putAll(map);
        return requestBody;
    }

    private Response entityTypeUpdateRequest(JSONObject requestBody) {
        try {
            entityTypeUpdatePermissionEndpoint = new URI(PROP_LIST.get("scp.update.entitytype.endpoint").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for updateEntityTypeEndpoint", e);
        }
        // Make the POST request to update the permission
        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .pathParams("id", createdId)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when().post(entityTypeUpdatePermissionEndpoint + "{id}");

        // Pretty-print the response for debugging
        response.prettyPrint();

        return response;
    }

    // Method to create request body for read entityTypes
    private JSONObject entityTypeForRead(String value) {
        JSONObject requestBody = new JSONObject();
        JSONArray valueArray = new JSONArray();
        valueArray.add(value);
        requestBody.put("value", valueArray);
        requestBody.put("read_user_entity", false);

        // Log the request body
        logger.info("Request Body for read entity types: " + requestBody.toJSONString());

        return requestBody;
    }

    private Response entityTypeReadRequest(JSONObject requestBody) {
        try {
            entityTypeReadEndpoint = new URI(PROP_LIST.get("scp.read.entitytype.endpoint").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for readEntityTypeEndpoint", e);
        }

        // Make the POST request to read the entityTypes
        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when().post(entityTypeReadEndpoint);

        // Pretty-print the response for debugging
        response.prettyPrint();

        return response;
    }

    private Response entityTypeDeleteRequest() {
        try {
            entityTypeDeleteEndpoint = new URI(PROP_LIST.get("scp.delete.entitytype.endpoint").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for deleteEntityTypeEndpoint", e);
        }

        // Make the DELETE request to read the entityTypes
        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .pathParams("id", createdId)
                .contentType(ContentType.JSON)
                .when().delete(entityTypeDeleteEndpoint + "{id}");

        // Pretty-print the response for debugging
        response.prettyPrint();

        return response;
    }
    //Method to create request body for entityTypes
    private JSONObject createEntity(Map<String, String> map) {
        JSONObject requestBody = new JSONObject();
        requestBody.putAll(map);
        return requestBody;
    }

    private Response createEntityRequest(JSONObject requestBody) {
        try {
            entityCreateEndpoint = new URI(PROP_LIST.get("scp.create.entity.endpoint").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for entityCreatEndpoint", e);
        }

        // Make the POST request to update the permission
        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when().post(entityCreateEndpoint);

        // Pretty-print the response for debugging
        response.prettyPrint();

        return response;
    }
}