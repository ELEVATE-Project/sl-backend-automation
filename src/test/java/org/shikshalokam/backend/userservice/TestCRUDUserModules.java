package org.shikshalokam.backend.userservice;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;
import org.shikshalokam.backend.userServiceUtility.CommonUtilityUserService;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import static io.restassured.RestAssured.given;
import static org.shikshalokam.backend.userServiceUtility.CommonUtilityUserService.loginAsTenantAdmin;
import static org.shikshalokam.backend.userServiceUtility.CommonUtilityUserService.loginTOAdmin;
import static org.testng.Assert.*;

public class TestCRUDUserModules extends UserServiceBaseTest {

    public static final Logger logger = LogManager.getLogger(TestCRUDUserModules.class);
    private URI createUserModulesEndpoint, getUserModulesEndpoint, updateUserModulesEndpoint, deleteUserModulesEndpoint;
    private String token,userModuleCodeName, createdModuleID,updateUserModuleCode;
    HashMap<String, String> databody = new HashMap<>();

    @BeforeTest
    @org.testng.annotations.Parameters({"role"})
    public void init(String role) throws Exception {
        logger.info("Running tests for user modules: " + role);

        if (role.equalsIgnoreCase("admin")) {
            loginTOAdmin(
                    PropertyLoader.PROP_LIST.getProperty("userservice.qa.admin.login.user"),
                    PropertyLoader.PROP_LIST.getProperty("userservice.qa.admin.login.password")
            );
            token = CommonUtilityUserService.adminToken;

        } else if (role.equalsIgnoreCase("tenantAdmin")) {
            loginAsTenantAdmin(
                    PropertyLoader.PROP_LIST.getProperty("userservice.qa.tenantadmin.login.user"),
                    PropertyLoader.PROP_LIST.getProperty("userservice.qa.tenantadmin.login.password")
            );
            token = CommonUtilityUserService.tenantAdminToken;

        } else {
            assertTrue(
                    role.equalsIgnoreCase("admin") || role.equalsIgnoreCase("tenantAdmin"),
                    "Invalid role passed: " + role);
        }
        assertNotNull(token, "Token is null for role: " + role);
        assertFalse(token.isEmpty(), "Token is empty for role: " + role);

        createUserModulesEndpoint = new URI("/user/v1/modules/create");
        getUserModulesEndpoint = new URI("/user/v1/modules/list");
        updateUserModulesEndpoint = new URI("/user/v1/modules/update/");
        deleteUserModulesEndpoint = new URI("/user/v1/modules/delete/");

        userModuleCodeName = "userModuleCode" + RandomStringUtils.randomAlphabetic(10).toLowerCase();
        updateUserModuleCode = "updUserModuleCode" + RandomStringUtils.randomAlphabetic(10).toLowerCase();
    }

    @Test(description = "Test for Creation of User Module with given code title")
    public void testCreateUserModules(){
        logger.info("Started calling ------------ Create User Modules API ------------");
        Response response = createUserModules(userModuleCodeName);

        logger.info(
                "\nResponse Code : {}" +
                        "\nMessage       : {}" +
                        "\nModule ID     : {}" +
                        "\nCode          : {}" +
                        "\nStatus        : {}",
                response.getStatusCode(),
                response.jsonPath().getString("message"),
                response.jsonPath().getInt("result.Id"),
                response.jsonPath().getString("result.code"),
                response.jsonPath().getString("result.status")
        );

        assertEquals(response.getStatusCode(), 201, "User Module creation failed with" + response.getStatusCode());
        createdModuleID = response.jsonPath().getString("result.Id");
        assertNotNull(createdModuleID, "Module ID not found in the response");

        // Optional: Verify the module exists via GET
        Response getResponse = getUserModules(false, userModuleCodeName);
        assertEquals(getResponse.getStatusCode(), 200);

        logger.info("Ended calling -------- Create User Modules API : with assertions completed");
    }

    @Test(description = "Validates the negative use case of CREATE USER MODULES ")
    public void testCreateUserModules_MissingRequiredFields()
    {
        logger.info("Started calling --------- Create User Modules API: Negative Test Missing Required Fields");
        Response response = createUserModules("");

        logger.info("Response Code: {}", response.getStatusCode());
        assertEquals(response.getStatusCode(), 422, "Expected Bad Request for missing required fields, but got " + response.getStatusCode());

        String responseBody = response.getBody().asString();
        assertTrue(responseBody.contains("code field is empty") && responseBody.contains ("code is invalid, must not contain spaces"),
                "Expected error messages for missing code or invalid code not found in response"+responseBody);

        logger.info("Ended calling ----------- Create User Modules API: Negative test completed with assertions on missing fields.");
    }

