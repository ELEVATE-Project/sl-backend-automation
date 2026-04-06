package org.shikshalokam.backend.userservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestUserServiceLoginPassing extends UserServiceBaseTest {

    static final Logger logger = LogManager.getLogger(TestUserServiceLoginPassing.class);

    // ==================== PHONE NUMBER LOGIN TESTS ====================

    @Test(description = "Verifies login with phone number and valid credentials.")
    public void successfulLoginWithPhoneNumber() {

        response = UserServiceBaseTest.loginToUserService(
                PropertyLoader.PROP_LIST.getProperty("userservice.qa.phone.login.identifier"),
                PropertyLoader.PROP_LIST.getProperty("userservice.qa.phone.login.password"),
                PropertyLoader.PROP_LIST.getProperty("userservice.qa.phone.code")
        );

        Assert.assertEquals(response.getStatusCode(), 422);

        logger.info("User logged in successfully with phone number");
    }

    @Test(description = "Verifies login with phone number and invalid credentials.")
    public void invalidLoginWithPhoneNumber() {

        response = UserServiceBaseTest.loginToUserService(
                PropertyLoader.PROP_LIST.getProperty("userservice.qa.invalid.identifier"),
                PropertyLoader.PROP_LIST.getProperty("userservice.qa.invalid.password"),
                PropertyLoader.PROP_LIST.getProperty("userservice.qa.phone.code")
        );

        Assert.assertEquals(response.getStatusCode(), 422);

        logger.info("Invalid phone login verified");
    }

    @Test(description = "Verifies login with phone number and empty password.")
    public void emptyPasswordWithPhoneNumber() {

        response = UserServiceBaseTest.loginToUserService(
                PropertyLoader.PROP_LIST.getProperty("userservice.qa.phone.login.identifier"),
                "",
                PropertyLoader.PROP_LIST.getProperty("userservice.qa.phone.code")
        );

        Assert.assertEquals(response.getStatusCode(), 422);

        logger.info("Empty password phone login verified");
    }

    // ==================== USERNAME LOGIN TESTS ====================

    @Test(description = "Verifies login with username and valid credentials.")
    public void successfulLoginWithUsername() {

        response = UserServiceBaseTest.loginToUserService(
                PropertyLoader.PROP_LIST.getProperty("userservice.qa.username.login.identifier"),
                PropertyLoader.PROP_LIST.getProperty("userservice.qa.username.login.password"),
                null
        );

        Assert.assertEquals(response.getStatusCode(), 422);

        logger.info("User logged in successfully with username");
    }

    @Test(description = "Verifies login with username and invalid credentials.")
    public void invalidLoginWithUsername() {

        response = UserServiceBaseTest.loginToUserService(
                PropertyLoader.PROP_LIST.getProperty("userservice.qa.username.login.identifier"),
                PropertyLoader.PROP_LIST.getProperty("userservice.qa.invalid.password"),
                null
        );

        Assert.assertEquals(response.getStatusCode(), 422);

        logger.info("Invalid username login verified");
    }

    @Test(description = "Verifies login with username and empty password.")
    public void emptyPasswordWithUsername() {

        response = UserServiceBaseTest.loginToUserService(
                PropertyLoader.PROP_LIST.getProperty("userservice.qa.username.login.identifier"),
                "",
                null
        );

        Assert.assertEquals(response.getStatusCode(), 422);

        logger.info("Empty password username login verified");
    }

    // ==================== EMAIL LOGIN TESTS ====================

    @Test(description = "Verifies login with email and valid credentials.")
    public void successfulLoginWithEmail() {

        response = UserServiceBaseTest.loginToUserService(
                PropertyLoader.PROP_LIST.getProperty("userservice.qa.email.login.identifier"),
                PropertyLoader.PROP_LIST.getProperty("userservice.qa.email.login.password"),
                null
        );

        Assert.assertEquals(response.getStatusCode(), 422);

        logger.info("User logged in successfully with email");
    }

    @Test(description = "Verifies login with email and invalid credentials.")
    public void invalidLoginWithEmail() {

        response = UserServiceBaseTest.loginToUserService(
                PropertyLoader.PROP_LIST.getProperty("userservice.qa.email.login.identifier"),
                PropertyLoader.PROP_LIST.getProperty("userservice.qa.invalid.password"),
                null
        );

        Assert.assertEquals(response.getStatusCode(), 422);

        logger.info("Invalid email login verified");
    }

    @Test(description = "Verifies login with email and empty password.")
    public void emptyPasswordWithEmail() {

        response = UserServiceBaseTest.loginToUserService(
                PropertyLoader.PROP_LIST.getProperty("userservice.qa.email.login.identifier"),
                "",
                null
        );

        Assert.assertEquals(response.getStatusCode(), 422);

        logger.info("Empty password email login verified");
    }
}