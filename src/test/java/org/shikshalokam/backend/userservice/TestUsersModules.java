package org.shikshalokam.backend.userservice;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.userServiceUtility.CommonUtilityUserService;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.shikshalokam.backend.PropertyLoader.PROP_LIST;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TestUsersModules extends UserServiceBaseTest {

    public static final Logger logger = LogManager.getLogger(TestUsersModules.class);

    private String normalUserToken;
    private String adminToken;

    @BeforeMethod
    public void init() {
        normalUserToken = CommonUtilityUserService.loginToUser();
        adminToken = CommonUtilityUserService.loginAsAdmin();

        if (adminToken == null || adminToken.isEmpty()) {
            throw new RuntimeException("Admin Token is null");
        }

        if (normalUserToken == null || normalUserToken.isEmpty()) {
            throw new RuntimeException("User Token is null");
        }

        logger.info("Admin Token / User Token Generated Successfully");
    }

    @Test(description = "Validate Read User API")
    public void testReadUser() {

        Response response = readUser(normalUserToken);
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "Read User API failed");
    }

    @Test(description = "Negative Validation For Read User API")
    public void testReadUserWithInvalidToken() {

        Response response = readUser(PROP_LIST.get("userservice.invalid.x.auth.token").toString());
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 401, "Expected 401 for invalid token");
    }

    @Test(description = "Validate Get User Profile By ID API")
    public void testGetUserProfileById() {

        Response response = getUserProfileById(PROP_LIST.get("userservice.user.id").toString());
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 200, "Profile By ID API failed");
        assertTrue(response.getBody().asString().contains(PROP_LIST.get("userservice.user.id").toString()), "Expected user id not found");
    }

    @Test(description = "Negative Validation For Invalid User ID")
    public void testGetUserProfileByInvalidId() {

        Response response = getUserProfileById(PROP_LIST.get("userservice.invalid.user.id").toString());
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 404, "Expected 404 for invalid user id");
    }

    @Test(description = "Validate Update User API")
    public void testUpdateUser() {

        Response response = updateUser(PROP_LIST.get("userservice.update.user.name").toString(), adminToken);
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 202, "Update User API failed");
    }

    @Test(description = "Negative Validation For Invalid Token")
    public void testUpdateUserWithInvalidToken() {

        Response response = updateUser(PROP_LIST.get("userservice.update.user.name").toString(), PROP_LIST.get("userservice.invalid.x.auth.token").toString());
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 401, "Expected 401 for invalid token");
    }

    @Test(description = "Negative Validation For Empty Username")
    public void testUpdateUserWithEmptyName() {

        Response response = updateUser("", adminToken);
        response.prettyPrint();
        assertEquals(response.getStatusCode(), 422, "Expected 422 for empty username");
    }

    private Response readUser(String token) {

        return given().header("X-auth-token", token)
                      .when()
                      .get(URI.create(PROP_LIST.get("userservice.read.user.endpoint").toString()));
    }

    private Response getUserProfileById(String userId) {

        return given().header("internal_access_token", PROP_LIST
                      .get("userservice.internal.access.token").toString())
                      .when()
                      .get(PROP_LIST.get("userservice.profilebyid.endpoint").toString() + userId + "?tenant_code=default");
    }

    private Response updateUser(String name, String token) {

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("about", PROP_LIST.get("userservice.update.user.about").toString());
        requestBody.put("block", PROP_LIST.get("userservice.update.user.block").toString());
        requestBody.put("state", PROP_LIST.get("userservice.update.user.state").toString());
        requestBody.put("school", PROP_LIST.get("userservice.update.user.school").toString());
        requestBody.put("cluster", PROP_LIST.get("userservice.update.user.cluster").toString());
        requestBody.put("district", PROP_LIST.get("userservice.update.user.district").toString());
        requestBody.put("professional_role", PROP_LIST.get("userservice.update.user.professional.role").toString());

        ArrayList<String> professionalSubroles = new ArrayList<>();
        professionalSubroles.add(PROP_LIST.get("userservice.update.user.professional.subrole").toString());

        requestBody.put("professional_subroles", professionalSubroles);
        logger.info("Request Body : {}", requestBody);

        return given().header("X-auth-token", token).contentType(ContentType.JSON).body(requestBody).when().patch(URI.create(PROP_LIST.get("userservice.update.user.endpoint").toString()));
    }
}