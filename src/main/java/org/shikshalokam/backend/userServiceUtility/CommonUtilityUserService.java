package org.shikshalokam.backend.userServiceUtility;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;

import java.io.File;

import static io.restassured.RestAssured.given;

public class CommonUtilityUserService {

    public static String User_ID = null;
    public static String UserToken = null;
    public static String adminToken = null;

    static String baseUrl = null;

    static String origin;

    private static final Logger logger = LogManager.getLogger(CommonUtilityUserService.class);

    // Generate Normal User Token
    public static String generateNormalUserToken() {
        baseUrl = fetchProperty("userservice.qa.api.base.url");
        RestAssured.baseURI = baseUrl;

        boolean isSG = DerivingSystem();

        origin = isSG ? fetchProperty("ep.sg.origin") : fetchProperty("ep.sl.origin");

        Response response = given().contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("identifier", fetchProperty("userservice.qa.phone.login.identifier"))
                .formParam("password", fetchProperty("userservice.qa.phone.login.password"))
                .header("Origin", origin)
                .when()
                .post(fetchProperty("userservice.login.endpointasuser"));

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

        baseUrl = fetchProperty("userservice.qa.api.base.url");
        RestAssured.baseURI = baseUrl;

        boolean isSG = DerivingSystem();

        origin = isSG ? fetchProperty("ep.sg.origin") : fetchProperty("ep.sl.origin");

        Response response = given().contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("identifier", fetchProperty("userservice.qa.admin.login.user"))
                .formParam("password", fetchProperty("userservice.qa.admin.login.password"))
                .header("Origin", origin)
                .when()
                .post(fetchProperty("userservice.admin.login.endpoint"));

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
        baseUrl = fetchProperty("userservice.qa.api.base.url");
        RestAssured.baseURI = baseUrl;

        boolean isSG = DerivingSystem();

        origin = isSG ? fetchProperty("ep.sg.origin") : fetchProperty("ep.sl.origin");

        Response response = given().contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("identifier", loginId)
                .formParam("password", password)
                .header("Origin", origin)
                .when()
                .post(fetchProperty("userservice.login.endpointasuser"));

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
        baseUrl = fetchProperty("userservice.qa.api.base.url");
        RestAssured.baseURI = baseUrl;

        boolean isSG = DerivingSystem();

        origin = isSG ? fetchProperty("ep.sg.origin") : fetchProperty("ep.sl.origin");

        Response response = given().contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("identifier", loginId)
                .formParam("password", password)
                .header("Origin", origin)
                .when()
                .post(fetchProperty("userservice.admin.login.endpoint"));

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

        Response response = given().header("X-auth-token", adminToken)
                .when()
                .delete(fetchProperty("userservice.deleteUserEndPoint") + User_ID);

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

    // Generate Signed URL
    public static Response getSignedUrlForBulkUpload(String adminToken) {
        baseUrl = fetchProperty("userservice.qa.api.base.url");
        RestAssured.baseURI = baseUrl;

        return given().header("X-auth-token", adminToken)
                .queryParam("fileName", fetchProperty("userservice.bulkupload.csv.filename"))
                .contentType("text/plain")
                .body("").when().get(fetchProperty("userservice.bulkupload.getsignedurl.endpoint"));
    }

    // Upload CSV File
    public static Response uploadBulkCsvFile(String signedUrl) {

        File csvFile = new File(fetchProperty("userservice.bulkupload.csv.path"));

        return given().header("Content-Type", "multipart/form-data").body(csvFile).when().put(signedUrl);
    }

    // Login Created Bulk User
    public static Response loginCreatedBulkUser(String identifier, String password) {

        baseUrl = fetchProperty("userservice.qa.api.base.url");

        RestAssured.baseURI = baseUrl;

        boolean isSG = DerivingSystem();

        origin = isSG ? fetchProperty("ep.sg.origin") : fetchProperty("ep.sl.origin");

        return given().contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("identifier", identifier)
                .formParam("password", password)
                .header("Origin", origin)
                .when()
                .post(fetchProperty("userservice.login.endpointasuser"));
    }
    // Create Entity Type
    public static Response createEntityType(String token, String requestBody) {
        baseUrl = fetchProperty("userservice.qa.api.base.url");
        RestAssured.baseURI = baseUrl;

        return given().header("X-auth-token", token).contentType("application/json").body(requestBody).when().post(fetchProperty("userservice.entitytype.create.endpoint"));
    }

    // Read Entity Type
    public static Response readEntityType(String token, String requestBody) {
        baseUrl = fetchProperty("userservice.qa.api.base.url");
        RestAssured.baseURI = baseUrl;

        return given().header("X-auth-token", token).contentType("application/json").body(requestBody).when().post(fetchProperty("userservice.entitytype.read.endpoint"));
    }

    // Read All Entity Types
    public static Response readAllEntityTypes(String token) {
        baseUrl = fetchProperty("userservice.qa.api.base.url");
        RestAssured.baseURI = baseUrl;

        return given().header("X-auth-token", token).contentType("application/json").when().post(fetchProperty("userservice.entitytype.read.endpoint"));
    }

    // Delete Entity Type
    public static Response deleteEntityType(String token, int entityTypeId) {
        baseUrl = fetchProperty("userservice.qa.api.base.url");
        RestAssured.baseURI = baseUrl;

        Response response = given().header("X-auth-token", token).when().delete(fetchProperty("userservice.entitytype.delete.endpoint") + entityTypeId);

        response.prettyPrint();

        return response;
    }
}