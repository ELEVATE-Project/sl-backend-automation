package org.shikshalokam.backend.userservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.restassured.response.Response;
import org.shikshalokam.backend.PropertyLoader;
import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import java.net.URI;

import static io.restassured.RestAssured.given;

public class TestUserServiceLogin extends UserServiceBaseTest {
    static final Logger logger = LogManager.getLogger(TestUserServiceLogin.class);
    public static String X_AUTH_TOKEN = null;
    public static Response response = null;

    // ==================== PHONE NUMBER LOGIN TESTS ====================

    //Login with Phone Number - Valid Credentials
    @Test(priority = 1, description = "Verifies the functionality of logging in with phone number and valid credentials.")
    public void successfulLoginWithPhoneNumber() {
        String identifier = PropertyLoader.PROP_LIST.getProperty("userservice.qa.phone.login.identifier");
        String password = PropertyLoader.PROP_LIST.getProperty("userservice.qa.phone.login.password");
        String phoneCode = PropertyLoader.PROP_LIST.getProperty("userservice.qa.phone.code");

        response = this.loginToUserServiceWithPhoneAndOrigin(identifier, password, phoneCode);
        //Status code 200
        Assert.assertEquals(response.getStatusCode(), 200, "User logged in successfully with phone number.");

        //To check whether statusCode message coming correctly or not
        Assert.assertEquals(response.jsonPath().getString("message"), "User logged in successfully.");
        //Printing response
        logger.info("User logged in successfully with phone number and valid credentials");
    }

    //Login with Phone Number - Invalid Credentials
    @Test(priority = 2, description = "Verifies the functionality of logging in with phone number and invalid credentials.")
    public void invalidLoginWithPhoneNumber() {
        String identifier = PropertyLoader.PROP_LIST.getProperty("userservice.qa.invalid.identifier");
        String password = PropertyLoader.PROP_LIST.getProperty("userservice.qa.invalid.password");
        String phoneCode = PropertyLoader.PROP_LIST.getProperty("userservice.qa.phone.code");

        response = this.loginToUserServiceWithPhoneAndOrigin(identifier, password, phoneCode);
        //Status code 400
        Assert.assertEquals(response.getStatusCode(), 400, "Invalid identifier or password.");

        //To check whether statusCode message coming correctly or not
        Assert.assertEquals(response.jsonPath().getString("message"), "Invalid identifier or password. Please check your login credentials.");
        logger.info("Phone number login failed with invalid credentials as expected");
    }

    //Login with Phone Number - Empty Password
    @Test(priority = 3, description = "Verifies the functionality of logging in with phone number and empty password.")
    public void checkEmptyPasswordWithPhoneNumber() {
        String identifier = PropertyLoader.PROP_LIST.getProperty("userservice.qa.phone.login.identifier");
        String phoneCode = PropertyLoader.PROP_LIST.getProperty("userservice.qa.phone.code");

        response = this.loginToUserServiceWithPhoneAndOrigin(identifier, "", phoneCode);
        //Status code 400
        Assert.assertEquals(response.getStatusCode(), 400, "Invalid identifier or password.");

        logger.info("Phone number login failed with empty password as expected");
    }

    // ==================== USERNAME LOGIN TESTS ====================

    //Login with Username - Valid Credentials
    @Test(priority = 4, description = "Verifies the functionality of logging in with username and valid credentials.")
    public void successfulLoginWithUsername() {
        String identifier = PropertyLoader.PROP_LIST.getProperty("userservice.qa.username.login.identifier");
        String password = PropertyLoader.PROP_LIST.getProperty("userservice.qa.username.login.password");

        response = this.loginToUserServiceWithOrigin(identifier, password);
        //Status code 200
        Assert.assertEquals(response.getStatusCode(), 200, "User logged in successfully with username.");

        //To check whether statusCode message coming correctly or not
        Assert.assertEquals(response.jsonPath().getString("message"), "User logged in successfully.");
        logger.info("User logged in successfully with username and valid credentials");
    }

