package org.shikshalokam.backend.ep;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.shikshalokam.backend.PropertyLoader.PROP_LIST;
import static org.testng.Assert.*;

public class TestCRUDUserRoleExtensionOperations extends ElevateProjectBaseTest {

    public static final Logger logger = LogManager.getLogger(TestCRUDUserRoleExtensionOperations.class);
    private String updatedRoleTitle = "Principal" + RandomStringUtils.randomAlphabetic(3).toLowerCase();
    private String userRoleId = RandomStringUtils.randomAlphabetic(10);
    private String userRoleTitle = "userRoleExt" + RandomStringUtils.randomAlphabetic(10).toLowerCase();

    @BeforeMethod
    public void login() {
        logger.info("Logging into the application :");
        loginToElevate(PROP_LIST.get("elevate.login.contentcreator").toString(), PROP_LIST.get("elevate.login.contentcreator.password").toString());
    }

    @Test(description = "Creates user role extension with given parameters")
    public void testCreateValidUserRoleExtension() {
        Response response = createUserRoleExtension(userRoleTitle, userRoleId, PROP_LIST.getProperty("elevate.qa.entityType"), PROP_LIST.getProperty("elevate.qa.entityTypeId"));
        logger.info("Response Code: {}, Response Body: {}", response.getStatusCode(), response.getBody().asString());
        assertEquals(response.getStatusCode(), 200, "User role extension creation failed with " + response.getStatusCode());
        assertNotNull(createdRoleID, "Role ID not found in the response");
        logger.info("Ended CreateUserRoleExtension with assertions completed");
        deleteUserRoleExtension(createdRoleID);
    }

    @Test(description = "creating user role extension by passing empty values")
    public void testCreateInvalidUserRoleExtension() {
        Response response = createUserRoleExtension("", "", PROP_LIST.getProperty("elevate.qa.entityType"), PROP_LIST.getProperty("elevate.qa.entityTypeId"));
        logger.info("Response Code: {}, Response Body: {}", response.getStatusCode(), response.getBody().asString());
        assertEquals(response.getStatusCode(), 400, "User role extension creation failed with " + response.getStatusCode());
        assertTrue(response.asString().contains("The title field cannot be empty."));
        logger.info("Ended CreateUserRoleExtension with assertions completed");
    }

    @Test(description = "Updates the user role extension")
    public void testValidUpdateUserRoleExtension() {
        createUserRoleExtension(userRoleTitle, userRoleId, PROP_LIST.getProperty("elevate.qa.entityType"), PROP_LIST.getProperty("elevate.qa.entityTypeId"));
        Response response = updateUserRoleExtension(updatedRoleTitle, PROP_LIST.getProperty("elevate.qa.entityType"), PROP_LIST.getProperty("elevate.qa.entityTypeId"));
        logger.info("Response Code: {}, Response Body: {}", response.getStatusCode(), response.getBody().asString());
        assertEquals(response.getStatusCode(), 200, "User role extension update failed with " + response.getStatusCode());
        assertEquals(response.jsonPath().getString("message"), "USER_ROLE_UPDATATED");
        Response responseFind = findUserRoleExtension();
        assertEquals(responseFind.getStatusCode(), 200, "User role extension update failed with " + response.getStatusCode());
        assertTrue(responseFind.getBody().asString().contains(updatedRoleTitle));
        logger.info("Ended UpdateUserRoleExtension with assertions completed");
    }

    @Test(dependsOnMethods = "testCreateValidUserRoleExtension", description = "Finds the user role extension by status")
    public void testFindUserRoleExtension() {
        createUserRoleExtension(userRoleTitle, userRoleId, PROP_LIST.getProperty("elevate.qa.entityType"), PROP_LIST.getProperty("elevate.qa.entityTypeId"));
        Response response = findUserRoleExtension();
        logger.info("Response Code: {}, Response Body: {}", response.getStatusCode(), response.getBody().asString());
        assertEquals(response.getStatusCode(), 200, "User role extension find request failed with " + response.getStatusCode());
        assertTrue(response.getBody().asString().contains(userRoleTitle), "Expected role title 'Principal' not found in the response.");
        logger.info("Ended FindUserRoleExtension with assertions completed");
        deleteUserRoleExtension(createdRoleID);
    }

    @Test(description = "Deletes the user role extension")
    public void testDeleteUserRoleExtension() {
        Response response = createUserRoleExtension(userRoleTitle, userRoleId, PROP_LIST.getProperty("elevate.qa.entityType"), PROP_LIST.getProperty("elevate.qa.entityTypeId"));
        logger.info("Response Code: {}, Response Body: {}", response.getStatusCode(), response.getBody().asString());
        createdRoleID = response.jsonPath().getString("result._id");
        Response responseDelete = deleteUserRoleExtension(createdRoleID);
        logger.info("Response Code: {}, Response Body: {}", responseDelete.getStatusCode(), responseDelete.getBody().asString());
        assertEquals(responseDelete.getStatusCode(), 200, "User role extension deletion failed with " + responseDelete.getStatusCode());
        logger.info("Ended DeleteUserRoleExtension with assertions completed");
    }

    private Response updateUserRoleExtension(String title, String entityType, String entityTypeId) {
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", title);
        requestBody.put("entityTypes", List.of(new HashMap<String, String>() {{
            put("entityType", entityType);
            put("entityTypeId", entityTypeId);
        }}));
        Response response = given()
                .header("X-auth-token", X_AUTH_TOKEN)
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().pathParam("_id", createdRoleID).post((String) PROP_LIST.get("updateUserRoleExtensionEndpoint") + "{_id}"); // Use the updated URL here
        response.prettyPrint();
        return response;
    }

    private Response findUserRoleExtension() {
        HashMap<String, Object> requestBody = new HashMap<>();
        HashMap<String, String> query = new HashMap<>();
        query.put("status", "ACTIVE");
        requestBody.put("query", query);
        requestBody.put("projection", List.of("all"));
        Response response = given()
                .header("X-auth-token", X_AUTH_TOKEN)
                .header("internal-access-token", INTERNAL_ACCESS_TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody).when().post(PROP_LIST.getProperty("findUserRoleExtensionEndpoint"));
        response.prettyPrint();
        return response;
    }
}