    @Test(dependsOnMethods = "testCreateUserModules", description = "Test for Updation of User Module with given code title")
    public void testUpdateUserModules(){
        logger.info("Started calling ------------ Update User Modules API ------------");
        Response response = updateUserModules(updateUserModuleCode);

        logger.info(
                "\nResponse Code : {}" +
                        "\nMessage       : {}" +
                        "\nModule ID     : {}" +
                        "\nCode          : {}" +
                        "\nStatus        : {}",
                response.getStatusCode(),
                response.jsonPath().getString("message"),
                response.jsonPath().getInt("result.id"),
                response.jsonPath().getString("result.code"),
                response.jsonPath().getString("result.status")
        );

        assertEquals(response.getStatusCode(), 201, "User Module updation failed with" + response.getStatusCode());

        Response responsebody = getUserModules(false, updateUserModuleCode);
        userModuleCodeName = responsebody.jsonPath().getString("result.data[0].code");

        if (userModuleCodeName.equals(updateUserModuleCode))
            assertEquals(userModuleCodeName,updateUserModuleCode,"Code name does not match the expected Updated code name.");
        else
            assertEquals(userModuleCodeName,updateUserModuleCode,"Code names not same, update worked !");

        logger.info("Ended calling -------- Update User Modules API : with assertions completed");
    }

    @Test(dependsOnMethods = "testCreateUserModules", description = "Validates the negative use case of UPDATE USER MODULES ")
    public void testUpdateUserModules_MissingRequiredFields()
    {
        logger.info("Started calling --------- Update User Modules API: Negative Test Missing Required Fields");
        Response response = updateUserModules(" ");

        logger.info("Response Code: {}", response.getStatusCode());
        assertEquals(response.getStatusCode(), 422, "Expected Bad Request for missing required fields, but got " + response.getStatusCode());

        String responseBody = response.getBody().asString();
        assertTrue(responseBody.contains("code field is empty") && responseBody.contains ("code is invalid, must not contain spaces"),
                "Expected error messages for missing code or invalid code not found in response"+responseBody);

        logger.info("Ended calling ----------- Update User Modules API: Negative test completed with assertions on missing fields.");
    }

    @Test(dependsOnMethods = "testUpdateUserModules_MissingRequiredFields", description = "Test for deletion of User Module with retrieved module ID ")
    public void testDeleteModules() {
        logger.info("Started calling ----------- Delete User Modules ------------");
        Response response = deleteUserModules();

        logger.info("Response Code: {}", response.getStatusCode());
        assertEquals(response.getStatusCode(), 202, "Expected status code 202 for successful deletion, got" + response.getStatusCode());

        // Optionally, try to get the deleted role to ensure it no longer exists
        Response codeExists = getUserModules(false, updateUserModuleCode);

        List<?> data = codeExists.jsonPath().getList("result.data");
        if (data == null || data.isEmpty()) {
            logger.info("Verified: ID is no longer present in the system.");
        } else {
            logger.warn("ID -" + createdModuleID + " still exists in the system.");
        }

        logger.info("Ended calling ------------ Delete User Modules API: with assertions completed.");
    }

    private Response createUserModules(String code) {
        databody.clear();
        databody.put("code", code);

        Response response = given()
                .header("X-auth-token", token)
                .contentType(ContentType.JSON)
                .body(databody)
                .when().post(createUserModulesEndpoint);

               return response;
    }

    private Response updateUserModules(String code){
        databody.clear();
        databody.put("code", code);

        Response response = given()
                .header("X-auth-token", token)
                .pathParam("id",createdModuleID)
                .contentType(ContentType.JSON)
                .body(databody)
                .when().post(updateUserModulesEndpoint+"{id}");

        return response;
    }

    private Response getUserModules(boolean idNeeded, String code) {
        logger.info("Started calling ---------- GET User Module API -------------");

        Response response = given()
                .header("X-auth-token", token)
                .queryParam("search", code)
                .when().get(getUserModulesEndpoint);

        return response; // If idNeeded is false, return the full response
    }

    private Response deleteUserModules() {
        Response response = given()
                .header("X-auth-token", token)
                .pathParam("id",createdModuleID)
                .contentType(ContentType.JSON)
                .when().delete(deleteUserModulesEndpoint+"{id}");
        return response;
    }
}
