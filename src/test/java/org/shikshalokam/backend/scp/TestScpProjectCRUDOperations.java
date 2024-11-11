package org.shikshalokam.backend.scp;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.shikshalokam.backend.MentorBase;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.net.URI;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.shikshalokam.backend.PropertyLoader.PROP_LIST;

public class TestScpProjectCRUDOperations extends SelfCreationPortalBaseTest {
    private static final Logger logger = LogManager.getLogger(TestScpProjectCRUDOperations.class);
    private URI projectCreateEndpoint, projectUpdateEndpoint, getProjectDetailsEndpoint, projectDeletionEndpoint;
    private Integer createdId;
    private String contentCreatorToken;

    @BeforeMethod()
    public void setupForContentCreator() {
        logger.info("Logging in as Content Creator");
        contentCreatorToken = String.valueOf(loginToScp(
                PROP_LIST.get("sl.scp.userascontentcreator").toString(),
                PROP_LIST.get("sl.scp.passwordforcontentcreator").toString()
        ));
    }

    @Test(description = "Verifies the functionality of creating new project with valid payload.")
    public void testCreateProjectWithValidPayload() throws Exception {
        logger.info("Started calling the CreateProject API with valid payload:");

        // Path to JSON file containing request payload
        JSONObject requestBody = loadJsonPayload("testCreateProjectWithValidPayload");

        // Make the API call and verify the response
        JSONObject createRequestBody = createProject(requestBody);

        // Call updatePermission with the created requestBody
        Response response = createProjectRequest(createRequestBody);

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

    @Test(description = "Verifies the creation with certificates.")
    public void testCreateProjectWithCertificate() throws Exception {
        logger.info("Started calling the CreateProject API with certificates:");

        // Path to JSON file containing request payload
        JSONObject requestBody = loadJsonPayload("testCreateProjectWithCertificate");

        // Make the API call and verify the response
        JSONObject createRequestBody = createProject(requestBody);

        // Call the project creation endpoint
        Response response = createProjectRequest(createRequestBody);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 200
        Assert.assertEquals(statusCode, 200, "Status code should be 200");

        // Fetch ID from response and validate
        createdId = response.jsonPath().getInt("result.id");
        Assert.assertNotNull(createdId, "Created ID should not be null");
        logger.info("Created ID: " + createdId);

        // Validate the content of the created project
        validateProjectContentAfterCreation();

        logger.info("Ended calling the projectCreation API with certificates.");
    }

    @Test(description = "Verifies the creation without certificates.")
    public void testCreateProjectWithoutCertificate() throws Exception {
        logger.info("Started calling the CreateProject API without certificates:");

        // Path to JSON file containing request payload
        JSONObject requestBody = loadJsonPayload("testCreateProjectWithoutCertificate");

        // Make the API call and verify the response
        JSONObject createRequestBody = createProject(requestBody);

        // Call the project creation endpoint
        Response response = createProjectRequest(createRequestBody);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 200
        Assert.assertEquals(statusCode, 200, "Status code should be 200");

        // Fetch ID from response and validate
        createdId = response.jsonPath().getInt("result.id");
        Assert.assertNotNull(createdId, "Created ID should not be null");
        logger.info("Created ID: " + createdId);

        // Validate the content of the created project
        validateProjectContentAfterCreation();

        logger.info("Ended calling the projectCreation API without certificates.");
    }

    @Test(description = "Verifies the creation with no subtasks.")
    public void testCreateProjectWithNoSubtasks() throws Exception {
        logger.info("Started calling the CreateProject API with no subtasks:");

        // Path to JSON file containing request payload
        JSONObject requestBody = loadJsonPayload("testCreateProjectWithNoSubtasks");

        // Make the API call and verify the response
        JSONObject createRequestBody = createProject(requestBody);

        // Call the project creation endpoint
        Response response = createProjectRequest(createRequestBody);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 200
        Assert.assertEquals(statusCode, 200, "Status code should be 200");

        // Fetch ID from response and validate
        createdId = response.jsonPath().getInt("result.id");
        Assert.assertNotNull(createdId, "Created ID should not be null");
        logger.info("Created ID: " + createdId);

        // Validate the content of the created project
        validateProjectContentAfterCreation();

        logger.info("Ended calling the projectCreation API with no subtasks.");
    }

    @Test(description = "Verifies the creation with only mandatory fields.")
    public void testCreateProjectWithOnlyMandatoryFields() throws Exception {
        logger.info("Started calling the CreateProject API with only mandatory fields:");

        // Path to JSON file containing request payload
        JSONObject requestBody = loadJsonPayload("testCreateProjectWithOnlyMandatoryFields");

        // Make the API call and verify the response
        JSONObject createRequestBody = createProject(requestBody);

        // Call the project creation endpoint
        Response response = createProjectRequest(createRequestBody);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 200
        Assert.assertEquals(statusCode, 200, "Status code should be 200");

        // Fetch ID from response and validate
        createdId = response.jsonPath().getInt("result.id");
        Assert.assertNotNull(createdId, "Created ID should not be null");
        logger.info("Created ID: " + createdId);

        // Validate the content of the created project
        validateProjectContentAfterCreation();

        logger.info("Ended calling the projectCreation API with only mandatory fields.");
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
        Response response = createProjectRequest(createRequestBody);

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
        Response response = updateRequest(createRequestBody);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 202
        Assert.assertEquals(statusCode, 202, "Status code should be 202");

        logger.info("Ended calling the projectUpdation API with valid payload.");
    }

    @Test(dependsOnMethods = "testCreateProjectWithValidPayload", description = "Verifies the functionality of updating project with certificates.")
    public void testUpdateProjectWithCertificate() throws Exception {
        logger.info("Started calling the UpdateProject API with certificates:");

        // Load JSON payload for update with certificates
        JSONObject requestBody = loadJsonPayload("testCreateProjectWithCertificate");

        // Modify values for updating
        requestBody.put("title", "Updated Project Title with Certificate");
        requestBody.put("objective", "Updated objective for project with certificate.");
        requestBody.put("keywords", "Updated, Classroom Management");

        // Make the API call to update the project
        JSONObject updateRequestBody = updateProject(requestBody);
        Response response = updateRequest(updateRequestBody);

        // Validate response code is 202
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 202, "Status code should be 202");

        // Validate the updated content
        validateUpdatedProjectContent(createdId, "Updated Project Title with Certificate", "Updated objective for project with certificate.", "Updated, Classroom Management");

        logger.info("Ended calling the UpdateProject API with certificates.");
    }

