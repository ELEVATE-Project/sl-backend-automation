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

    public static Response loginToUserService(String loginId, String password, Object phoneCode) {
        try {
            RestAssured.baseURI = BASE_URL;
            var request = given()
                    .contentType("application/x-www-form-urlencoded; charset=utf-8")
                    .header("origin", PropertyLoader.PROP_LIST.getProperty("userservice.qa.tenantdomain.origin"))
                    .formParam("identifier", loginId)
                    .formParam("password", password);
            if (phoneCode != null) {
                request.formParam("phoneCode", phoneCode);
            }
            response = request.post(
                    new URI(PropertyLoader.PROP_LIST.get("userservice.login.endpointasuser").toString())
            );
            if (response == null) {
                throw new RuntimeException("No response received from UserService login API");
            }
            int statusCode = response.getStatusCode();
            if (statusCode == 200) {
                logger.info("User login successful");

                X_AUTH_TOKEN = response.jsonPath().getString("result.access_token");

                if (X_AUTH_TOKEN == null || X_AUTH_TOKEN.isEmpty()) {
                    throw new RuntimeException("Login succeeded but access_token is missing");
                }

            } else {
                logger.error("User login failed → Status Code: {}", statusCode);
            }
            return response;
        } catch (Exception e) {
            logger.error("Exception during loginToUserService", e);
            throw new RuntimeException(e);
        }
    }
}