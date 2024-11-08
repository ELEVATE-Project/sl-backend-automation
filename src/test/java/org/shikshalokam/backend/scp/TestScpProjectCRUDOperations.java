package org.shikshalokam.backend.scp;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.shikshalokam.backend.PropertyLoader.PROP_LIST;

public class TestScpProjectCRUDOperations extends SelfCreationPortalBaseTest {
    private static final Logger logger = LogManager.getLogger(TestScpProjectCRUDOperations.class);
    private URI projectCreateEndpoint, projectUpdateEndpoint, getProjectDetailsEndpoint, projectDeletionEndpoint;
    private Integer createdId;
    private String orgAdminToken;
    private String contentCreatorToken;

    @BeforeMethod
    public void setupForOrgAdmin() {
        logger.info("Logging in as Org Admin");
        orgAdminToken = String.valueOf(loginToScp(
                PROP_LIST.get("scp.qa.admin.login.user").toString(),
                PROP_LIST.get("scp.qa.admin.login.password").toString()
        ));
    }

    @BeforeMethod(dependsOnMethods = "setupForOrgAdmin")
    public void setupForContentCreator() {
        logger.info("Logging in as Content Creator");
        contentCreatorToken = String.valueOf(loginToScp(
                PROP_LIST.get("sl.scp.userascontentcreator").toString(),
                PROP_LIST.get("sl.scp.passwordforcontentcreator").toString()
        ));
    }

//    @BeforeTest
//    public void init() {
//        logger.info("Logging into the application :");
//        loginToScpContentCreator(PROP_LIST.get("sl.scp.userascontentcreator").toString(), PROP_LIST.get("sl.scp.passwordforcontentcreator").toString());
//        // Initialize the projectCreateEndpoint URI
//        try {
//            projectCreateEndpoint = new URI(PROP_LIST.get("scp.create.project.endpoint").toString());
//        } catch (URISyntaxException e) {
//            throw new RuntimeException("Invalid URI for createEntityTypeEndpoint", e);
//        }
//    }

    @Test(description = "Verifies the functionality of creating new project with valid payload.")
    public void testCreateProjectWithValidPayload() throws Exception {
        logger.info("Started calling the CreateProject API with valid payload:");

        // Path to JSON file containing request payload
        JSONObject requestBody = loadJsonPayload("testCreateProjectWithValidPayload");

        // Make the API call and verify the response
        JSONObject createRequestBody = createProject(requestBody);

        // Call updatePermission with the created requestBody
        Response response = createProjectRequest(createRequestBody, contentCreatorToken);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 200
        Assert.assertEquals(statusCode, 200, "Status code should be 200");

        // Fetch ID from response and validate
        createdId = response.jsonPath().getInt("result.id");
        Assert.assertNotNull(createdId, "Created ID should not be null");
        logger.info("Created ID: " + createdId);

        logger.info("Ended calling the projectCreation API with valid payload.");
    }

    @Test(description = "Verifies the createProject API response when called with an invalid payload.")
    public void testCreateProjectWithInvalidPayload() throws Exception {
        logger.info("Started calling the CreateProject API with invalid payload:");

        // Path to JSON file containing request payload
        JSONObject requestBody = loadJsonPayload("testCreateProjectWithInvalidPayload");

        // Generate the payload invalid
        requestBody.put("title", "");
        requestBody.put("recommended_duration", new HashMap<>());
        requestBody.remove("categories");

        // Make the API call and verify the response
        JSONObject createRequestBody = createProject(requestBody);

        // Call updatePermission with the created requestBody
        Response response = createProjectRequest(createRequestBody, contentCreatorToken);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 400
        Assert.assertEquals(statusCode, 400, "Status code should be 400 for invalid payload");

        logger.info("Ended calling the projectCreation API with Invalid payload.");
    }

    @Test(dependsOnMethods = "testCreateProjectWithValidPayload", description = "Verifies the functionality of updating project with valid payload.")
    public void testUpdateProjectWithValidPayload() throws Exception {
        logger.info("Started calling the UpdateProject API with valid payload:");

        // Load JSON payload and ensure the file exists
        JSONObject requestBody = loadJsonPayload("testCreateProjectWithValidPayload");

        // Generate random values for specific fields
        requestBody.put("title", "ProjectTitle" + RandomStringUtils.randomAlphabetic(8));
        requestBody.put("objective", "Objective" + RandomStringUtils.randomAlphabetic(20));
        requestBody.put("keywords", "Keyword" + RandomStringUtils.randomAlphabetic(10));

        // Make the API call and verify the response
        JSONObject createRequestBody = updateProject(requestBody);

        // Call updateRequest with the created requestBody
        Response response = updateRequest(createRequestBody, contentCreatorToken);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 202
        Assert.assertEquals(statusCode, 202, "Status code should be 202");

        logger.info("Ended calling the projectUpdation API with valid payload.");
    }


    @Test(description = "Verifies the functionality of updating new project with invalid payload using random strings.")
    public void testUpdateProjectWithInValidPayload() throws Exception {
        logger.info("Started calling the UpdateProject API with invalid payload:");

        // Load JSON payload and ensure the file exists
        JSONObject requestBody = loadJsonPayload("testCreateProjectWithValidPayload");

        // Use random strings for all values to create an invalid payload
        requestBody.put("title", " " + RandomStringUtils.randomAlphabetic(8));
        requestBody.put("objective", " " + RandomStringUtils.randomAlphabetic(20));

        // Make the API call and verify the response
        JSONObject createRequestBody = updateProject(requestBody);

        // Call updateRequest with the created requestBody
        Response response = updateRequest(createRequestBody, contentCreatorToken);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 400
        Assert.assertEquals(statusCode, 400, "Status code should be 400 for invalid payload");

        logger.info("Ended calling the projectUpdation API with invalid payload.");
    }

