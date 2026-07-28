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
import static org.testng.Assert.*;

public class TestFeatureCRUDOperations extends UserServiceBaseTest {

    private static final Logger logger = LogManager.getLogger(TestFeatureCRUDOperations.class);

    private String adminToken;
    private String featureCode;
    private String featureLabel;
    private String featureDescription;
    private int displayOrder;

    @BeforeClass
    public void init() {

        adminToken = CommonUtilityUserService.generateAdminToken();

        if (adminToken == null || adminToken.isEmpty()) {
            fail("Admin Token Generation Failed");
        }

        String random = RandomStringUtils.randomAlphabetic(6).toLowerCase();

        featureCode = "autofeature" + random;
        featureLabel = featureCode;
        featureDescription = "Project capability feature check";
        displayOrder = 16;

        logger.info("Feature Code: {}", featureCode);
    }

    @Test
    public void testCreateFeature() {

        Response response = given().header("X-auth-token", adminToken)
                .header("x-org-code", getFeatureOrgCode())
                .header("x-tenant-code", getFeatureTenantCode())
                .contentType(ContentType.JSON)
                .body(getCreateFeatureBody())
                .when()
                .post(PROP_LIST.getProperty("userservice.create.feature.endpoint"));

        printResponse("Create Feature", response);

        assertEquals(response.getStatusCode(), 201, "Create Feature API failed");

        assertEquals(response.jsonPath().getString("message"), "Feature created successfully", "Create Feature message mismatch");

        assertEquals(response.jsonPath().getString("result.code"), featureCode, "Feature code mismatch");

        assertEquals(response.jsonPath().getString("result.label"), featureLabel, "Feature label mismatch");
    }

    @Test(dependsOnMethods = "testCreateFeature")
    public void testUpdateFeature() {

        featureLabel = featureLabel + "Updated";
        featureDescription = "Updated Project capability feature";

        String endpoint = PROP_LIST.getProperty("userservice.update.feature.endpoint") + "/{featureCode}";

        Response response = given().header("X-auth-token", adminToken)
                .header("x-org-code", getFeatureOrgCode())
                .header("x-tenant-code", getFeatureTenantCode())
                .pathParam("featureCode", featureCode)
                .contentType(ContentType.JSON)
                .body(getUpdateFeatureBody())
                .when()
                .post(endpoint);

        printResponse("Update Feature", response);

        assertEquals(response.getStatusCode(), 200, "Update Feature API failed");

        assertEquals(response.jsonPath().getString("result.label"), featureLabel, "Updated feature label mismatch");

        assertEquals(response.jsonPath().getString("result.description"), featureDescription, "Updated feature description mismatch");
    }

    @Test
    public void testListFeatures() {
        Response response = given().header("X-auth-token", adminToken)
                .header("x-org-code", getFeatureOrgCode())
                .header("x-tenant-code", getFeatureTenantCode())
                .queryParam("search", "")
                .contentType(ContentType.JSON)
                .when()
                .get(PROP_LIST.getProperty("userservice.feature.list.endpoint"));

        printResponse("Feature List", response);

        assertEquals(response.getStatusCode(), 200, "Feature List API failed");

        assertNotNull(response.getBody(), "Feature List response body should not be null");
    }

    @Test(dependsOnMethods = "testUpdateFeature")
    public void testDeleteFeature() {

        String endpoint = PROP_LIST.getProperty("userservice.delete.feature.endpoint") + "/{featureCode}";

        Response response = getFeatureRequest().pathParam("featureCode", featureCode).when().delete(endpoint);

        printResponse("Delete Feature", response);

        assertEquals(response.getStatusCode(), 200, "Delete Feature API failed");

        assertEquals(response.jsonPath().getString("message"), "FEATURE_DELETED_SUCCESSFULLY", "Delete Feature response message mismatch");
    }

    @Test(dependsOnMethods = "testCreateFeature")
    public void testMapFeatureToOrganization() {

        Response response = getOrganizationFeatureRequest()
                .body(getOrganizationFeatureMappingBody())
                .when()
                .post(PROP_LIST.getProperty("userservice.organization.feature.mapping.endpoint"));

        printResponse("Map Feature To Organization", response);

        assertEquals(response.getStatusCode(), 201, "Organization Feature Mapping API failed");

        assertEquals(response.jsonPath().getString("result.feature_code"), featureCode, "Feature code mismatch");

        assertEquals(response.jsonPath().getString("result.feature_name"), featureLabel, "Feature label mismatch");
    }