    //Login with Username - Invalid Credentials
    @Test(priority = 5, description = "Verifies the functionality of logging in with username and invalid credentials.")
    public void invalidLoginWithUsername() {
        String identifier = PropertyLoader.PROP_LIST.getProperty("userservice.qa.username.login.identifier");
        String password = PropertyLoader.PROP_LIST.getProperty("userservice.qa.invalid.password");

        response = this.loginToUserServiceWithOrigin(identifier, password);
        //Status code 400
        Assert.assertEquals(response.getStatusCode(), 400, "Invalid identifier or password.");

        logger.info("Username login failed with invalid credentials as expected");
    }

    //Login with Username - Empty Password
    @Test(priority = 6, description = "Verifies the functionality of logging in with username and empty password.")
    public void checkEmptyPasswordWithUsername() {
        String identifier = PropertyLoader.PROP_LIST.getProperty("userservice.qa.username.login.identifier");

        response = this.loginToUserServiceWithOrigin(identifier, "");
        //Status code 400
        Assert.assertEquals(response.getStatusCode(), 400, "Invalid identifier or password.");

        logger.info("Username login failed with empty password as expected");
    }

    // ==================== EMAIL LOGIN TESTS ====================

    //Login with Email - Valid Credentials
    @Test(priority = 7, description = "Verifies the functionality of logging in with email and valid credentials.")
    public void successfulLoginWithEmail() {
        String identifier = PropertyLoader.PROP_LIST.getProperty("userservice.qa.email.login.identifier");
        String password = PropertyLoader.PROP_LIST.getProperty("userservice.qa.email.login.password");

        response = this.loginToUserServiceWithOrigin(identifier, password);
        //Status code 200
        Assert.assertEquals(response.getStatusCode(), 200, "User logged in successfully with email.");

        //To check whether statusCode message coming correctly or not
        Assert.assertEquals(response.jsonPath().getString("message"), "User logged in successfully.");
        logger.info("User logged in successfully with email and valid credentials");
    }

    //Login with Email - Invalid Credentials
    @Test(priority = 8, description = "Verifies the functionality of logging in with email and invalid credentials.")
    public void invalidLoginWithEmail() {
        String identifier = PropertyLoader.PROP_LIST.getProperty("userservice.qa.email.login.identifier");
        String password = PropertyLoader.PROP_LIST.getProperty("userservice.qa.invalid.password");

        response = this.loginToUserServiceWithOrigin(identifier, password);
        //Status code 400
        Assert.assertEquals(response.getStatusCode(), 400, "Invalid identifier or password.");

        logger.info("Email login failed with invalid credentials as expected");
    }

    //Login with Email - Empty Password
    @Test(priority = 9, description = "Verifies the functionality of logging in with email and empty password.")
    public void checkEmptyPasswordWithEmail() {
        String identifier = PropertyLoader.PROP_LIST.getProperty("userservice.qa.email.login.identifier");

        response = this.loginToUserServiceWithOrigin(identifier, "");
        //Status code 400
        Assert.assertEquals(response.getStatusCode(), 400, "Invalid identifier or password.");

        logger.info("Email login failed with empty password as expected");
    }

    // ==================== LOGIN HELPER METHODS ====================

    /**
     * Login to UserService with identifier and password (without phone_code)
     */
    private Response loginToUserServiceWithOrigin(String identifier, String password) {
        try {
            RestAssured.baseURI = UserServiceBaseTest.BASE_URL;
            String jsonBody = "{\"identifier\": \"" + identifier + "\", \"password\": \"" + password + "\"}";
            response = given().contentType("application/json")
                    .header("origin", PropertyLoader.PROP_LIST.getProperty("userservice.qa.tenantdomain.origin"))
                    .body(jsonBody)
                    .post(new URI(PropertyLoader.PROP_LIST.get("userservice.login.endpointasuser").toString()));
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

    /**
     * Login to UserService with phone number, password, and phone_code
     */
    private Response loginToUserServiceWithPhoneAndOrigin(String identifier, String password, String phoneCode) {
        try {
            RestAssured.baseURI = UserServiceBaseTest.BASE_URL;
            String jsonBody = "{\"identifier\": \"" + identifier + "\", \"password\": \"" + password + "\", \"phone_code\": \"" + phoneCode + "\"}";
            response = given().contentType("application/json")
                    .header("origin", PropertyLoader.PROP_LIST.getProperty("userservice.qa.tenantdomain.origin"))
                    .body(jsonBody)
                    .post(new URI(PropertyLoader.PROP_LIST.get("userservice.login.endpointasuser").toString()));
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