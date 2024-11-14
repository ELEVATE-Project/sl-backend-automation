package org.shikshalokam.backend;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.net.URI;
import java.util.HashMap;
import static org.shikshalokam.backend.PropertyLoader.PROP_LIST;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TestRolePermissionMapping extends MentorEDBaseTest{

    public static final Logger logger = LogManager.getLogger(TestRolePermissionMapping.class);
    private URI createPermissionMappingEndpoint, deletePermissionMappingEndpoint;
    HashMap<String, Object> permissionMap = new HashMap<>();

    @BeforeTest
    public void init() {
        logger.info("Logging into the application :");
        loginToMentorED(PROP_LIST.get("mentor.qa.sysadmin.login.user").toString(), PROP_LIST.get("mentor.qa.sysadmin.login.password").toString());

        createPermissionMappingEndpoint = MentorBase.createURI("/user/v1/role-permission-mapping/create/");
        deletePermissionMappingEndpoint = MentorBase.createURI("/user/v1/role-permission-mapping/delete/");
    }

    @Test(description = "Test for Creation of Permission Mapping")
    public void testCreatePermissionMapping(){
        logger.info("Started calling ------------ Create Permission Mapping API ------------");

        permissionMap.clear();
        permissionMap.put("role_title",PROP_LIST.getProperty("userService.userRole.createdTitle"));
        permissionMap.put("permission_id", PROP_LIST.getProperty("userService.userPermissions.createdPermissionId"));

        Response response = createPermissionMapping(permissionMap);
        logger.info("Response Code: {}", response.getStatusCode());
        assertEquals(response.getStatusCode(), 201, "Permission Mapping failed with" + response.getStatusCode());

        String apiRes = response.getBody().asString();
        assertTrue(apiRes.contains("ROLE_PERMISSION_CREATED_SUCCESSFULLY"), "Expected message not found" );

        logger.info("Ended calling -------- Create Permission Mapping API : with assertions completed");
    }

    @Test(description = "Validates the negative use case for CREATE PERMISSION MAPPING", dependsOnMethods = "testCreatePermissionMapping")
    public void testCreatePermissionMapping_MissingRequiredFields(){
        logger.info("Started calling ------------ Create Permission Mapping API ------------");

        logger.info("Calling Permission Mapping when --ROLE TITLE-- field is empty");
        permissionMap.clear();
        permissionMap.put("role_title","");
        permissionMap.put("permission_id", PROP_LIST.getProperty("userService.userPermissions.createdPermissionId"));

        Response response = createPermissionMapping(permissionMap);
        logger.info("Response Code: {}", response.getStatusCode());
        assertEquals(response.getStatusCode(), 422, "Expected Permission Mapping to be failed for missing required fields with bad request- 422, but got" + response.getStatusCode());

        String responseBody = response.getBody().asString();
        assertTrue(responseBody.contains("role_title field is empty") && responseBody.contains("role_title is invalid, must not contain spaces"), "Expected role_title missing or invalid & not found in response");

        response = null;
        logger.info("Calling Permission Mapping when --ROLE TITLE-- field is null");
        permissionMap.clear();
        permissionMap.put("role_title",null);
        permissionMap.put("permission_id", PROP_LIST.getProperty("userService.userPermissions.createdPermissionId"));

        response = createPermissionMapping(permissionMap);
        logger.info("Response Code: {}", response.getStatusCode());
        assertEquals(response.getStatusCode(), 422, "Expected Permission Mapping to be failed for missing required fields with bad request- 422, but got" + response.getStatusCode());

        response = null;
        logger.info("Calling Permission Mapping when --PERMISSION ID-- field is empty");
        permissionMap.clear();
        permissionMap.put("role_title",PROP_LIST.getProperty("userService.userRole.createdTitle"));
        permissionMap.put("permission_id", "");

        response = createPermissionMapping(permissionMap);
        logger.info("Response Code: {}", response.getStatusCode());
        assertEquals(response.getStatusCode(), 422, "Expected Permission Mapping to be failed for missing required fields with bad request- 422, but got" + response.getStatusCode());

        String res = response.getBody().asString();
        assertTrue(res.contains("permission_id field is empty") && res.contains("permission_id is invalid, must not contain spaces"), "Expected permission_id missing or invalid & not found in response");

        response = null;
        logger.info("Calling Permission Mapping when --PERMISSION ID-- field is null");
        permissionMap.clear();
        permissionMap.put("role_title",PROP_LIST.getProperty("userService.userRole.createdTitle"));
        permissionMap.put("permission_id", null);

        response = createPermissionMapping(permissionMap);
        logger.info("Response Code: {}", response.getStatusCode());
        assertEquals(response.getStatusCode(), 422, "Expected Permission Mapping to be failed for missing required fields with bad request- 400, but got" + response.getStatusCode());

        response = null;
        permissionMap.clear();
        logger.info("Ended calling -------- Create Permission Mapping API : with assertions completed");
    }

    @Test(description = "Test for DELETION OF PERMISSION MAPPING", dependsOnMethods = "testCreatePermissionMapping")
    public void testDeletePermissionMapping() {
        logger.info("Started calling ----------- Delete Permission Mapping API ------------");

        permissionMap.clear();
        permissionMap.put("role_title",PROP_LIST.getProperty("userService.userRole.createdTitle"));
        permissionMap.put("permission_id", PROP_LIST.getProperty("userService.userPermissions.createdPermissionId"));

        Response response = deletePermissionMapping(permissionMap);

        logger.info("Response Code: {}, Response Body: {}", response.getStatusCode(), response.getBody().asString());
        assertEquals(response.getStatusCode(), 201, "Expected status code 201 for successful deletion, got" + response.getStatusCode());

        String apiRes = response.getBody().asString();
        assertTrue(apiRes.contains("ROLE_PERMISSION_DELETED_SUCCESSFULLY"), "Expected message not found " );

        logger.info("Ended calling ------------ Delete Permission Mapping API: with assertions completed.");
    }

    @Test(description = "Validates the negative use case for DELETE PERMISSION MAPPING", dependsOnMethods = "testCreatePermissionMapping")
    public void testDeletePermissionMapping_MissingRequiredFields() {
        logger.info("Started calling ----------- Delete Permission Mapping API ------------");

        logger.info("Calling Delete Permission Mapping when --ROLE TITLE-- field is empty");
        permissionMap.clear();
        permissionMap.put("role_title","");
        permissionMap.put("permission_id", PROP_LIST.getProperty("userService.userPermissions.createdPermissionId"));

        Response response = createPermissionMapping(permissionMap);
        logger.info("Response Code: {}", response.getStatusCode());
        assertEquals(response.getStatusCode(), 422, "Expected status code 422 for successful deletion, got" + response.getStatusCode());

        String errorMessage = response.getBody().jsonPath().getString("message");
        assertEquals(errorMessage, "Validation failed, Entered data is incorrect!", "Expected message 'ROLE_PERMISSION_NOT_FOUND' but got: " + errorMessage);
        // assertEquals(errorMessage, "ROLE_PERMISSION_NOT_FOUND", "Expected message 'ROLE_PERMISSION_NOT_FOUND' but got: " + errorMessage);

        response = null;
        permissionMap.clear();
        logger.info("Calling Delete Permission Mapping when --PERMISSION ID-- field is empty");

        permissionMap.put("role_title",PROP_LIST.getProperty("userService.userRole.createdTitle"));
        permissionMap.put("permission_id", "");

        response = createPermissionMapping(permissionMap);
        logger.info("Response Code: {}", response.getStatusCode());
        assertEquals(response.getStatusCode(), 422, "Expected Permission Mapping to be failed for missing required fields with bad request- 422, but got" + response.getStatusCode());

        response = null;
        permissionMap.clear();
        logger.info("Ended calling ------------ Delete Permission Mapping API: with assertions completed.");
    }

    private Response createPermissionMapping(HashMap<String, Object> permissionMap) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(permissionMap);

        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .contentType(ContentType.JSON)
                .pathParam("id", PROP_LIST.get("userService.userRole.createdRoleId"))
                .body(jsonObject).log().all()
                .when().post(createPermissionMappingEndpoint+"{id}");

        return response;
    }

    private Response deletePermissionMapping(HashMap<String, Object> permissionMap) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(permissionMap);

        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .pathParam("id", PROP_LIST.get("userService.userRole.createdRoleId"))
                .contentType(ContentType.JSON)
                .body(jsonObject).log().all()
                .when().post(deletePermissionMappingEndpoint + "{id}");
        return response;
    }
}