package org.shikshalokam.backend.ep;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.MentorBase;
import org.shikshalokam.backend.PropertyLoader;
import java.net.URI;
import static io.restassured.RestAssured.given;
import static org.testng.AssertJUnit.fail;

public class ElevateProjectBaseTest extends MentorBase {
    static final Logger logger = LogManager.getLogger(ElevateProjectBaseTest.class);
    public static String X_AUTH_TOKEN = null;
    public static String User_ID = null;


    // method to login with required parameters
    public static void logintoElevate(String email, String Password) {

        try {
            RestAssured.baseURI = PropertyLoader.PROP_LIST.getProperty("Elevate.qa.api.base.url");
            Response response = given().contentType("application/x-www-form-urlencoded; charset=utf-8").params("email", email, "password", Password).post(new URI(PropertyLoader.PROP_LIST.getProperty("Elevate.login.endpoint")));
            int statusCode = response.getStatusCode();
            logger.info("HTTP Status code :- " + statusCode);
            String responseBody = response.getBody().prettyPrint();
            if (statusCode == 200) {
                String Pcondition = response.jsonPath().getString("message");
                boolean ConditionP = Pcondition.equals("User logged in successfully.");
                logger.info(ConditionP);
                logger.info(Pcondition);
                X_AUTH_TOKEN = response.body().jsonPath().get("result.access_token");
                User_ID = response.body().jsonPath().get("result.user.id").toString();
            } else {
                String message = response.jsonPath().getString("message");
                boolean ConditionA = message.equals("email is invalid"); // Incorrect E-mail use numbers or use incorrect e-mail format
                boolean ConditionB = message.equals("Please enter the correct login ID and Password."); // wrong Password or wrong e-mail
                boolean ConditionC = message.equals("Validation failed, Entered data is incorrect!"); // Password or E-mail field is empty
                if (ConditionA) {
                    logger.info("email is invalid, Please check the email field");
                    fail();
                } else if (ConditionB) {
                    logger.info("Please enter the correct login ID and Password., Please enter the correct E-mail ID and Password");
                    fail();
                } else if (ConditionC) {
                    logger.info("E-mail/password field is empty, ");
                    fail();
                } else {
                    logger.info("Unexpected HTTP Status code: " + statusCode);
                    fail();
                }
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
            e.printStackTrace();
        }
    }
}