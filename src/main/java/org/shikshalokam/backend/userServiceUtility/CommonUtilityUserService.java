package org.shikshalokam.backend.userServiceUtility;

import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;

import static io.restassured.RestAssured.given;

public class CommonUtilityUserService {

    public static String User_ID = null;
    public static String UserToken = null;
    public static String adminToken = null;

    static String baseUrl = fetchProperty("userservice.qa.api.base.url");

    static String origin;

    private static final Logger logger = LogManager.getLogger(CommonUtilityUserService.class);

    // Generate Normal User Token
    public static String generateNormalUserToken() {

        RestAssured.baseURI = baseUrl;

        boolean isSG = DerivingSystem();

        origin = isSG ? fetchProperty("ep.sg.origin") : fetchProperty("ep.sl.origin");

        Response response = given().contentType("application/x-www-form-urlencoded; charset=UTF-8").formParam("identifier", fetchProperty("userservice.qa.phone.login.identifier")).formParam("password", fetchProperty("userservice.qa.phone.login.password")).header("Origin", origin).when().post(fetchProperty("userservice.login.endpointasuser"));

        if (response.getStatusCode() != 200) {

            throw new RuntimeException("Normal User Login Failed");
        }

        String token = response.jsonPath().getString("result.access_token");

        if (token == null || token.isEmpty()) {

            throw new RuntimeException("Normal user token generation failed");
        }

        logger.info("Normal User Token Generated Successfully");

        return token;
    }

    // Generate Admin Token
    public static String generateAdminToken() {

        RestAssured.baseURI = baseUrl;

        boolean isSG = DerivingSystem();

        origin = isSG ? fetchProperty("ep.sg.origin") : fetchProperty("ep.sl.origin");

        Response response = given().contentType("application/x-www-form-urlencoded; charset=UTF-8").formParam("identifier", fetchProperty("userservice.qa.admin.login.user")).formParam("password", fetchProperty("userservice.qa.admin.login.password")).header("Origin", origin).when().post(fetchProperty("userservice.admin.login.endpoint"));

        if (response.getStatusCode() != 200) {

            throw new RuntimeException("Admin Login Failed");
        }

        String token = response.jsonPath().getString("result.access_token");

        if (token == null || token.isEmpty()) {

            throw new RuntimeException("Admin token generation failed");
        }

        logger.info("Admin Token Generated Successfully");

        return token;
    }

    // Existing Login Method
    public static void loginToUser(String loginId, String password) {

        RestAssured.baseURI = baseUrl;

        boolean isSG = DerivingSystem();

        origin = isSG ? fetchProperty("ep.sg.origin") : fetchProperty("ep.sl.origin");

        Response response = given().contentType("application/x-www-form-urlencoded; charset=UTF-8").formParam("identifier", loginId).formParam("password", password).header("Origin", origin).when().post(fetchProperty("userservice.login.endpointasuser"));

        if (response.getStatusCode() != 200) {

            logger.error("Login failed → Status Code: {}", response.getStatusCode());

        } else {

            logger.info("Login successful for user");
        }

        UserToken = response.jsonPath().getString("result.access_token");

        User_ID = response.jsonPath().getString("result.user.id");
    }

    // Login To Admin
    public static void loginTOAdmin(String loginId, String password) {

        RestAssured.baseURI = baseUrl;

        boolean isSG = DerivingSystem();

        origin = isSG ? fetchProperty("ep.sg.origin") : fetchProperty("ep.sl.origin");

        Response response = given().contentType("application/x-www-form-urlencoded; charset=UTF-8").formParam("identifier", loginId).formParam("password", password).header("Origin", origin).when().post(fetchProperty("userservice.admin.login.endpoint"));

        if (response.getStatusCode() != 200) {

            logger.error("Admin login failed → Status Code: {}", response.getStatusCode());

        } else {

            logger.info("Admin login successful");
        }

        adminToken = response.jsonPath().getString("result.access_token");
    }

    // Delete User API
    public static void deleteUserFromAdmin() {

        if (User_ID == null || adminToken == null) {

            throw new RuntimeException("User_ID or adminToken is null.");
        }

        Response response = given().header("X-auth-token", adminToken).when().delete(fetchProperty("userservice.deleteUserEndPoint") + User_ID);

        if (response.getStatusCode() != 200) {

            logger.error("User deletion failed → Status Code: {}", response.getStatusCode());

        } else {

            logger.info("User deleted successfully");
        }
    }

    public static boolean DerivingSystem() {

        String URL = fetchProperty("userservice.url");

        if (URL == null) {

            throw new RuntimeException("userservice.url is not configured");
        }

        URL = URL.toLowerCase();

        return URL.contains("shikshagraha");
    }

    public static String fetchProperty(String key) {

        return PropertyLoader.PROP_LIST.getProperty(key);
    }

    // Create Entity
    public static Response createEntity(String token, String requestBody) {

        baseUrl = fetchProperty("userservice.qa.api.base.url");

        RestAssured.baseURI = baseUrl;

        return given().header("X-auth-token", token).contentType("application/json")
                .body(requestBody)
                .when()
                .post(fetchProperty("userservice.entity.create.endpoint"));
    }

    // Read Entity
    public static Response readEntity(String token, int entityId) {

        baseUrl = fetchProperty("userservice.qa.api.base.url");

        RestAssured.baseURI = baseUrl;

        RestAssured.config = RestAssured.config().encoderConfig(io.restassured.config.EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));

        return given().header("X-auth-token", token).queryParam("id", entityId).when().post(fetchProperty("userservice.entity.read.endpoint"));
    }

    // Update Entity
    public static Response updateEntity(String token, int entityId, String requestBody) {

        baseUrl = fetchProperty("userservice.qa.api.base.url");

        RestAssured.baseURI = baseUrl;

        RestAssured.config = RestAssured.config().encoderConfig(EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));

        return given().header("X-auth-token", token).contentType("application/json").body(requestBody).when().post(fetchProperty("userservice.entity.update.endpoint") + entityId);
    }

    // Delete Entity
    public static Response deleteEntity(String token, int entityId) {

        baseUrl = fetchProperty("userservice.qa.api.base.url");

        RestAssured.baseURI = baseUrl;

        return given().header("X-auth-token", token).when().delete(fetchProperty("userservice.entity.delete.endpoint") + entityId);
    }
}