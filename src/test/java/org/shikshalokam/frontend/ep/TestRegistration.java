package org.shikshalokam.frontend.ep;

import org.shikshalokam.backend.elevateUtility.CommonUtilitySAAS;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestRegistration {

    @BeforeClass()
    public void tearDown() {
        CommonUtilitySAAS.deleteUser(fetchProperty("ep.mail"), fetchProperty("ep.password"),fetchProperty("ep.superadminmail"), fetchProperty("ep.superadminpassword"));

    }
    @Test(description = "Verify Registration page working properly.")
    public void testRegistration() {
        Robot robot = new Robot();
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.registration).openAndFillRegistrationForm();
        robot.sees(AppAllPages.registration).getOTPAndFill();
        robot.sees(AppAllPages.homePage).verifyHomePage();
        robot.quitAppBrowser();
    }
}