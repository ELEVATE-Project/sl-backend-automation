package org.shikshalokam.backend.scp;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static io.restassured.RestAssured.given;
import static org.shikshalokam.backend.PropertyLoader.PROP_LIST;

public class TestScpProjectCRUDOperations extends SelfCreationPortalBaseTest {
    private static final Logger logger = LogManager.getLogger(TestScpProjectCRUDOperations.class);
    private URI projectCreateEndpoint, projectUpdateEndpoint, getProjectDetailsEndpoint, projectDeletionEndpoint;
    private Integer createdId;

    @BeforeTest
    public void init() {
        logger.info("Logging into the application :");
        loginToScpContentCreator(PROP_LIST.get("sl.scp.userascontentcreator").toString(), PROP_LIST.get("sl.scp.passwordforcontentcreator").toString());
        // Initialize the projectCreateEndpoint URI
        try {
            projectCreateEndpoint = new URI(PROP_LIST.get("scp.create.project.endpoint").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for createEntityTypeEndpoint", e);
        }
    }

    @Test(description = "Verifies the functionality of creating new project with valid payload.")
    public void testCreateProjectWithValidPayload() {
        logger.info("Started calling the CreateProject API with valid payload:");

        // Call ProjectCreation with valid parameters
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", "587");
        requestBody.put("title", "Innovative Pedagogy in Action");

        // Categories List
        List<String> categories = Arrays.asList("teachers", "students", "community");
        requestBody.put("categories", categories);

        requestBody.put("objective", "Teachers will be able to engage students in class using the Innovative Pedagogy Method");

        // Recommended Duration Map
        Map<String, String> recommendedDuration = new HashMap<>();
        recommendedDuration.put("number", "2");
        recommendedDuration.put("duration", "months");
        requestBody.put("recommended_duration", recommendedDuration);

        requestBody.put("keywords", "Innovative Pedagogy");

        // Recommended For List
        List<String> recommendedFor = Arrays.asList("hm", "head_teacher", "principal", "head_master/mistress");
        requestBody.put("recommended_for", recommendedFor);

        // Languages List
        List<String> languages = Arrays.asList("en", "hi", "assamese", "bengali");
        requestBody.put("languages", languages);

        // Learning Resources List
        List<Map<String, String>> learningResources = new ArrayList<>();
        learningResources.add(new HashMap<>() {{
            put("name", "What is Innovative Pedagogy English");
            put("url", "https://diksha.gov.in/play/content/do_314145472557998080112225");
        }});
        learningResources.add(new HashMap<>() {{
            put("name", "How to conduct a staff meeting");
            put("url", "https://diksha.gov.in/play/content/do_31414547447630233618641");
        }});
        requestBody.put("learning_resources", learningResources);

        requestBody.put("licenses", "cc_by_nc");
        Map<String, Object> formMeta = new HashMap<>();
        Map<String, String> formValidation = new HashMap<>();
        formValidation.put("projectDetails", "VALID");
        formValidation.put("tasks", "VALID");
        formValidation.put("subTasks", "VALID");
        formValidation.put("certificates", "VALID");
        formMeta.put("formValidation", formValidation);
        formMeta.put("isCertificateSelected", "1");
        formMeta.put("isProjectEvidenceSelected", "0");
        Map<String, Integer> taskEvidenceSelected = new HashMap<>();
        taskEvidenceSelected.put("70aa6379-df7f-4033-a653-d58e45adff24", 1);
        taskEvidenceSelected.put("c43eab58-cb4d-4a6d-a6ee-ab63952e4e43", 1);
        formMeta.put("taskEvidenceSelected", taskEvidenceSelected);
        requestBody.put("formMeta", formMeta);

        // Tasks List
        List<Map<String, Object>> tasks = new ArrayList<>();
        tasks.add(new HashMap<>() {{
            put("id", "70aa6379-df7f-4033-a653-d58e45adff24");
            put("name", "Watch the video to learn about What is Innovative Pedagogy");
            put("is_mandatory", true);
            put("allow_evidences", true);
            put("type", "content");
            put("sequence_no", 1);
            put("solution_details", new HashMap<>());

            Map<String, Object> evidenceDetails = new HashMap<>();
            evidenceDetails.put("file_types", Arrays.asList("images", "document", "videos", "audio"));
            evidenceDetails.put("min_no_of_evidences", 5);
            put("evidence_details", evidenceDetails);

            List<Map<String, String>> taskLearningResources = new ArrayList<>();
            taskLearningResources.add(new HashMap<>() {{
                put("name", "Sample Lesson Plan Template");
                put("url", "https://diksha.gov.in/play/content/do_31414547548140339219861");
            }});
            taskLearningResources.add(new HashMap<>() {{
                put("name", "Guidelines for Video Evidence");
                put("url", "https://diksha.gov.in/play/content/do_31414547669843148819063");
            }});
            put("learning_resources", taskLearningResources);

            List<Map<String, Object>> children = new ArrayList<>();
            children.add(new HashMap<>() {{
                put("id", "ae207d2c-f42b-442b-9647-7a90b4cfb769");
                put("name", "Sub task 1");
                put("type", "content");
                put("parent_id", "70aa6379-df7f-4033-a653-d58e45adff24");
                put("sequence_no", 1);
                put("is_mandatory", true);
                put("allow_evidences", true);
            }});
            put("children", children);
        }});
        requestBody.put("tasks", tasks);

        // Certificate Map
        Map<String, Object> certificate = new HashMap<>();
        certificate.put("base_template_id", 1);
        certificate.put("base_template_url", "https://mentoring-prod-storage.s3.ap-south-1.amazonaws.com/certfile_BASE_TEMPLATE/system/cert/cf317c7c-4496-4fe1-8648-8cf751ec78c6/one_logo_one_sign.svg");
        certificate.put("code", "one_logo_one_sign");
        certificate.put("name", "One Logo One Signature");
        Map<String, Object> logos = new HashMap<>();
        logos.put("no_of_logos", 1);
        logos.put("stateLogo1", "https://mentoring-prod-storage.s3.ap-south-1.amazonaws.com/certificate/314/certificate/f9d18893-461b-452e-ad65-633b5d0015d6/image-80x80.png");
        logos.put("stateLogo2", "");
        certificate.put("logos", logos);
        Map<String, Object> signature = new HashMap<>();
        signature.put("no_of_signature", 1);
        signature.put("signatureImg1", "https://mentoring-prod-storage.s3.ap-south-1.amazonaws.com/certificate/314/certificate/cd9b539b-88ad-4929-a2ce-db6bad2676d0/image-120x50%20%282%29.png");
        signature.put("signatureTitleName1", "Smti R S Manners MCS");
        signature.put("signatureTitleDesignation1", "Director Directorate of Educational Research and Training");
        signature.put("signatureImg2", "");
        signature.put("signatureTitleName2", "");
        signature.put("signatureTitleDesignation2", "");
        certificate.put("signature", signature);
        certificate.put("issuer", "Directorate of Educational Research and Training Government of Meghalaya");

        // Certificate Criteria
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("validationText", "Complete validation message");
        criteria.put("expression", "C1&&C3");
        Map<String, Object> conditions = new HashMap<>();
        Map<String, Object> conditionC1 = new HashMap<>();
        conditionC1.put("scope", "project");
        conditionC1.put("key", "status");
        conditionC1.put("operator", "==");
        conditionC1.put("value", "submitted");
        Map<String, Object> conditionC2 = new HashMap<>();
        conditionC2.put("scope", "project");
        conditionC2.put("key", "attachments");
        conditionC2.put("function", "count");
        conditionC2.put("filter", new HashMap<String, Object>());
        conditionC2.put("operator", ">");
        conditionC2.put("value", "10");
        conditions.put("C1", conditionC1);
        conditions.put("C2", conditionC2);
        criteria.put("conditions", conditions);
        certificate.put("criteria", criteria);
        requestBody.put("certificate", certificate);

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

    @Test(description = "Verifies the createProject API response when called with an invalid payload.")
    public void testCreateProjectWithInvalidPayload() {
        logger.info("Started calling the CreateProject API with invalid payload:");

        // Create invalid requestBody
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", 123);
        requestBody.put("title", "");
        requestBody.put("categories", "teachers");
        requestBody.put("objective", null);
        Map<String, Object> recommendedDuration = new HashMap<>();
        recommendedDuration.put("time", 2);
        recommendedDuration.put("period", true);
        requestBody.put("recommended_duration", recommendedDuration);
        requestBody.put("keywords", 12345);
        requestBody.put("recommended_for", new ArrayList<>());
        requestBody.put("languages", Arrays.asList("xyz", "123"));
        List<Map<String, String>> learningResources = new ArrayList<>();
        learningResources.add(new HashMap<>() {{
            put("name", "");
        }});
        requestBody.put("learning_resources", learningResources);
        requestBody.put("licenses", "invalid_license");
        Map<String, Object> formMeta = new HashMap<>();
        formMeta.put("formValidation", "INVALID");
        formMeta.put("isCertificateSelected", 2);
        formMeta.put("isProjectEvidenceSelected", null);
        requestBody.put("formMeta", formMeta);
        List<Map<String, Object>> tasks = new ArrayList<>();
        tasks.add(new HashMap<>() {{
            put("id", 12345);
            put("name", "");
            put("is_mandatory", "yes");
            put("type", "unknown_type");
            put("sequence_no", "one");
        }});
        requestBody.put("tasks", tasks);
        Map<String, Object> certificate = new HashMap<>();
        certificate.put("base_template_id", "one");
        certificate.put("base_template_url", "invalid_url");
        certificate.put("name", null);
        certificate.put("issuer", 12345);
        requestBody.put("certificate", certificate);

        // Make the API call and verify the response
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

    @Test(dependsOnMethods = "testCreateProjectWithValidPayload", description = "Verifies the functionality of updating project with valid payload using valid payload.")
    public void testUpdateProjectWithValidPayload() {
        logger.info("Started calling the UpdateProject API with valid payload:");

        // Use random strings for all values
        String randomTitle = "ProjectTitle" + RandomStringUtils.randomAlphabetic(8);
        String randomObjective = "Objective" + RandomStringUtils.randomAlphabetic(20);
        String randomKeyword = "Keyword" + RandomStringUtils.randomAlphabetic(10);

        // Call ProjectCreation with random parameters
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", "587");
        requestBody.put("title", randomTitle);

        // Categories List
        List<String> categories = Arrays.asList("teachers", "students", "community");
        requestBody.put("categories", categories);

        requestBody.put("objective", randomObjective);

        // Recommended Duration Map
        Map<String, String> recommendedDuration = new HashMap<>();
        recommendedDuration.put("number", "2");
        recommendedDuration.put("duration", "months");
        requestBody.put("recommended_duration", recommendedDuration);

        requestBody.put("keywords", randomKeyword);

        // Recommended For List
        List<String> recommendedFor = Arrays.asList("hm", "head_teacher", "principal", "head_master/mistress");
        requestBody.put("recommended_for", recommendedFor);

        // Languages List
        List<String> languages = Arrays.asList("en", "hi", "assamese", "bengali");
        requestBody.put("languages", languages);

        // Learning Resources List
        List<Map<String, String>> learningResources = new ArrayList<>();
        learningResources.add(new HashMap<>() {{
            put("name", "ResourceName" + RandomStringUtils.randomAlphabetic(5));
            put("url", "https://example.com/resource/" + RandomStringUtils.randomAlphanumeric(10));
        }});
        requestBody.put("learning_resources", learningResources);

        requestBody.put("licenses", "cc_by_nc");
        Map<String, Object> formMeta = new HashMap<>();
        Map<String, String> formValidation = new HashMap<>();
        formValidation.put("projectDetails", "VALID");
        formValidation.put("tasks", "VALID");
        formValidation.put("subTasks", "VALID");
        formValidation.put("certificates", "VALID");
        formMeta.put("formValidation", formValidation);
        formMeta.put("isCertificateSelected", "1");
        formMeta.put("isProjectEvidenceSelected", "0");
        Map<String, Integer> taskEvidenceSelected = new HashMap<>();
        taskEvidenceSelected.put(RandomStringUtils.randomAlphanumeric(36), 1);
        formMeta.put("taskEvidenceSelected", taskEvidenceSelected);
        requestBody.put("formMeta", formMeta);

        // Tasks List
        List<Map<String, Object>> tasks = new ArrayList<>();
        tasks.add(new HashMap<>() {{
            put("id", RandomStringUtils.randomAlphanumeric(36));
            put("name", "TaskName" + RandomStringUtils.randomAlphabetic(5));
            put("is_mandatory", true);
            put("allow_evidences", true);
            put("type", "content");
            put("sequence_no", 1);
            put("solution_details", new HashMap<>());

            Map<String, Object> evidenceDetails = new HashMap<>();
            evidenceDetails.put("file_types", Arrays.asList("images", "document", "videos", "audio"));
            evidenceDetails.put("min_no_of_evidences", 5);
            put("evidence_details", evidenceDetails);

            List<Map<String, String>> taskLearningResources = new ArrayList<>();
            taskLearningResources.add(new HashMap<>() {{
                put("name", "LessonPlan" + RandomStringUtils.randomAlphabetic(5));
                put("url", "https://example.com/lesson/" + RandomStringUtils.randomAlphanumeric(10));
            }});
            put("learning_resources", taskLearningResources);

            List<Map<String, Object>> children = new ArrayList<>();
            children.add(new HashMap<>() {{
                put("id", RandomStringUtils.randomAlphanumeric(36));
                put("name", "SubTask" + RandomStringUtils.randomAlphabetic(5));
                put("type", "content");
                put("parent_id", RandomStringUtils.randomAlphanumeric(36));
                put("sequence_no", 1);
                put("is_mandatory", true);
                put("allow_evidences", true);
            }});
            put("children", children);
        }});
        requestBody.put("tasks", tasks);

        // Certificate Map
        Map<String, Object> certificate = new HashMap<>();
        certificate.put("base_template_id", 1);
        certificate.put("base_template_url", "https://example.com/certificate/template/" + RandomStringUtils.randomAlphanumeric(10));
        certificate.put("code", "CertCode" + RandomStringUtils.randomAlphabetic(5));
        certificate.put("name", "CertName" + RandomStringUtils.randomAlphabetic(5));
        Map<String, Object> logos = new HashMap<>();
        logos.put("no_of_logos", 1);
        logos.put("stateLogo1", "https://example.com/logo/" + RandomStringUtils.randomAlphanumeric(10));
        certificate.put("logos", logos);
        Map<String, Object> signature = new HashMap<>();
        signature.put("no_of_signature", 1);
        signature.put("signatureImg1", "https://example.com/signature/" + RandomStringUtils.randomAlphanumeric(10));
        signature.put("signatureTitleName1", "SignatureName" + RandomStringUtils.randomAlphabetic(5));
        signature.put("signatureTitleDesignation1", "Designation" + RandomStringUtils.randomAlphabetic(5));
        certificate.put("signature", signature);
        certificate.put("issuer", "Issuer" + RandomStringUtils.randomAlphabetic(10));

        // Certificate Criteria
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("validationText", "Validation" + RandomStringUtils.randomAlphabetic(8));
        criteria.put("expression", "C1&&C2");
        Map<String, Object> conditions = new HashMap<>();
        Map<String, Object> conditionC1 = new HashMap<>();
        conditionC1.put("scope", "project");
        conditionC1.put("key", "status");
        conditionC1.put("operator", "==");
        conditionC1.put("value", "submitted");
        conditions.put("C1", conditionC1);
        criteria.put("conditions", conditions);
        certificate.put("criteria", criteria);
        requestBody.put("certificate", certificate);

        // Make the API call and verify the response
        JSONObject createRequestBody = updateProject(requestBody);

        // Call updatePermission with the created requestBody
        Response response = updateRequest(createRequestBody);

        // Log the status code and response body
        int statusCode = response.getStatusCode();
        response.prettyPrint();

        // Validate response code is 202
        Assert.assertEquals(statusCode, 202, "Status code should be 202");

        logger.info("Ended calling the projectUpdation API with valid payload.");
    }

    @Test(description = "Verifies the functionality of updating new project with invalid payload using random strings.")
    public void testUpdateProjectWithInValidPayload() {
        logger.info("Started calling the UpdateProject API with invalid payload:");

        // Use random strings for all values to create an invalid payload
        String randomId = RandomStringUtils.randomAlphabetic(5);
        String randomTitle = "";
        String randomObjective = "";
        String randomKeyword = RandomStringUtils.randomNumeric(5);

        // Call ProjectCreation with invalid parameters
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", randomId);
        requestBody.put("title", randomTitle);

        // Categories List - providing an empty list to make it invalid
        List<String> categories = new ArrayList<>();
        requestBody.put("categories", categories);

        requestBody.put("objective", randomObjective);

        // Recommended Duration Map - incorrect value for duration to make it invalid
        Map<String, String> recommendedDuration = new HashMap<>();
        recommendedDuration.put("number", "0");
        recommendedDuration.put("duration", "invalid_unit");
        requestBody.put("recommended_duration", recommendedDuration);

        requestBody.put("keywords", randomKeyword);

        // Recommended For List - providing an invalid role
        List<String> recommendedFor = Arrays.asList("invalid_role");
        requestBody.put("recommended_for", recommendedFor);

        // Languages List - providing an unsupported language code
        List<String> languages = Arrays.asList("xyz");
        requestBody.put("languages", languages);

        // Learning Resources List - invalid URL format
        List<Map<String, String>> learningResources = new ArrayList<>();
        learningResources.add(new HashMap<>() {{
            put("name", "ResourceName" + RandomStringUtils.randomAlphabetic(5));
            put("url", "invalid_url");
        }});
        requestBody.put("learning_resources", learningResources);

        requestBody.put("licenses", "");
        Map<String, Object> formMeta = new HashMap<>();
        Map<String, String> formValidation = new HashMap<>();
        formValidation.put("projectDetails", "INVALID");
        formValidation.put("tasks", "INVALID");
        formValidation.put("subTasks", "INVALID");
        formValidation.put("certificates", "INVALID");
        formMeta.put("formValidation", formValidation);
        formMeta.put("isCertificateSelected", "");
        formMeta.put("isProjectEvidenceSelected", "invalid_value");
        Map<String, Integer> taskEvidenceSelected = new HashMap<>();
        taskEvidenceSelected.put(RandomStringUtils.randomAlphanumeric(36), -1);
        formMeta.put("taskEvidenceSelected", taskEvidenceSelected);
        requestBody.put("formMeta", formMeta);

        // Tasks List - providing invalid task details
        List<Map<String, Object>> tasks = new ArrayList<>();
        tasks.add(new HashMap<>() {{
            put("id", RandomStringUtils.randomAlphanumeric(5));
            put("name", "");
            put("is_mandatory", false);
            put("allow_evidences", false);
            put("type", "invalid_type");
            put("sequence_no", -1);
            put("solution_details", new HashMap<>());

            Map<String, Object> evidenceDetails = new HashMap<>();
            evidenceDetails.put("file_types", Arrays.asList("unsupported_type"));
            evidenceDetails.put("min_no_of_evidences", -5);
            put("evidence_details", evidenceDetails);

            List<Map<String, String>> taskLearningResources = new ArrayList<>();
            taskLearningResources.add(new HashMap<>() {{
                put("name", "");
                put("url", "invalid_url");
            }});
            put("learning_resources", taskLearningResources);

            List<Map<String, Object>> children = new ArrayList<>();
            children.add(new HashMap<>() {{
                put("id", RandomStringUtils.randomAlphanumeric(5));
                put("name", "");
                put("type", "invalid_type");
                put("parent_id", RandomStringUtils.randomAlphanumeric(5));
                put("sequence_no", -1);
                put("is_mandatory", false);
                put("allow_evidences", false);
            }});
            put("children", children);
        }});
        requestBody.put("tasks", tasks);

        // Certificate Map - invalid data
        Map<String, Object> certificate = new HashMap<>();
        certificate.put("base_template_id", -1);
        certificate.put("base_template_url", "invalid_url");
        certificate.put("code", "");
        certificate.put("name", "");
        Map<String, Object> logos = new HashMap<>();
        logos.put("no_of_logos", -1);
        logos.put("stateLogo1", "invalid_url");
        certificate.put("logos", logos);
        Map<String, Object> signature = new HashMap<>();
        signature.put("no_of_signature", -1);
        signature.put("signatureImg1", "invalid_url");
        signature.put("signatureTitleName1", "");
        signature.put("signatureTitleDesignation1", "");
        certificate.put("signature", signature);
        certificate.put("issuer", "");

        // Certificate Criteria - invalid data
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("validationText", "");
        criteria.put("expression", "");
        Map<String, Object> conditions = new HashMap<>();
        Map<String, Object> conditionC1 = new HashMap<>();
        conditionC1.put("scope", "invalid_scope");
        conditionC1.put("key", "");
        conditionC1.put("operator", "");
        conditionC1.put("value", "");
        conditions.put("C1", conditionC1);
        criteria.put("conditions", conditions);
        certificate.put("criteria", criteria);
        requestBody.put("certificate", certificate);

        // Make the API call and verify the response
        JSONObject createRequestBody = updateProject(requestBody);

        // Call updatePermission with the created requestBody
        Response response = updateRequest(createRequestBody);

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
        Response response = getProjectDetailsRequest(createdId);

        // Log the status code and response body
        response.prettyPrint();

        // Validate response code
        Assert.assertEquals(response.getStatusCode(), 200, "Status code should be 200");
        logger.info("Ended calling the projectCreation API with valid payload.");
    }

    @Test(description = "Verifies the functionality of getting project details with Invalid project id.")
    public void testGetProjectDetailsWithInValidProjectId() {
        logger.info("Started calling the getProjectDetails API Invalid project ID.");
        int invalidId = 9999999;

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

    @Test(description = "Verifies the functionality of deleting project with Invalid project id.")
    public void testDeleteProjectWithInValidProjectId() {
        logger.info("Started calling the deleting project API Invalid project ID.");
        int invalidId = 9999999;

        Response response = getProjectDetailsRequest(invalidId);

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

    private Response createProjectRequest(JSONObject requestBody) {
        try {
            projectCreateEndpoint = new URI(PROP_LIST.get("scp.create.project.endpoint").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for projectCreateEndpoint", e);
        }

        // Make the POST request to update the permission
        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN_CC)
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
        try {
            projectUpdateEndpoint = new URI(PROP_LIST.get("scp.update.project.endpoint").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for updateProjectEndpoint", e);
        }
        // Make the POST request to update the permission
        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN_CC)
                .pathParams("id", createdId)
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
                .when().post(projectUpdateEndpoint + "{id}");

        // Pretty-print the response for debugging
        response.prettyPrint();

        return response;
    }

    // Method to handle the getProjectDetails API request with project ID
    private Response getProjectDetailsRequest(Integer id) {
        try {
            getProjectDetailsEndpoint = new URI(PROP_LIST.get("scp.qa.projectdetails").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for getProjectDetails", e);
        }

        // Make the GET request with the project ID as a path parameter
        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN_CC)
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
        try {
            projectDeletionEndpoint = new URI(PROP_LIST.get("scp.delete.project.endpoint").toString());
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URI for deleteProjectEndpoint", e);
        }

        // Make the DELETE project request
        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN_CC)
                .pathParam("id", id)
                .contentType(ContentType.JSON)
                .when().delete(projectDeletionEndpoint + "{id}");

        // Pretty-print the response for debugging
        response.prettyPrint();

        return response;
    }
}