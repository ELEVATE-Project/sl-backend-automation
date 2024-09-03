package org.shikshalokam.backend;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.apache.logging.log4j.core.util.Assert.isEmpty;


public class MentorEDBaseTest extends MentorBase {
    private static final Logger logger = LogManager.getLogger(MentorEDBaseTest.class);
    public static String X_AUTH_TOKEN = null;
    public static String User_ID = null;

    private String searchMenteeEndPoint = "mentoring/v1/users/list?type=mentee&page=1&limit=1&search=";
    private String searchMentorEndPoint = "mentoring/v1/users/list?type=mentor&page=1&limit=1&search=";
    private String deleteUseruserEndPoint = "user/v1/admin/deleteUser/";

    private String deleteUserMentorEndPoint = "/mentoring/v1/admin/userDelete?userId=";
    public static void loginToMentorED(String loginId, String password) {

        try {

            RestAssured.baseURI = PropertyLoader.PROP_LIST.get("mentor.qa.api.base.url").toString();
            Response responce = given().contentType("application/x-www-form-urlencoded; charset=utf-8")
                    .params("email", loginId, "password", password)
                    .post(new URI(PropertyLoader.PROP_LIST.get("mentor.login.endpoint").toString()));
            if (responce.getStatusCode() != 200) {
                logger.info("Login to the application failed ");
            } else {

                X_AUTH_TOKEN = responce.body().jsonPath().get("result.access_token");
                User_ID = responce.body().jsonPath().get("result.user.id").toString();
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }


    //deprecated
    public void deleteMenteeByGivenName(String name) {
        String menteeUserId = null;
        String encodedString = Base64.getEncoder().encodeToString(name.getBytes());
        searchMenteeEndPoint = searchMenteeEndPoint + encodedString;
        Response responce = given().header("X-auth-token", "bearer " + X_AUTH_TOKEN).when().get(searchMenteeEndPoint);
        if (responce.getStatusCode() == 200) {

            if (isEmpty(responce.getBody().jsonPath().get("result.data.values.id"))) {
                logger.info("User not found for the given username :" + name);
                return;
            }
            menteeUserId = responce.getBody().jsonPath().get("result.data.values.id").toString().split("\\[\\[|\\]\\]")[1];
            loginToMentorED("adminmaster@admin.com", "password");
            try {
                responce = given().header("X-auth-token", "bearer " + X_AUTH_TOKEN).when().delete(new URI(deleteUseruserEndPoint + menteeUserId));
                logger.info("Mentee User  deleted from the User service status :" + responce.getStatusCode());
                responce = given().header("X-auth-token", "bearer " + X_AUTH_TOKEN).when().delete(new URI(deleteUserMentorEndPoint + menteeUserId));
                logger.info("Mentee User  deleted from the Mentor service status :" + responce.getStatusCode());
            } catch (URISyntaxException e) {
                logger.info("User deleteion flow URI is not defined properly");
            }
        } else {
            logger.info("User not found for the given username :" + name);
        }
    }

    //deprecated
    public void deleteMentorByGivenName(String name) {
        String mentorUserID = null;
        String encodedString = Base64.getEncoder().encodeToString(name.getBytes());
        searchMentorEndPoint = searchMentorEndPoint + encodedString;
        Response responce = given().header("X-auth-token", "bearer " + X_AUTH_TOKEN).when().get(searchMentorEndPoint);
        if (responce.getStatusCode() == 200) {
            if (isEmpty(responce.getBody().jsonPath().get("result.data.values.id"))) {
                logger.info("User not found for the given username :" + name);
                return;
            }
            mentorUserID = responce.getBody().jsonPath().get("result.data.values.id").toString().split("\\[\\[|\\]\\]")[1];
            loginToMentorED("adminmaster@admin.com", "password");
            try {
                responce = given().header("X-auth-token", "bearer " + X_AUTH_TOKEN).when().delete(new URI(deleteUseruserEndPoint + mentorUserID));
                logger.info("Mentor User  deleted from the User service status :" + responce.getStatusCode());
                responce = given().header("X-auth-token", "bearer " + X_AUTH_TOKEN).when().delete(new URI(deleteUserMentorEndPoint + mentorUserID));
                logger.info("Mentor User  deleted from the Mentor service status :" + responce.getStatusCode());
            } catch (URISyntaxException e) {
                logger.info("User deleteion flow URI is not defined properly");
            }
        } else {
            logger.info("User not found for the given username :" + name);
        }
    }

    public void deleteUser(String userEmail, String userPassword) {
        loginToMentorED(userEmail, userPassword);
        String deleteUserId = User_ID;
        logger.info("user id :"+deleteUserId);

        // Check if deleteUserId is null or empty
        if (deleteUserId == null || deleteUserId.isEmpty()) {
            logger.info("User ID is null or empty. Cannot proceed with deletion.");
            return;
        }
        // Perform the deletion from both services
        loginToMentorED("adminmaster@admin.com", "password");
        try {
            Response response = given().header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                    .when().delete(new URI(deleteUseruserEndPoint + deleteUserId));
            logger.info("User deleted from the User service status: " + response.getStatusCode());

            response = given().header("X-auth-token", "bearer " + X_AUTH_TOKEN)
                    .when().delete(new URI(deleteUserMentorEndPoint + deleteUserId));
            logger.info("User deleted from the Mentor service status: " + response.getStatusCode());
        } catch (URISyntaxException e) {
            logger.info("User deletion flow URI is not defined properly");
        }
    }

    }




