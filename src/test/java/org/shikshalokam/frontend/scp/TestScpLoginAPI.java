package org.shikshalokam.frontend.scp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.restassured.response.Response;
import org.shikshalokam.backend.PropertyLoader;
import org.shikshalokam.backend.scp.SelfCreationPortalBaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestScpLoginAPI extends SelfCreationPortalBaseTest {
    static final Logger logger = LogManager.getLogger(TestScpLoginAPI.class);
    public static String X_AUTH_TOKEN = null;
    public static Response response = null;

    //Login for SCP
    @Test(description = "Verifies the functionality of logging in as an user.")
    public void successfulLoginScp() {
        response = SelfCreationPortalBaseTest.loginToScp(PropertyLoader.PROP_LIST.getProperty("sl.scp.userascontentcreator"), PropertyLoader.PROP_LIST.getProperty("sl.scp.passwordforcontentcreator"));
        //Status code 200
        Assert.assertEquals(response.getStatusCode(), 200, "User logged in successfully.");

        //To check whether statusCode message coming correctly or not
        Assert.assertEquals(response.jsonPath().getString("message"), "User logged in successfully.");
        //Printing response
        logger.info("User logged in with valid credentials");
    }

    //Login with invalid credentials
    @Test(description = "Verifies the functionality of user logged with invalid credentials.")
    public void invalidLoginScp() {
        response = SelfCreationPortalBaseTest.loginToScp(PropertyLoader.PROP_LIST.getProperty("sl.scp.invalidCreds"), PropertyLoader.PROP_LIST.getProperty("sl.scp.invalidCredsPassword"));
        //Status code 400
        Assert.assertEquals(response.getStatusCode(), 400, "Please enter the correct login ID and Password.");

        //To check whether statusCode message coming correctly or not
        Assert.assertEquals(response.jsonPath().getString("message"), "Please enter the correct login ID and Password.");
        //Printing response
        logger.info("Please enter the correct login ID and Password.");
    }

    //Login with empty login fields
    @Test(description = "Verifies the functionality of user entered password but not email")
    public void checkEmptyFieldsForLoginCredentials() {
        response = SelfCreationPortalBaseTest.loginToScp(PropertyLoader.PROP_LIST.getProperty("sl.scp.invalidCreds"), PropertyLoader.PROP_LIST.getProperty(""));
        //Status code 422
        Assert.assertEquals(response.getStatusCode(), 422, "Entered Email/Password is empty.");

        //To check whether statusCode message coming correctly or not
        Assert.assertEquals(response.jsonPath().getString("message"), "Validation failed, Entered data is incorrect!");
        //Printing response
        logger.info("Entered Email/Password is empty.");
    }
}
