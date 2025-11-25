package org.shikshalokam.backend.elevateUtility;

import org.shikshalokam.backend.PropertyLoader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;


public class DeleteUserSAAS {

    public static String User_ID = null;
    public static String adminToken= null;
    private static String baseUrl = "https://qa.elevate-apis.shikshalokam.org/";


    // Call the User API and delete the user
    public static void deleteUser(String userName, String userPassword, String superAdminUN, String superAdminPass) {

        try {
            //Call User Login Admin API Method
            loginToUser(userName,userPassword);

            //Call Login Admin API Method
            loginTOAdmin(superAdminUN,superAdminPass);

            //Call Delete user API
            deleteUserFromAdmin();

        } catch (Exception e)
        {
            System.out.println("User Deletion Failed!, check API parameters");
        }
    }


    // Call the User API get user id
    public static void loginToUser (String loginId, String password)
    {
        RestAssured.baseURI = baseUrl;

        Response response = given()

                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("identifier", loginId)
                .formParam("password", password)
                .header("Origin", "qa.elevate-shikshagraha.shikshalokam.org")
                .post("user/v1/account/login");

        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Login failed → " + response.getStatusCode());
        }

        //Extract userId from response (example path)
        User_ID = response.jsonPath().getString("result.user.id");
    }


    //Log in to Admin
    public static void loginTOAdmin (String loginId, String password)
    {
        //https://qa.elevate-apis.shikshalokam.org/user/v1/admin/login

        Response response = given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("identifier", loginId)
                .formParam("password", password)
                .header("Origin", "qa.elevate-shikshagraha.shikshalokam.org")
                .post("user/v1/admin/login");

        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Admin login failed → " + response.getStatusCode());
        }

        // Extract Admin token from response
        adminToken = response.jsonPath().getString("result.access_token");

    }

    //Log in as Admin use the token for Delete user API
    public static void deleteUserFromAdmin()
    {
        //https://qa.elevate-apis.shikshalokam.org/user/v1/admin/deleteUser/3090

        Response response = given()
                .header("X-auth-token", adminToken)
                .delete("user/v1/admin/deleteUser/"+User_ID);

        if (response.getStatusCode() != 200) {
            System.out.println(response.getStatusCode());
            throw new RuntimeException("Delete User failed → " + response.getStatusCode());

        }
        else
            System.out.println("User Deletion Completed!");

    }

    public static String fetchProperty (String key)
    {
        return PropertyLoader.PROP_LIST.getProperty(key);
    }

}