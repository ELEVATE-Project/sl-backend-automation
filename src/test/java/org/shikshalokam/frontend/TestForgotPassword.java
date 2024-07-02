package org.shikshalokam.frontend;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

public class TestForgotPassword {

    @Test
    public void forgotPassword() {
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).userForgetsPassword();
        robot.sees(AppAllPages.resetPasswordPage).resetPassword("slautoraj@gmail.com", "PassworD@@@1234");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp("slautoraj@gmail.com", "PassworD@@@1234");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();

    }
}