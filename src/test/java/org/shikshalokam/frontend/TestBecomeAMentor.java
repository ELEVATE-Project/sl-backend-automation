package org.shikshalokam.frontend;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.PWBasePage;
import org.shikshalokam.uiPageObjects.PWBrowser;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestBecomeAMentor {

    @Test(dependsOnMethods = {"org.shikshalokam.frontend.TestMenteeSignUp.testSignup"})
    public void testBecomeMentor() {
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("gmailsignup.userEmail"),
                fetchProperty("gmailsignup.userPassword"));
        robot.sees(AppAllPages.welcomePage).becomeMentor();
        robot.sees(AppAllPages.mentorPage).requestToBeAMentor();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        if (PWBasePage.PWBrowser == PWBrowser.chromePixel4a || PWBasePage.PWBrowser == PWBrowser.msedgePixel4a) {
                // Reinitialize the browser for the admin tasks
                PWBasePage.setBrowserType(PWBrowser.chromium.toString());
                PWBasePage.reInitializePage();

                Robot adminRobot = new Robot();
                adminRobot.openApp();
                adminRobot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("defaultorgadmin.user"),
                        fetchProperty("defaultorgadmin.password"));
                adminRobot.sees(AppAllPages.welcomePage).workspace();
                adminRobot.sees(AppAllPages.workspacePage).acceptAsMentor();
                adminRobot.sees(AppAllPages.welcomePage).logOutFromApp();
                adminRobot.quitAppBrowser();
                PWBasePage.setDefaultBrowserType();
                robot.openApp();
        }
        else {
            robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("defaultorgadmin.user"), fetchProperty("defaultorgadmin.password"));
            robot.sees(AppAllPages.welcomePage).workspace();
            robot.sees(AppAllPages.workspacePage).acceptAsMentor();
            robot.sees(AppAllPages.welcomePage).logOutFromApp();
        }
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("gmailsignup.userEmail"),
                fetchProperty("gmailsignup.userPassword"));
        robot.sees(AppAllPages.welcomePage).verifiedUserAsaMentor();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
    }

}