package org.shikshalokam.frontend;

import org.shikshalokam.backend.MentorEDBaseTest;
import org.shikshalokam.backend.PropertyLoader;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.PWBasePage;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestChangePassword extends MentorEDBaseTest {

    @Test
    public void changePassword() {
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("gmailsignup.userEmail"), fetchProperty("gmailsignup.userPassword"));
        robot.sees(AppAllPages.welcomePage).requestToChangePassword();
        robot.sees(AppAllPages.changePasswordPage).changePassword(fetchProperty("gmailsignup.userPassword"), "PassworD@@@1234");
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("gmailsignup.userEmail"),"PassworD@@@1234" );
        robot.sees(AppAllPages.welcomePage).requestToChangePassword();
        // Resetting to Old Password
        robot.sees(AppAllPages.changePasswordPage).changePassword("PassworD@@@1234",fetchProperty("gmailsignup.userPassword"));
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("gmailsignup.userEmail"), fetchProperty("gmailsignup.userPassword"));
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();

    }
}
