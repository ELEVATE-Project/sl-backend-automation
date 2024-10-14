package org.shikshalokam.backend.ep;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;


/*
Validating the Login Scenarios in this below class
 */
public class TestElevateLogin extends ElevateProjectBaseTest {
    static final Logger logger = LogManager.getLogger(TestElevateLogin.class);
    public static String X_AUTH_TOKEN = null;
    public static String User_ID = null;
    public static Response response = null;

    @Test(description = "Validating the login with correct credentials")
    public void testLoginWithValidCredentials() {
        //Taking the response from the login method using method calling
        response = loginToElevate("manju@guerrillamail.info", "Password@123");
        response.prettyPrint();
        int StatusCode = response.getStatusCode();
        User_ID = response.body().jsonPath().get("result.user.id").toString();
        String message = response.jsonPath().getString("message");
        Assert.assertEquals(StatusCode, 200, "User logged in successfully.");
        Assert.assertEquals(message, "User logged in successfully.");
        //Printing responce
        logger.info("Login Successfull...!!!");
    }

    @Test(description = "Validating the login with incorrect credentials")
    public void testLoginWithInvalidCredentials() {
        response = loginToElevate("manju@guerrillamail.info", "Password###");
        response.prettyPrint();
        int StatusCode = response.getStatusCode();
        Assert.assertEquals(StatusCode, 400, "Incorrect email/password :- Enter correct email/password");
        String message = response.jsonPath().getString("message");
        Assert.assertEquals(message, "Please enter the correct login ID and Password.");
        logger.info("Enter correct email/password");
    }

    @Test(description = "Validating the login with empty login fields")
    public void testLoginWithEmptyFields() {
        response = loginToElevate("manju@guerrillamail.info", "");
        response.prettyPrint();
        int StatusCode = response.getStatusCode();
        Assert.assertEquals(StatusCode, 422, "email/password field is empty");
        String message = response.jsonPath().getString("message");
        Assert.assertEquals(message, "Validation failed, Entered data is incorrect!");
        logger.info("email/password field is empty :- Please fill in the login details");
    }
}

