package org.shikshalokam.backend.userservice;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.userServiceUtility.CommonUtilityUserService;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.shikshalokam.backend.PropertyLoader.PROP_LIST;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class TestFeatureCRUDOperations extends UserServiceBaseTest {

    private static final Logger logger = LogManager.getLogger(TestFeatureCRUDOperations.class);

    private String adminToken;
    private String featureCode;
    private String featureLabel;
    private String featureDescription;
    private int displayOrder;

    @BeforeClass
    public void init() {
        logger.info("Generating Admin Token");

        adminToken = CommonUtilityUserService.generateAdminToken();

        if (adminToken == null || adminToken.isEmpty()) {
            logger.error("Admin Token Generation Failed");
            fail("Admin Token Generation Failed");
        }

        String random = RandomStringUtils.randomAlphabetic(6).toLowerCase();

        featureCode = "autofeature" + random;
        featureLabel = featureCode;
        featureDescription = "Project capability feature check";
        displayOrder = 16;

        logger.info("Feature Code : {}", featureCode);
        logger.info("Feature Label : {}", featureLabel);
        logger.info("Admin Token Generated Successfully");
    }

    @Test
    public void testCreateFeature() {
        logger.info("Started Create Feature API");

        Response response = given()
                .header("X-auth-token", adminToken)
                .contentType(ContentType.JSON)
                .body(getCreateFeatureBody())
                .when()
                .post(PROP_LIST.getProperty("userservice.create.feature.endpoint"));

        printResponse("Create Feature", response);

        assertEquals(response.getStatusCode(), 201, "Create Feature API failed");
        assertEquals(response.jsonPath().getString("message"), "Feature created successfully", "Message mismatch");
        assertEquals(response.jsonPath().getString("result.code"), featureCode, "Feature code mismatch");
        assertEquals(response.jsonPath().getString("result.label"), featureLabel, "Feature label mismatch");

        logger.info("Feature Created Successfully");
    }

    @Test(dependsOnMethods = "testCreateFeature")
    public void testUpdateFeature() {
        logger.info("Started Update Feature API");

        featureLabel = featureLabel + "Updated";
        featureDescription = "Updated Project capability feature";

        String endpoint = PROP_LIST.getProperty("userservice.update.feature.endpoint") + "/{featureCode}";

        Response response = given()
                .header("X-auth-token", adminToken)
                .pathParam("featureCode", featureCode)
                .contentType(ContentType.JSON)
                .body(getUpdateFeatureBody())
                .when()
                .post(endpoint);

        printResponse("Update Feature", response);

        assertEquals(response.getStatusCode(), 200, "Update Feature API failed");
        assertEquals(response.jsonPath().getString("result.label"), featureLabel, "Updated label mismatch");
        assertEquals(response.jsonPath().getString("result.description"), featureDescription, "Updated description mismatch");

        logger.info("Feature Updated Successfully");
    }

    @Test(dependsOnMethods = "testUpdateFeature")
    public void testMapFeatureToOrganization() {
        logger.info("Started Organization Feature Mapping API");

        Response response = given()
                .header("X-auth-token", adminToken)
                .header("x-org-code", "tripura")
                .header("x-tenant-code", "shikshagrahanew")
                .contentType(ContentType.JSON)
                .body(getOrganizationFeatureMappingBody())
                .when()
                .post(PROP_LIST.getProperty("userservice.organization.feature.mapping.endpoint"));

        printResponse("Organization Feature Mapping", response);

        assertEquals(response.getStatusCode(), 201, "Organization Feature Mapping API failed");
        assertEquals(response.jsonPath().getString("result.feature_code"), featureCode, "Feature code mismatch");
        assertEquals(response.jsonPath().getString("result.feature_name"), featureLabel, "Feature label mismatch");

        logger.info("Organization Feature Mapping Successful");
    }

    @Test(dependsOnMethods = "testMapFeatureToOrganization")
    public void testUpdateOrganizationFeatureMapping() {
        logger.info("Started Update Organization Feature API");

        String endpoint = PROP_LIST.getProperty("userservice.organization.feature.update.endpoint") + "/{featureCode}";

        Response response = given()
                .header("X-auth-token", adminToken)
                .header("x-org-code", "tripura")
                .header("x-tenant-code", "shikshagrahanew")
                .pathParam("featureCode", featureCode)
                .contentType(ContentType.JSON)
                .body(getUpdateOrganizationFeatureMappingBody())
                .when()
                .patch(endpoint);

        printResponse("Update Organization Feature", response);

        assertEquals(response.getStatusCode(), 200, "Update Organization Feature API failed");
        assertEquals(response.jsonPath().getString("message"), "Organization Feature updated successfully", "Message mismatch");
        assertEquals(response.jsonPath().getString("result.feature_code"), featureCode, "Feature code mismatch");
        assertEquals(response.jsonPath().getString("result.feature_name"), featureLabel, "Feature name mismatch");

        logger.info("Organization Feature Updated Successfully");
    }

    @Test(dependsOnMethods = "testUpdateOrganizationFeatureMapping")
    public void testReadOrganizationFeatureMapping() {
        logger.info("Started Read Organization Feature API");

        String endpoint = PROP_LIST.getProperty("userservice.organization.feature.read.endpoint") + "/{featureCode}";

        Response response = given()
                .header("X-auth-token", adminToken)
                .header("x-org-code", "tripura")
                .header("x-tenant-code", "shikshagrahanew")
                .pathParam("featureCode", featureCode)
                .contentType(ContentType.JSON)
                .when()
                .get(endpoint);

        printResponse("Read Organization Feature", response);

        assertEquals(response.getStatusCode(), 200, "Read Organization Feature API failed");
        assertEquals(response.jsonPath().getString("result.feature_code"), featureCode, "Feature code mismatch");
        assertEquals(response.jsonPath().getString("result.feature_name"), featureLabel, "Feature name mismatch");

        logger.info("Organization Feature Read Successfully");
    }

    @Test(dependsOnMethods = "testReadOrganizationFeatureMapping")
    public void testListOrganizationFeatureMappings() {
        logger.info("Started Organization Feature List API");

        Response response = given()
                .header("X-auth-token", adminToken)
                .header("x-org-code", "tripura")
                .header("x-tenant-code", "shikshagrahanew")
                .contentType(ContentType.JSON)
                .when()
                .get(PROP_LIST.getProperty("userservice.organization.feature.list.endpoint"));

        printResponse("Organization Feature List", response);

        assertEquals(response.getStatusCode(), 200, "Organization Feature List API failed");
        assertTrue(response.getBody().asString().contains(featureCode), "Created feature code not in org list");
        assertTrue(response.getBody().asString().contains(featureLabel), "Created feature name not in org list");

        logger.info("Organization Feature List API validated successfully");
    }

    @Test(dependsOnMethods = "testListOrganizationFeatureMappings")
    public void testDeleteOrganizationFeatureMapping() {
        logger.info("Started Delete Organization Feature API");

        String endpoint = PROP_LIST.getProperty("userservice.organization.feature.delete.endpoint") + "/{featureCode}";

        Response response = given()
                .header("X-auth-token", adminToken)
                .header("x-org-code", "tripura")
                .header("x-tenant-code", "shikshagrahanew")
                .pathParam("featureCode", featureCode)
                .contentType(ContentType.JSON)
                .when()
                .delete(endpoint);

        printResponse("Delete Organization Feature", response);

        assertEquals(response.getStatusCode(), 200, "Delete Organization Feature API failed");
        assertEquals(response.jsonPath().getString("message"), "Organization Feature deleted successfully", "Message mismatch");

        logger.info("Organization Feature Deleted Successfully");
    }

    @Test(dependsOnMethods = "testDeleteOrganizationFeatureMapping")
    public void testListFeatures() {
        logger.info("Started Feature List API");

        Response response = given()
                .header("X-auth-token", adminToken)
                .header("x-org-code", "tripura")
                .header("x-tenant-code", "shikshagrahanew")
                .queryParam("search", "")
                .contentType(ContentType.JSON)
                .when()
                .get(PROP_LIST.getProperty("userservice.feature.list.endpoint"));

        printResponse("Feature List", response);

        assertEquals(response.getStatusCode(), 200, "Feature List API failed");
        assertTrue(response.getBody().asString().contains(featureCode), "Feature code not in list");
        assertTrue(response.getBody().asString().contains(featureLabel), "Feature label not in list");

        logger.info("Feature still exists in Main Feature Table : {}", featureCode);
    }

    @Test(dependsOnMethods = "testListFeatures")
    public void testDeleteFeature() {
        logger.info("Started Delete Feature API");

        String endpoint = PROP_LIST.getProperty("userservice.delete.feature.endpoint") + "/{featureCode}";

        logger.info("Deleting Main Feature : {}", featureCode);

        Response response = given()
                .header("X-auth-token", adminToken)
                .header("x-tenant-code", "shikshagrahanew")
                .header("x-org-code", "tripura")
                .pathParam("featureCode", featureCode)
                .contentType(ContentType.JSON)
                .when()
                .delete(endpoint);

        printResponse("Delete Feature", response);

        assertEquals(response.getStatusCode(), 200, "Delete Feature API failed");
        assertEquals(response.jsonPath().getString("message"), "FEATURE_DELETED_SUCCESSFULLY", "Delete response message mismatch");

        logger.info("Main Feature Deleted Successfully : {}", featureCode);
    }

    // --- Helper Payload Generators ---

    private Map<String, Object> getThemeMap() {
        Map<String, Object> theme = new HashMap<>();
        theme.put("primaryColor", "#572E91");
        theme.put("secondaryColor", "#FF9911");
        return theme;
    }

    private Map<String, Object> getMetaMap(String title) {
        Map<String, Object> meta = new HashMap<>();
        meta.put("url", "/ml/listing/project?type=project");
        meta.put("icon", "/assets/images/ic_project.png");
        meta.put("title", title);
        meta.put("theme", getThemeMap());
        return meta;
    }

    private HashMap<String, Object> getCreateFeatureBody() {
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("code", featureCode);
        requestBody.put("label", featureLabel);
        requestBody.put("description", featureDescription);
        requestBody.put("icon", null);
        requestBody.put("display_order", displayOrder);

        Map<String, Object> meta = getMetaMap("autoFeature");
        meta.put("sameOrigin", true);
        requestBody.put("meta", meta);

        logger.info("Create Request Body : {}", requestBody);
        return requestBody;
    }

    private HashMap<String, Object> getUpdateFeatureBody() {
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("label", featureLabel);
        requestBody.put("description", featureDescription);

        logger.info("Update Request Body : {}", requestBody);
        return requestBody;
    }

    private HashMap<String, Object> getOrganizationFeatureMappingBody() {
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("feature_name", featureLabel);
        requestBody.put("feature_code", featureCode);
        requestBody.put("enabled", true);
        requestBody.put("icon", null);
        requestBody.put("display_order", displayOrder);
        requestBody.put("meta", getMetaMap(featureLabel));

        logger.info("Organization Feature Mapping Request Body : {}", requestBody);
        return requestBody;
    }

    private HashMap<String, Object> getUpdateOrganizationFeatureMappingBody() {
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("feature_name", featureLabel);
        requestBody.put("enabled", true);
        requestBody.put("feature_code", featureCode);
        requestBody.put("icon", null);
        requestBody.put("display_order", displayOrder);
        requestBody.put("meta", getMetaMap(featureLabel));

        ArrayList<String> roles = new ArrayList<>();
        roles.add("mentee");
        roles.add("mentor");
        roles.add("session_manager");

        requestBody.put("roles", roles);

        logger.info("Update Organization Feature Request Body : {}", requestBody);
        return requestBody;
    }

    private void printResponse(String apiName, Response response) {
        logger.info("{} Status Code : {}", apiName, response.getStatusCode());
        logger.info("{} Response Body :\n{}", apiName, response.getBody().asPrettyString());
    }
}