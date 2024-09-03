package org.shikshalokam.frontend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.MentorEDBaseTest;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;


public class TestMenteeSignUp extends MentorEDBaseTest {
    private static final Logger logger = LogManager.getLogger(TestMenteeSignUp.class);

    @BeforeTest
    public void init() {
        deleteUser(fetchProperty("gmailsignup.userEmail"),
                fetchProperty("gmailsignup.userPassword"));
    }

    @Test
    public void testSignup() {
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).signUp();
        robot.sees(AppAllPages.signupPage).SignupToApp(fetchProperty("gmailsignup.userName"),
                fetchProperty("gmailsignup.userEmail"),
                fetchProperty("gmailsignup.userPassword"));
        robot.sees(AppAllPages.welcomePage).continueToSetupProfile();
        robot.sees(AppAllPages.profileDetailsPage).updateProfile();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("gmailsignup.userEmail"),
                fetchProperty("gmailsignup.userPassword"));
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();

    }

}
