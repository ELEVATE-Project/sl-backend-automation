package org.shikshalokam.backend.userServiceUtility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.*;


public class CommonUtilityUserService {

    public static String User_ID = null;
    public static String UserToken = null;
    public static String adminToken = null;
    static String baseUrl = fetchProperty("userservice.qa.api.base.url");
    static String origin;

    private static final Logger logger = LogManager.getLogger(CommonUtilityUserService.class);


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

            logger.info("User Deletion Failed!, check API parameters");
        }
    }


    // Call the User API get user id
    public static void loginToUser(String loginId, String password) {

        RestAssured.baseURI = baseUrl;

        boolean isshikshagrahaPresent = CommonUtilityUserService.DerivingSystem();
        if (isshikshagrahaPresent == true) {
            origin = fetchProperty("ep.sg.origin");
        } else {
            origin = fetchProperty("ep.sl.origin");
        }
        Response response = given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("identifier", loginId)
                .formParam("password", password)
                .header("Origin", origin)
                .post(fetchProperty("userservice.login.endpointasuser"));

        if (response.getStatusCode() != 200) {
            logger.error("Login failed → Status Code: {}", response.getStatusCode());
        } else logger.info("Login successful for user");

        //Extract userId from response (example path)
        UserToken = response.jsonPath().getString("result.access_token");
        User_ID = response.jsonPath().getString("result.user.id");
    }

    //Log in to Admin
    public static void loginTOAdmin(String loginId, String password) {

        Response response = given()

                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("identifier", loginId)
                .formParam("password", password)
                .header("Origin", origin)
                .post(fetchProperty("userservice.admiLoginEndPoint"));

        if (response.getStatusCode() != 200) {
            logger.error("Admin login failed → Status Code: {}", response.getStatusCode());
        }else logger.info("Login successful for user");

        logger.info("Exception during Admin login");

        // Extract Admin token from response
        adminToken = response.jsonPath().getString("result.access_token");

    }

    //Log in as Admin use the token for Delete user API
    public static void deleteUserFromAdmin() {

        Response response = given()
                .header("X-auth-token", adminToken)
                .delete(fetchProperty("userservice.deleteUserEndPoint") + User_ID);

        if (response.getStatusCode() != 200) {
            logger.error("Status Code: {}", response.getStatusCode());
        } else logger.info("Exception during user deletion");
    }

    public static boolean DerivingSystem(){
        String URL = fetchProperty("userservice.url");
        String searchString = "shikshagraha";
        return URL.contains(searchString);
    }


    public static String fetchProperty(String key) {
        return PropertyLoader.PROP_LIST.getProperty(key);
    }
}