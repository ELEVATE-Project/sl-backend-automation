package org.shikshalokam.frontend;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestOtherOptionOnProfile {
    @Test
    public void testOtherOption() {
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("mentor.qa.admin.login.user"),
                fetchProperty("mentor.qa.admin.login.password"));
        robot.sees(AppAllPages.welcomePage).profile();
        robot.sees(AppAllPages.profileDetailsPage).editProfile();
        robot.sees(AppAllPages.profileDetailsPage).otherOption("Your role");
        robot.sees(AppAllPages.profileDetailsPage).otherOptionValuePopUp("Enter your role","Principle");
        robot.sees(AppAllPages.profileDetailsPage).otherOption("Your expertise");
        robot.sees(AppAllPages.profileDetailsPage).otherOptionValuePopUp("Enter your expertise","Management");
        robot.sees(AppAllPages.profileDetailsPage).submit();
        robot.sees(AppAllPages.welcomePage).profile();
        robot.sees(AppAllPages.profileDetailsPage).editProfile();
        robot.sees(AppAllPages.profileDetailsPage).verifyAddedOptionIsSelectedAndUnselect("Principle");
        robot.sees(AppAllPages.profileDetailsPage).verifyAddedOptionIsSelectedAndUnselect("Management");
        robot.sees(AppAllPages.profileDetailsPage).submit();
        robot.sees(AppAllPages.welcomePage).profile();
        robot.sees(AppAllPages.profileDetailsPage).editProfile();
        robot.sees(AppAllPages.profileDetailsPage).verifyAddedOptionIsRemoved("Principle");
        robot.sees(AppAllPages.profileDetailsPage).verifyAddedOptionIsRemoved("Management");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
    }
}