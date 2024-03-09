package org.shikshalokam.backend;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URI;

import static io.restassured.RestAssured.given;


public class MentorEDBaseTest extends MentorBase {
    private static final Logger logger = LogManager.getLogger(MentorEDBaseTest.class);
    public static String X_AUTH_TOKEN = null;

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


}
