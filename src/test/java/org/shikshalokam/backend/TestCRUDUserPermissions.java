package org.shikshalokam.backend;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.shikshalokam.backend.PropertyLoader.PROP_LIST;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TestCRUDUserPermissions extends MentorEDBaseTest {

    public static final Logger logger = LogManager.getLogger(org.shikshalokam.backend.TestCRUDUserPermissions.class);
    private URI createUserPermissionEndpoint, getUserPermissionsEndPoint, updateUserPermissionEndpoint, deleteUserPermissionEndpoint;
    private String createdPermissionID, permissionName, updatedPermName;

    @BeforeTest
    public void init() {
        logger.info("Logging into the application :");
        loginToMentorED(PROP_LIST.get("mentor.qa.sysadmin.login.user").toString(), PROP_LIST.get("mentor.qa.sysadmin.login.password").toString());

        createUserPermissionEndpoint = MentorBase.createURI("/user/v1/permissions/create");
        getUserPermissionsEndPoint = MentorBase.createURI("/user/v1/permissions/list");
        updateUserPermissionEndpoint = MentorBase.createURI("/user/v1/permissions/update/");
        deleteUserPermissionEndpoint = MentorBase.createURI("/user/v1/permissions/delete/");

        // Generate a random permission name
        permissionName = "permname" + RandomStringUtils.randomAlphabetic(10).toLowerCase();
        updatedPermName = "updatedpermname" + RandomStringUtils.randomAlphabetic(10).toLowerCase();
    }

    @Test(description = "Testing CREATION Of PERMISSION with valid data")
    public void testCreatePermission() {
        logger.info("Started calling ---- CreatePermission API");

        HashMap<String, Object> permissionMap = new HashMap<>();
        permissionMap.put("code", permissionName);
        permissionMap.put("module", "manage_user");

        // Example request type as a List (JSONObject handles this automatically)
        List<String> requestTypeList = new ArrayList<>();
        requestTypeList.add("POST");
        permissionMap.put("request_type", requestTypeList);

        permissionMap.put("api_path", "/mentoring/v1/entity/create");
        permissionMap.put("status", "ACTIVE");

        // Call the createPermission method
        Response response = createPermission(permissionMap);
        assertEquals(response.getStatusCode(), 201, "Permission creation failed with" + response.getStatusCode());
        createdPermissionID = String.valueOf(response.jsonPath().getInt("result.Id"));
        Assert.assertNotNull(createdPermissionID, "Created Permission ID should not be null");

        logger.info("Ended calling ---- CreatePermission API: with assertions completed.");
    }

    @Test(description = "Validates the negative use case for CREATE PERMISSION ")
    public void testCreatePermission_MissingRequiredFields() {
        logger.info("Started calling --------- CreatePermission API: Negative Test Missing Required Fields");

        HashMap<String, Object> permissionMap = new HashMap<>();
        List<String> requestTypeList = new ArrayList<>();

        logger.info("Calling permission creation when --CODE-- field is empty");
        permissionMap.put("code", "");
        permissionMap.put("module", "manage_user");
        requestTypeList.add("POST");
        permissionMap.put("request_type", requestTypeList);
        permissionMap.put("api_path", "/mentoring/v1/entity/create");
        permissionMap.put("status", "ACTIVE");
        // Call the createPermission method
        Response response = createPermission(permissionMap);
        logger.info("Response Code: {}, Response Body: {}", response.getStatusCode(), response.getBody().asString());
        assertEquals(response.getStatusCode(), 422, "Expected Bad Request for missing required fields - 422, but got " + response.getStatusCode());

        String responseBody = response.getBody().asString();
        assertTrue(responseBody.contains("Code field is empty") && responseBody.contains("Code is invalid, must not contain spaces"), "Expected code missing or invalid & not found in response");

        response = null;
        logger.info("Calling permission creation when --MODULE-- field is empty");
        permissionMap.clear();
        permissionMap.put("code", "create_perm");
        permissionMap.put("module", "");

        requestTypeList.clear();
        requestTypeList.add("POST");
        permissionMap.put("request_type", requestTypeList);

        permissionMap.put("api_path", "/mentoring/v1/entity/create");
        permissionMap.put("status", "ACTIVE");

        response = createPermission(permissionMap);
        logger.info("Response Code: {}, Response Body: {}", response.getStatusCode(), response.getBody().asString());
        assertEquals(response.getStatusCode(), 422, "Expected Bad Request for missing required fields - 422, but got " + response.getStatusCode());

        String respBody = response.getBody().asString();
        assertTrue(respBody.contains("Module field is empty") && respBody.contains("Module is invalid, must not contain spaces"), "Expected MODULE missing or invalid & not found in response");

        response = null;
        logger.info("Calling permission creation when --REQUEST TYPE-- field is empty");
        permissionMap.clear();
        permissionMap.put("code", "create_perm");
        permissionMap.put("module", "manage_user");

        requestTypeList.clear();
        requestTypeList.add(" ");
        permissionMap.put("request_type", requestTypeList);

        permissionMap.put("api_path", "/mentoring/v1/entity/create");
        permissionMap.put("status", "ACTIVE");

        response = createPermission(permissionMap);
        logger.info("Response Code: {}, Response Body: {}", response.getStatusCode(), response.getBody().asString());
        assertEquals(response.getStatusCode(), 422, "Expected Bad Request for missing required fields - 422, but got " + response.getStatusCode());

        String reqtype = response.getBody().asString();
        assertTrue(reqtype.contains("request_type is invalid, must be one of: GET,POST,PATCH,PUT,DELETE"), "Expected REQUEST TYPE missing or invalid & not found in response");

        response = null;
        logger.info("Calling permission creation when --API PATH-- field is empty");
        permissionMap.clear();
        permissionMap.put("code", "create_perm");
        permissionMap.put("module", "manage_user");

        requestTypeList.clear();
        requestTypeList.add("POST");
        permissionMap.put("request_type", requestTypeList);

        permissionMap.put("api_path", " ");
        permissionMap.put("status", "ACTIVE");

        response = createPermission(permissionMap);
        logger.info("Response Code: {}, Response Body: {}", response.getStatusCode(), response.getBody().asString());
        assertEquals(response.getStatusCode(), 422, "Expected Bad Request for missing required fields - 422, but got " + response.getStatusCode());

        String apires = response.getBody().asString();
        assertTrue(apires.contains("API Path is invalid"), apires);

        response = null;
        logger.info("Calling permission creation when --STATUS-- field is empty");
        permissionMap.clear();
        permissionMap.put("code", "create_perm");
        permissionMap.put("module", "manage_user");

        requestTypeList.clear();
        requestTypeList.add("POST");
        permissionMap.put("request_type", requestTypeList);

        permissionMap.put("api_path", "/mentoring/v1/entity/create");
        permissionMap.put("status", "@!@");

        response = createPermission(permissionMap);
        logger.info("Response Code: {}, Response Body: {}", response.getStatusCode(), response.getBody().asString());
        assertEquals(response.getStatusCode(), 422, response.getBody().asString());

        String apiRes = response.getBody().asString();
        assertTrue(apiRes.contains("Status is invalid, must not contain spaces"), "Expected message found " );

        logger.info("Ended calling --------------- CreatePermission API: Negative test completed with assertions on missing fields.");
    }

    @Test(dependsOnMethods = "testCreatePermission")
    public void testUpdatePermission() {
        logger.info("Started calling ---- UpdatePermission API");

        HashMap<String, Object> permissionMap = new HashMap<>();
        permissionMap.put("code", updatedPermName);
        permissionMap.put("module", "manage_user");

        List<String> requestTypeList = new ArrayList<>();
        requestTypeList.add("POST");
        permissionMap.put("request_type", requestTypeList);

        permissionMap.put("api_path", "/mentoring/v1/entity/create");
        permissionMap.put("status", "ACTIVE");

        // Call the updatePermission method
        Response response = updatePermission(permissionMap);
        logger.info("Response Code: {}, Response Body: {}", response.getStatusCode(), response.getBody().asString());
        assertEquals(response.getStatusCode(), 201, "Permission updation failed with " + response.getStatusCode());

        String apiRes = response.getBody().asString();
        assertTrue(apiRes.contains("PERMISSION_UPDATED_SUCCESSFULLY"), "Expected message found " );

        System.out.println("Updated Perm ID" + createdPermissionID);
        /**
         * Fetch the updated permission name from get api and compare with the created permission name
         * @todo BUG ID - 2014 under USER SERVICE, once fixed this below code can be uncommented and used accordingly
         */
//        Response responsebody = testGetUserPermissions(false, updatedPermName);
//        permissionName = responsebody.jsonPath().getString("result.data[0].title"); // check api path again
//        if (permissionName.equals(updatedPermName))
//            assertEquals(permissionName, updatedPermName, "Updated permission does not match the expected value.");
//        else
//            assertEquals(permissionName, updatedPermName, "permission code name not same");

        logger.info("Ended calling ---- UpdatePermission: with assertions completed.");
    }

    @Test(dependsOnMethods = "testCreatePermission")
    public void testUpdatePermission_MissingRequiredFields() {
        logger.info("Started calling ---- UpdatePermission: Negative Test Missing Required Fields");

        HashMap<String, Object> permissionMap = new HashMap<>();
        permissionMap.put("code", updatedPermName);
        permissionMap.put("module", "");

        List<String> requestTypeList = new ArrayList<>();
        requestTypeList.add("");
        permissionMap.put("request_type", requestTypeList);

        permissionMap.put("api_path", "/mentoring/v1/entity/create");
        permissionMap.put("status", "ACTIVE");

        // Call the updatePermission method
        Response response = updatePermission(permissionMap);
        logger.info("Response Code: {}, Response Body: {}", response.getStatusCode(), response.getBody().asString());
        assertEquals(response.getStatusCode(), 422, "Permission update failed with " + response.getStatusCode());

        System.out.println("Updated Perm ID successfully passed with  missing cases");
        logger.info("Ended calling ---- Update Permission: Negative test completed with assertions on missing fields");
    }

    @Test(description = "Fetches the list of available permissions already created")
    public void testGetUserPermissions() {
        logger.info("Started calling ---------- GET Permissions -------------");

        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .when().get(getUserPermissionsEndPoint);

        assertEquals(response.getStatusCode(), 200, "Status code failed for getting permissions");
        assertEquals(response.getStatusCode(),200,"Expected status code 200, but actual code"+response.getStatusCode());
        /**
         * Query parameter for searching specific string to retrieve details - throwing "Invalid search text"
         * hence we are not doing POST operation with search=string query parameter for now.
         * @todo BUG ID - 2013 under USER SERVICE, once fixed by dev change the post request to pass with search value in api
         */
        int permissionCount = response.jsonPath().getInt("result.results.count");
        assertTrue(permissionCount > 0, "Expected permission count to be greater than zero");
        logger.info("Total permissions fetched: " + permissionCount);

        logger.info("Ended calling ---- GET Permissions: with assertions completed");
    }

    @Test(dependsOnMethods = "testUpdatePermission")
    public void testDeletePermission() {
        logger.info("Started calling ----------- Delete Permissions ------------");
        System.out.println("Deleted Perm ID will be" + createdPermissionID);
        Response response = deletePermission();

        logger.info("Response Code: {}, Response Body: {}", response.getStatusCode(), response.getBody().asString());
        assertEquals(response.getStatusCode(), 202, "Expected status code 202 for successful deletion, got" + response.getStatusCode());

        String apiRes = response.getBody().asString();
        assertTrue(apiRes.contains("PERMISSION_DELETED_SUCCESSFULLY"), "Expected message found " );

        System.out.println("Deleted Perm ID successfully");
        /**
         * Once Bug ID - 2013 is fixed , can uncomment the below code to fetch the response from get req & validate the deleted ID
         */
        // Optionally, try to get the deleted role to ensure it no longer exists
//        Response permExists = testGetUserPermissions(true, permissionName);
//        if (permExists == null) {
//            logger.info("Verified: ID is no longer present in the system.");
//        } else {
//            logger.warn("ID -" + createdPermissionID + " still exists in the system.");
//        }

        logger.info("Ended calling ------------ Delete Permission: with assertions completed.");
    }

    private Response createPermission(HashMap<String, Object> permissionMap) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.putAll(permissionMap);

        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .contentType(ContentType.JSON)
                .body(jsonObject).log().all()
                .when().post(createUserPermissionEndpoint);
        return response;
    }

    private Response updatePermission(HashMap<String, Object> permissionMap) {
        JSONObject requestBodyJson = new JSONObject();
        requestBodyJson.putAll(permissionMap);

        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .pathParams("id",createdPermissionID)
                .contentType(ContentType.JSON)
                .body(requestBodyJson.toString())
                .when().post(updateUserPermissionEndpoint+"{id}");
        return response;
    }

    private Response deletePermission() {
        Response response = given()
                .header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                .pathParam("id",createdPermissionID)
                .contentType(ContentType.JSON)
                .when().delete(deleteUserPermissionEndpoint+"{id}");
        return response;
    }
}


