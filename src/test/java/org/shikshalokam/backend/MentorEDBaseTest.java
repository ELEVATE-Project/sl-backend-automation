package org.shikshalokam.backend;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;

import static io.restassured.RestAssured.given;


public class MentorEDBaseTest extends MentorBase {
    private static final Logger logger = LogManager.getLogger(MentorEDBaseTest.class);
    public static String X_AUTH_TOKEN = null;

    private String searchMenteeEndPoint = "mentoring/v1/users/list?type=mentee&page=1&limit=2&search=";
    private String deleteUseruserEndPoint="user/v1/admin/deleteUser/";

    private String deleteUserMentorEndPoint ="/mentoring/v1/admin/userDelete?userId=";
    public void loginToMentorED(String loginId, String password) {

        try {

            RestAssured.baseURI = PropertyLoader.PROP_LIST.get("mentor.qa.api.base.url").toString();
            Response responce = given().contentType("application/x-www-form-urlencoded; charset=utf-8")
                    .params("email", loginId, "password", password)
                    .post(new URI(PropertyLoader.PROP_LIST.get("mentor.login.endpoint").toString()));
            if (responce.getStatusCode() != 200) {
                logger.info("Login to the application failed ");
            } else {

                X_AUTH_TOKEN = responce.body().jsonPath().get("result.access_token");
            }

        }
        catch (Exception e)
        {

            logger.info(e.getMessage());
            e.printStackTrace();
        }

    }

    public void deleteMenteeByGivenName( String name)
    {
        String menteeUserId=null;
        String encodedString = Base64.getEncoder().encodeToString(name.getBytes());
        searchMenteeEndPoint =  new StringBuilder(searchMenteeEndPoint).append(encodedString).toString();
        Response responce = given().header("X-auth-token", "bearer " + X_AUTH_TOKEN).when().get(searchMenteeEndPoint);
        if (responce.getStatusCode()==200)
        {

            menteeUserId=responce.getBody().jsonPath().get("result.data.values.id" ).toString().split("\\[\\[|\\]\\]")[1];
            loginToMentorED("adminmaster@admin.com","password");
            try {
                responce = given().header("X-auth-token", "bearer " + X_AUTH_TOKEN).when().delete(new URI(deleteUseruserEndPoint + menteeUserId));
                logger.info("Mentee User  deleted from the User service status :" + responce.getStatusCode());
                responce = given().header("X-auth-token ", "bearer " + X_AUTH_TOKEN).when().delete(new URI(deleteUserMentorEndPoint + menteeUserId));
                logger.info("Mentee User  deleted from the Mentor service status :" + responce.getStatusCode());
            }
            catch (URISyntaxException e)
            {
                logger.info ("User deleteion flow URI is not defined properly");
            }
        }else {
            logger.info("User not found for the given username :" +name);
        }

    }


}
