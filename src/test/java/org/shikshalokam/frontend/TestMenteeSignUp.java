package org.shikshalokam.frontend;

import org.shikshalokam.backend.MentorEDBaseTest;
import org.shikshalokam.backend.PropertyLoader;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;


public class TestMenteeSignUp extends MentorEDBaseTest {

    @BeforeTest
    public void init() {
        loginToMentorED("jubedhashaik029@gmail.com", "PAssword@@123$");
        deleteMentorByGivenName("UserSignup");
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