    @Test(description = "Verifies the functionality of getting project details with valid project id.")
    public void testGetProjectDetailsWithValidProjectId() {
        logger.info("Started calling the getProjectDetails API valid project ID.");

        // Call updatePermission with the created requestBody
        Response response = getProjectDetailsRequest(createdId, contentCreatorToken);

        // Log the status code and response body
        response.prettyPrint();

        // Validate response code
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        logger.info("Ended calling the projectCreation API with valid payload.");
    }

    @Test(description = "Verifies the functionality of getting project details with Invalid project id.")
    public void testGetProjectDetailsWithInValidProjectId() {
        logger.info("Started calling the getProjectDetails API Invalid project ID.");
        int invalidId = Integer.parseInt((PROP_LIST.get("scp.project.details.invalidId").toString()));

        Response response = getProjectDetailsRequest(invalidId, contentCreatorToken);

        // Log the status code and response body
        response.prettyPrint();

        // Validate response code for invalid token
        Assert.assertEquals(response.getStatusCode(), 400, "Project not found");
        logger.info("Ended calling the projectCreation API with Invalid payload.");
    }

    @Test(description = "Verifies the functionality of deleting project with valid project id.")
    public void testDeleteProjectWithValidProjectId() {
        logger.info("Started calling the delete project API with project id");

        // Call delete project with the created requestBody
        Response response = projectDeleteRequest(createdId, contentCreatorToken);

        // Log the status code and response body
        response.prettyPrint();

        // Validate response code
        Assert.assertEquals(response.getStatusCode(), 202, "Status code should be 202");
        logger.info("Ended calling the delete project API with valid project id.");
    }

    @Test(description = "Verifies the functionality of deleting project with Invalid project id.")
    public void testDeleteProjectWithInValidProjectId() {
        logger.info("Started calling the deleting project API Invalid project ID.");
        int invalidId = Integer.parseInt((PROP_LIST.get("scp.project.details.invalidId").toString()));

        Response response = getProjectDetailsRequest(invalidId, contentCreatorToken);

        // Log the status code and response body
        response.prettyPrint();

        // Validate response code for invalid token
        Assert.assertEquals(response.getStatusCode(), 400, "Project not found");
        logger.info("Ended calling the projectDeletion API with Invalid project id.");
    }

    //Method to create project
    private JSONObject createProject(Map<String, Object> map) {
        JSONObject requestBody = new JSONObject();
        requestBody.putAll(map);
        return requestBody;
    }

    private Response createProjectRequest(JSONObject requestBody, String token) {
        try {
            projectCreateEndpoint = new URI(PROP_LIST.get("scp.create.project.endpoint").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for projectCreateEndpoint", e);
        }

        // Make the POST request to update the permission
        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when().post(projectCreateEndpoint);

        // Pretty-print the response for debugging
        response.prettyPrint();

        return response;
    }

    // Method to update project request body
    private JSONObject updateProject(Map<String, Object> map) {
        JSONObject requestBody = new JSONObject();
        requestBody.putAll(map);
        return requestBody;
    }

    private Response updateRequest(JSONObject requestBody, String token) {
        try {
            projectUpdateEndpoint = new URI(PROP_LIST.get("scp.update.project.endpoint").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for updateProjectEndpoint", e);
        }
        // Make the POST request to update the permission
        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .pathParams("id", createdId)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when().post(projectUpdateEndpoint + "{id}");

        // Pretty-print the response for debugging
        response.prettyPrint();

        return response;
    }

    // Method to handle the getProjectDetails API request with project ID
    private Response getProjectDetailsRequest(Integer id, String token) {
        try {
            getProjectDetailsEndpoint = new URI(PROP_LIST.get("scp.qa.projectdetails").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for getProjectDetails", e);
        }

        // Make the GET request with the project ID as a path parameter
        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .pathParam("id", id)
                .contentType(ContentType.JSON)
                .when()
                .get(getProjectDetailsEndpoint + "{id}")
                .then()
                .extract().response();

        // Pretty-print the response for debugging
        response.prettyPrint();

        return response;
    }

    //Method to delete entityType
    private Response projectDeleteRequest(Integer id, String token) {
        try {
            projectDeletionEndpoint = new URI(PROP_LIST.get("scp.delete.project.endpoint").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for deleteProjectEndpoint", e);
        }

        // Make the DELETE project request
        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .pathParam("id", id)
                .contentType(ContentType.JSON)
                .when().delete(projectDeletionEndpoint + "{id}");

        // Pretty-print the response for debugging
        response.prettyPrint();

        return response;
    }

    //Method to load Json payload
    private JSONObject loadJsonPayload(String key) throws Exception {
        String filePath = "src/main/resources/scp_project_details_payload.json";
        JSONParser parser = new JSONParser();
        JSONObject jsonObject;

        try (FileReader reader = new FileReader(filePath)) {
            jsonObject = (JSONObject) parser.parse(reader);
        }

        return (JSONObject) jsonObject.get(key);
    }
}