    @Test(dependsOnMethods = "testCreateProjectWithValidPayload", description = "Verifies the functionality of updating project without certificates.")
    public void testUpdateProjectWithoutCertificate() throws Exception {
        logger.info("Started calling the UpdateProject API without certificates:");

        // Load JSON payload for update without certificates
        JSONObject requestBody = loadJsonPayload("testCreateProjectWithoutCertificate");

        // Modify values for updating
        requestBody.put("title", "Updated Project Title without Certificate");
        requestBody.put("objective", "Updated objective for project without certificate.");
        requestBody.put("keywords", "Updated, Training");

        // Make the API call to update the project
        JSONObject updateRequestBody = updateProject(requestBody);
        Response response = updateRequest(updateRequestBody);

        // Validate response code is 202
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 202, "Status code should be 202");

        // Validate the updated content
        validateUpdatedProjectContent(createdId, "Updated Project Title without Certificate", "Updated objective for project without certificate.", "Updated, Training");

        logger.info("Ended calling the UpdateProject API without certificates.");
    }

    @Test(dependsOnMethods = "testCreateProjectWithValidPayload", description = "Verifies the functionality of updating project with no subtasks.")
    public void testUpdateProjectWithNoSubtasks() throws Exception {
        logger.info("Started calling the UpdateProject API with no subtasks:");

        // Load JSON payload for update with no subtasks
        JSONObject requestBody = loadJsonPayload("testCreateProjectWithNoSubtasks");

        // Modify values for updating
        requestBody.put("title", "Updated Project Title with No Subtasks");
        requestBody.put("objective", "Updated objective for project with no subtasks.");
        requestBody.put("keywords", "Updated, Subtask-less");

        // Make the API call to update the project
        JSONObject updateRequestBody = updateProject(requestBody);
        Response response = updateRequest(updateRequestBody);

        // Validate response code is 202
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 202, "Status code should be 202");

        // Validate the updated content
        validateUpdatedProjectContent(createdId, "Updated Project Title with No Subtasks", "Updated objective for project with no subtasks.", "Updated, Subtask-less");

        logger.info("Ended calling the UpdateProject API with no subtasks.");
    }

    @Test(dependsOnMethods = "testCreateProjectWithValidPayload", description = "Verifies the functionality of updating project with only mandatory fields.")
    public void testUpdateProjectWithOnlyMandatoryFields() throws Exception {
        logger.info("Started calling the UpdateProject API with only mandatory fields:");

        // Load JSON payload for update with only mandatory fields
        JSONObject requestBody = loadJsonPayload("testCreateProjectWithOnlyMandatoryFields");

        // Modify values for updating
        requestBody.put("title", "Updated Project Title with Only Mandatory Fields");
        requestBody.put("objective", "Updated objective for project with only mandatory fields.");
        requestBody.put("keywords", "Updated, Basic");

        // Make the API call to update the project
        JSONObject updateRequestBody = updateProject(requestBody);
        Response response = updateRequest(updateRequestBody);

        // Validate response code is 202
        int statusCode = response.getStatusCode();
        Assert.assertEquals(statusCode, 202, "Status code should be 202");

        // Validate the updated content
        validateUpdatedProjectContent(createdId, "Updated Project Title with Only Mandatory Fields", "Updated objective for project with only mandatory fields.", "Updated, Basic");

        logger.info("Ended calling the UpdateProject API with only mandatory fields.");
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
        Response response = updateRequest(createRequestBody);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 400
        Assert.assertEquals(statusCode, 400, "Status code should be 400 for invalid payload");

        logger.info("Ended calling the projectUpdation API with invalid payload.");
    }

    @Test(dependsOnMethods = "testCreateProjectWithValidPayload", description = "Verifies the functionality of getting project details with valid project id.")
    public void testGetProjectDetailsWithValidProjectId() {
        logger.info("Started calling the getProjectDetails API valid project ID.");

        // Call updatePermission with the created requestBody
        Response response = getProjectDetailsRequest(createdId);

        // Log the status code and response body
        response.prettyPrint();

        // Validate response code
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        logger.info("Ended calling the getProjectDetails API with valid payload.");
    }

    @Test(description = "Verifies the functionality of getting project details with Invalid project id.")
    public void testGetProjectDetailsWithInValidProjectId() {
        logger.info("Started calling the getProjectDetails API Invalid project ID.");
        int invalidId = Integer.parseInt(RandomStringUtils.randomNumeric(8));

        Response response = getProjectDetailsRequest(invalidId);

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
        Response response = projectDeleteRequest(createdId);

        // Log the status code and response body
        response.prettyPrint();

        // Validate response code
        Assert.assertEquals(response.getStatusCode(), 202, "Status code should be 202");
        logger.info("Ended calling the delete project API with valid project id.");
    }

    @Test(description = "Verifies the functionality of deleting a project that doesn't exist.")
    public void testDeletingNotTheExistingOne() {
        logger.info("Started calling the deleting project API Invalid project ID.");
        int nonExistingProjectId = Integer.parseInt(RandomStringUtils.randomNumeric(8));

        Response response = getProjectDetailsRequest(nonExistingProjectId);

        // Log the status code and response body
        response.prettyPrint();

        // Validate response code for invalid token
        Assert.assertEquals(response.getStatusCode(), 400, "Project not found");
        logger.info("Ended calling the delete project API with a non-existing project id.");
    }

    //Method to create project
    private JSONObject createProject(Map<String, Object> map) {
        JSONObject requestBody = new JSONObject();
        requestBody.putAll(map);
        return requestBody;
    }

    private Response createProjectRequest(JSONObject requestBody) {
        projectCreateEndpoint = MentorBase.createURI(PROP_LIST.get("scp.create_update.project.endpoint").toString());
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

    private Response updateRequest(JSONObject requestBody) {
        projectUpdateEndpoint = MentorBase.createURI(PROP_LIST.get("scp.create_update.project.endpoint").toString());
        // Make the POST request to update the permission
        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .pathParams("id", createdId)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when().post(projectUpdateEndpoint + "/{id}");

        // Pretty-print the response for debugging
        response.prettyPrint();

        return response;
    }

    // Method to handle the getProjectDetails API request with project ID
    private Response getProjectDetailsRequest(Integer id) {
        getProjectDetailsEndpoint = MentorBase.createURI(PROP_LIST.get("scp.qa.projectdetails").toString());
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
    private Response projectDeleteRequest(Integer id) {
        projectDeletionEndpoint = MentorBase.createURI(PROP_LIST.get("scp.delete.project.endpoint").toString());
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

    // Method to validate project content after creation
    private void validateProjectContentAfterCreation() {
        logger.info("Validating project content after creation with project ID: " + createdId);

        // Fetch project details using the created project ID
        Response response = getProjectDetailsRequest(createdId);

        // Validate the project title
        Assert.assertEquals(response.jsonPath().getString("result.title"), "Effective Classroom Management", "Project title mismatch");

        // Validate the project objective
        Assert.assertEquals(response.jsonPath().getString("result.objective"), "Teachers will learn strategies to manage classrooms effectively.", "Project objective mismatch");

        // Validate the keywords
        Assert.assertEquals(response.jsonPath().getString("result.keywords"), "Classroom Management, Teacher Training", "Project keywords mismatch");

        // Get the isCertificateSelected value
        String isCertificateSelected = response.jsonPath().getString("result.formMeta.isCertificateSelected");

        // If certificate is not selected, it should be null
        if (isCertificateSelected != null && isCertificateSelected.equals("0")) {
            Map<String, Object> certificate = response.jsonPath().get("result.certificate");
            Assert.assertNull(certificate, "Certificate should be null when not selected");
        } else {
            // Validate the certificate details when it is selected
            Map<String, Object> certificate = response.jsonPath().get("result.certificate");
            Assert.assertNotNull(certificate, "Certificate should not be null when selected");

            // Validate the certificate fields
            Assert.assertEquals(certificate.get("name"), "Classroom Management Certificate", "Certificate name mismatch");
            Assert.assertEquals(certificate.get("code"), "classroom_management_certificate", "Certificate code mismatch");
            Assert.assertEquals(certificate.get("issuer"), "Teacher Training Institute", "Certificate issuer mismatch");

            // Validate the certificate criteria
            Map<String, Object> criteria = (Map<String, Object>) certificate.get("criteria");
            Assert.assertNotNull(criteria, "Certificate criteria should not be null");
            Assert.assertEquals(criteria.get("validationText"), "Complete the project tasks to receive the certificate.", "Certificate validation text mismatch");

            // Validate conditions C1 and C2
            Map<String, Object> conditions = (Map<String, Object>) criteria.get("conditions");
            Assert.assertTrue(conditions.containsKey("C1"), "Condition C1 should be present");
            Assert.assertTrue(conditions.containsKey("C2"), "Condition C2 should be present");
        }

        // Validate subtasks field - check if it is null or empty
        List<Object> subtasks = response.jsonPath().getList("result.subtasks");
        if (subtasks != null) {
            Assert.assertTrue(subtasks.isEmpty(), "Subtasks should be empty");
        } else {
            logger.info("No subtasks found in the response, which is valid.");
        }

        // Log that validation was successful
        logger.info("Project content validated successfully with project ID: " + createdId);
    }

    // Method to validate updated project content
    private void validateUpdatedProjectContent(int projectId, String expectedTitle, String expectedObjective, String expectedKeywords) {
        logger.info("Validating updated project content for project ID: " + projectId);

        // Fetch project details after update
        Response response = getProjectDetailsRequest(projectId);

        // Validate title, objective, and keywords
        Assert.assertEquals(response.jsonPath().getString("result.title"), expectedTitle, "Project title mismatch after update");
        Assert.assertEquals(response.jsonPath().getString("result.objective"), expectedObjective, "Project objective mismatch after update");
        Assert.assertEquals(response.jsonPath().getString("result.keywords"), expectedKeywords, "Project keywords mismatch after update");

        logger.info("Updated project content validated successfully with project ID: " + projectId);
    }
}