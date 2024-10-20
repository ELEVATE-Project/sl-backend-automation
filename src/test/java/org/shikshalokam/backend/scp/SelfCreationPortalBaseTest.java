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
    public static Response response = null;

    public static Response loginToScp(String loginId, String password) {
        try {
            RestAssured.baseURI = PropertyLoader.PROP_LIST.get("scp.qa.api.base.url").toString();
            response = given().contentType("application/x-www-form-urlencoded; charset=utf-8").formParam("email", loginId).formParam("password", password).post(new URI(PropertyLoader.PROP_LIST.get("scp.login.endpointasuser").toString()));
            if (response == null) {
                logger.info("No response received login to the scp is failed");
                System.exit(-1);
            }
            X_AUTH_TOKEN = response.body().jsonPath().get("result.access_token");
            logger.info(response);
            logger.info(response.prettyPrint());
            return response;
        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }
}
