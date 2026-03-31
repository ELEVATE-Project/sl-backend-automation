package org.shikshalokam.backend.userservice;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.MentorBase;
import org.shikshalokam.backend.PropertyLoader;

import java.net.URI;

import static io.restassured.RestAssured.given;

public class UserServiceBaseTest extends MentorBase {
    private static final Logger logger = LogManager.getLogger(UserServiceBaseTest.class);
    public static String X_AUTH_TOKEN = null;
    public static Response response = null;
    public static String BASE_URL = PropertyLoader.PROP_LIST.getProperty("userservice.qa.api.base.url");

    public static Response loginToUserService(String loginId, String password, Object o) {
        try {
            RestAssured.baseURI = BASE_URL;
            response = given().contentType("application/x-www-form-urlencoded; charset=utf-8").header("origin", PropertyLoader.PROP_LIST.getProperty("userservice.qa.tenantdomain.origin")).formParam("email", loginId).formParam("password", password).post(new URI(PropertyLoader.PROP_LIST.get("userservice.login.endpointasuser").toString()));
            if (response == null) {
                logger.info("No response received login to the userservice is failed");
                System.exit(-1);
            }
            X_AUTH_TOKEN = response.body().jsonPath().get("result.access_token");
            logger.info(response.prettyPrint());
            return response;
        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }
}