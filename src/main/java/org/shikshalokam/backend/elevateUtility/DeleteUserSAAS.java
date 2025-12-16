package org.shikshalokam.backend.elevateUtility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;


public class DeleteUserSAAS {

    public static String User_ID = null;
    public static String UserToken = null;
    public static String adminToken = null;
    static String baseUrl = fetchProperty("ep.baseURL");
    static String origin;

    private static final Logger logger = LogManager.getLogger(DeleteUserSAAS.class);


    // Call the User API and delete the user
    public static void deleteUser(String userName, String userPassword, String superAdminUN, String superAdminPass) {

        try {
            //Call User Login Admin API Method
            loginToUser(userName, userPassword);

            //Call Login Admin API Method
            loginTOAdmin(superAdminUN, superAdminPass);

            //Call Delete user API
            deleteUserFromAdmin();

        } catch (Exception e) {

            System.out.println("User Deletion Failed!, check API parameters");
        }
    }


    // Call the User API get user id
    public static void loginToUser(String loginId, String password) {

        RestAssured.baseURI = baseUrl;
        String URL = fetchProperty("ep.url");
        String searchString = "shikshagraha";
        boolean isPresent = URL.contains(searchString);

        if (isPresent == true) {
            origin = fetchProperty("ep.sg.origin");
        } else {
            origin = fetchProperty("ep.sl.origin");
        }
        Response response = given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("identifier", loginId)
                .formParam("password", password)
                .header("Origin", origin)
                .post(fetchProperty("ep.userEndPoint"));
        try {
            if (response.getStatusCode() != 200) {
                logger.error("Login failed → Status Code: {}", response.getStatusCode());
                logger.error("API Response: {}", response.getBody().asString());
                throw new RuntimeException("Login failed");
            }
            logger.info("Login successful for user: {}", loginId);

            //Extract userId from response (example path)
            UserToken = response.jsonPath().getString("result.access_token");
            User_ID = response.jsonPath().getString("result.user.id");

        } catch (Exception e) {
            logger.error("Exception during login: {}", e.getMessage(), e);
        }
    }


    //Log in to Admin
    public static void loginTOAdmin(String loginId, String password) {

        Response response = given()

                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("identifier", loginId)
                .formParam("password", password)
                .header("Origin", origin)
                .post(fetchProperty("ep.admiLoginEndPoint"));

        try {
            if (response.getStatusCode() != 200) {
                logger.error("Admin login failed → Status Code: {}", response.getStatusCode());
                logger.error("API Response: {}", response.getBody().asString());
                throw new RuntimeException("Admin login failed");
            }
        } catch (Exception e) {
            logger.error("Exception during Admin login: {}", e.getMessage(), e);
        }

        // Extract Admin token from response
        adminToken = response.jsonPath().getString("result.access_token");

    }

    //Log in as Admin use the token for Delete user API
    public static void deleteUserFromAdmin() {
        //https://qa.elevate-apis.shikshalokam.org/user/v1/admin/deleteUser/3090

        Response response = given()
                .header("X-auth-token", adminToken)
                .delete(fetchProperty("ep.deleteUserEndPoint") + User_ID);

        try {
            if (response.getStatusCode() != 200) {
                logger.error("Status Code: {}", response.getStatusCode());
                throw new RuntimeException("Delete User failed → " + response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Exception during user deletion: {}", e.getMessage(), e);
        }

    }

    public static String fetchProperty(String key) {
        return PropertyLoader.PROP_LIST.getProperty(key);
    }

}