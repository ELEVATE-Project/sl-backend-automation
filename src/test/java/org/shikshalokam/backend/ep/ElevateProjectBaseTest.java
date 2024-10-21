package org.shikshalokam.backend.ep;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.MentorBase;
import org.shikshalokam.backend.PropertyLoader;

import java.net.URI;
import java.net.URISyntaxException;

import static io.restassured.RestAssured.given;

public class ElevateProjectBaseTest extends MentorBase {
    static final Logger logger = LogManager.getLogger(ElevateProjectBaseTest.class);
    public static String X_AUTH_TOKEN = null;
    public static Response response = null;
    public static String User_ID = null;

    // method to login with required parameters
    public static Response loginToElevate(String email, String Password) {
        RestAssured.baseURI = PropertyLoader.PROP_LIST.getProperty("elevate.qa.api.base.url");
        try {
            response = given().contentType("application/x-www-form-urlencoded; charset=utf-8").params("email", email, "password", Password).post(new URI(PropertyLoader.PROP_LIST.getProperty("elevate.login.endpoint")));
            if (response == null) {
                logger.info("NO response received login to the elevate is failed hence terminating");
                System.exit(-1);
            }
            X_AUTH_TOKEN = response.body().jsonPath().get("result.access_token");
        } catch (URISyntaxException e) {
            logger.info(e.getMessage());
        }
        return response;
    }
}

