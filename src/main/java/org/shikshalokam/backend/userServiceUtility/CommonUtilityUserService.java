package org.shikshalokam.backend.userServiceUtility;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;

import static io.restassured.RestAssured.given;

public class CommonUtilityUserService {

    public static String User_ID = null;
    public static String adminToken = null;
    public static String tenantAdminToken = null;
    public static String userToken = null;

    private static final Logger logger = LogManager.getLogger(CommonUtilityUserService.class);
    static String baseUrl = fetchProperty("userservice.qa.api.base.url");

    //Generic login method for any user type.
    private static String loginToGetToken(String loginId,
                                          String password,
                                          String endpoint,
                                          String userType) {
        RestAssured.baseURI = baseUrl;
        // Determine origin based on system
        String origin = DerivingSystem()
                ? fetchProperty("ep.sg.origin")
                : fetchProperty("ep.sl.origin");

        Response response = given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("identifier", loginId)
                .formParam("password", password)
                .header("Origin", origin)
                .when()
                .post(endpoint);

        logger.info("{} login response status: {}", userType, response.getStatusCode());

        if (response.getStatusCode() != 200) {
            logger.error("Login Failed for user: {}. Response: {}", loginId, response.asString());
            throw new RuntimeException(
                    "\nLogin Failed" +
                            "\nUser : " + loginId +
                            "\nStatus Code : " + response.getStatusCode() +
                            "\nResponse : " + response.asString()
            );
        }

        String token = response.jsonPath().getString("result.access_token");
        if (token == null || token.isEmpty()) {
            throw new RuntimeException(userType + " token generation failed for user: " + loginId);
        }

        logger.info("{} login successful for user: {}", userType, loginId);
        return token;
    }

    //Get Admin token after login
    public static String loginAsAdmin() {
        adminToken = loginToGetToken(
                fetchProperty("userservice.qa.admin.login.user"),
                fetchProperty("userservice.qa.admin.login.password"),
                fetchProperty("userservice.admin.login.endpoint"),
                "Admin"
        );
        return adminToken;
    }

    //Get Tenant Admin token after login
    public static String loginAsTenantAdmin() {
        tenantAdminToken = loginToGetToken(
                fetchProperty("userservice.qa.tenantAdmin.username"),
                fetchProperty("userservice.qa.tenantAdmin.password"),
                fetchProperty("userservice.login.endpointasuser"),
                "Tenant Admin"
        );
        return tenantAdminToken;
    }

    //Get Normal User token after login
    public static String loginToUser() {
        userToken = loginToGetToken(
                fetchProperty("userservice.qa.phone.login.identifier"),
                fetchProperty("userservice.qa.phone.login.password"),
                fetchProperty("userservice.login.endpointasuser"),
                "User"
        );
        return userToken;
    }

    //Delete user using admin
    public static void deleteUserFromAdmin() {
        if (User_ID == null || adminToken == null) {
            throw new RuntimeException("User_ID or adminToken is null.");
        }

        Response response = given()
                .header("X-auth-token", adminToken)
                .when()
                .delete(fetchProperty("userservice.deleteUserEndPoint") + User_ID);

        if (response.getStatusCode() != 200) {
            logger.error("User deletion failed → Status Code: {}", response.getStatusCode());
        } else {
            logger.info("User deleted successfully: {}", User_ID);
        }
    }

    //Determine which system (Shikshagraha or SL) based on URL
    public static boolean DerivingSystem() {
        String URL = fetchProperty("userservice.url");
        if (URL == null) {
            throw new RuntimeException("userservice.url is not configured");
        }
        return URL.toLowerCase().contains("shikshagraha");
    }

    public static String fetchProperty(String key) {
        return PropertyLoader.PROP_LIST.getProperty(key);
    }
}