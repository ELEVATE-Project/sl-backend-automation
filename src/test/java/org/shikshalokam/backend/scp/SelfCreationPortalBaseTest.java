package org.shikshalokam.backend.scp;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.MentorBase;
import org.shikshalokam.backend.PropertyLoader;

import java.net.URI;

import static io.restassured.RestAssured.given;

public class SelfCreationPortalBaseTest extends MentorBase {
    private static final Logger logger = LogManager.getLogger(SelfCreationPortalBaseTest.class);
    public static String X_AUTH_TOKEN = null;
    public static String User_ID = null;
    public static void loginToScp(String loginId, String password) {

        try {

            RestAssured.baseURI = PropertyLoader.PROP_LIST.get("scp.qa.api.base.url").toString();
            Response responce = given().contentType("application/x-www-form-urlencoded; charset=utf-8")
                    .params("email", loginId, "password", password)
                    .post(new URI(PropertyLoader.PROP_LIST.get("scp.login.endpointasuser").toString()));
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
}