    @Test(dependsOnMethods = "testMapFeatureToOrganization")
    public void testUpdateOrganizationFeatureMapping() {

        String endpoint = PROP_LIST.getProperty("userservice.organization.feature.update.endpoint") + "/{featureCode}";

        Response response = getOrganizationFeatureRequest().pathParam("featureCode", featureCode)
                .contentType(ContentType.JSON)
                .body(getUpdateOrganizationFeatureMappingBody())
                .when()
                .patch(endpoint);

        printResponse("Update Organization Feature", response);

        assertEquals(response.getStatusCode(), 200, "Update Organization Feature API failed");

        assertEquals(response.jsonPath().getString("result.feature_code"), featureCode, "Feature code mismatch");

        assertEquals(response.jsonPath().getString("result.feature_name"), featureLabel, "Feature name mismatch");
    }

    @Test(dependsOnMethods = "testMapFeatureToOrganization")
    public void testReadOrganizationFeatureMapping() {

        String endpoint = PROP_LIST.getProperty("userservice.organization.feature.read.endpoint") + "/{featureCode}";

        Response response = getOrganizationFeatureRequest().pathParam("featureCode", featureCode)
                .when()
                .get(endpoint);

        printResponse("Read Organization Feature", response);

        assertEquals(response.getStatusCode(), 200, "Read Organization Feature API failed");

        assertEquals(response.jsonPath().getString("result.feature_code"), featureCode, "Feature code mismatch");

        assertEquals(response.jsonPath().getString("result.feature_name"), featureLabel, "Feature name mismatch");
    }

    @Test
    public void testListOrganizationFeatureMappings() {

        Response response = getOrganizationFeatureRequest()
                .when()
                .get(PROP_LIST.getProperty("userservice.organization.feature.list.endpoint"));

        printResponse("List Organization Feature Mappings", response);

        assertEquals(response.getStatusCode(), 200, "Organization Feature List API failed");
    }

    @Test(dependsOnMethods = "testUpdateOrganizationFeatureMapping")
    public void testDeleteOrganizationFeatureMapping() {

        String endpoint = PROP_LIST.getProperty("userservice.organization.feature.delete.endpoint") + "/{featureCode}";

        Response response = getOrganizationFeatureRequest()
                .pathParam("featureCode", featureCode)
                .when()
                .delete(endpoint);

        printResponse("Delete Organization Feature", response);

        assertEquals(response.getStatusCode(), 200, "Delete Organization Feature API failed");

        assertEquals(response.jsonPath().getString("message"), "Organization Feature deleted successfully", "Delete Organization Feature message mismatch");
    }

    private io.restassured.specification.RequestSpecification getFeatureRequest() {

        return given().header("X-auth-token", adminToken)
                .header("x-org-code", getFeatureOrgCode())
                .header("x-tenant-code", getFeatureTenantCode())
                .contentType(ContentType.JSON);
    }

    private io.restassured.specification.RequestSpecification getOrganizationFeatureRequest() {

        return given().header("X-auth-token", adminToken)
                .header("x-org-code", getFeatureOrgCode())
                .header("x-tenant-code", getFeatureTenantCode())
                .contentType(ContentType.JSON);
    }

    private String getFeatureTenantCode() {

        String tenantCode = PROP_LIST.getProperty("userservice.feature.tenant.code");

        if (tenantCode == null || tenantCode.trim().isEmpty()) {
            fail("userservice.feature.tenant.code is missing " + "from automation.properties");
        }

        return tenantCode;
    }

    private String getFeatureOrgCode() {

        String orgCode = PROP_LIST.getProperty("userservice.feature.org.code");

        if (orgCode == null || orgCode.trim().isEmpty()) {
            fail("userservice.feature.org.code is missing " + "from automation.properties");
        }

        return orgCode;
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

        return requestBody;
    }

    private HashMap<String, Object> getUpdateFeatureBody() {

        HashMap<String, Object> requestBody = new HashMap<>();

        requestBody.put("label", featureLabel);

        requestBody.put("description", featureDescription);

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

        return requestBody;
    }

    private Map<String, Object> getMetaMap(String title) {

        Map<String, Object> meta = new HashMap<>();

        meta.put("url", "/ml/listing/project?type=project");

        meta.put("icon", "/assets/images/ic_project.png");

        meta.put("title", title);

        meta.put("sameOrigin", true);

        meta.put("theme", getThemeMap());

        return meta;
    }

    private Map<String, Object> getThemeMap() {

        Map<String, Object> theme = new HashMap<>();

        theme.put("primaryColor", "#572E91");

        theme.put("secondaryColor", "#FF9911");

        return theme;
    }

    private void printResponse(String apiName, Response response) {

        logger.info("{} Status Code: {}", apiName, response.getStatusCode());

        logger.info("{} Response: {}", apiName, response.asPrettyString());
    }
}