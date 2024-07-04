package org.shikshalokam.frontend;

import org.shikshalokam.backend.MentorEDBaseTest;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestChangeAndForgotPassword extends MentorEDBaseTest {

    @Test(dependsOnMethods = {"org.shikshalokam.frontend.TestMenteeSignUp.AppSignupTest"})
    public void changePassword() {
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("gmailsignup.userEmail"), fetchProperty("gmailsignup.userPassword"));
        robot.sees(AppAllPages.welcomePage).requestToChangePassword();
        robot.sees(AppAllPages.changePasswordPage).changePassword(fetchProperty("gmailsignup.userPassword"), "PassworD@@@1234");
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("gmailsignup.userEmail"), "PassworD@@@1234");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();

    }

    @Test(dependsOnMethods = {"changePassword"})
    public void forgotPassword() {
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).userForgetsPassword();
        robot.sees(AppAllPages.resetPasswordPage).resetPassword(fetchProperty("gmailsignup.userEmail"), fetchProperty("gmailsignup.userPassword"));
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("gmailsignup.userEmail"), fetchProperty("gmailsignup.userPassword"));
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
    }
}
