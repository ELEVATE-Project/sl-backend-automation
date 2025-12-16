package org.shikshalokam.frontend.ep;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestRegistration {

    @Test(description = "Verify Registration page working properly.")
    public void testRegistration() {
        Robot robot = new Robot();
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.registration).openAndFillRegistrationForm();
        robot.sees(AppAllPages.registration).getOTPAndFill();
        robot.sees(AppAllPages.homePage).verifyHomePage();

        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).verifyHomePage();

        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).clickOnPrograms();
        robot.sees(AppAllPages.homePage).backBTNfromPrograms();
        robot.sees(AppAllPages.homePage).clickOnProject();
        robot.sees(AppAllPages.homePage).backBTNfromProject();
        robot.sees(AppAllPages.homePage).clickOnSurvey();
        robot.sees(AppAllPages.homePage).backBTNfromSurvey();
        robot.sees(AppAllPages.homePage).clickOnObservation();
        robot.sees(AppAllPages.homePage).backBTNfromObservations();
        robot.sees(AppAllPages.homePage).clickOnReports();
        robot.sees(AppAllPages.homePage).backBTNfromReports();
        robot.sees(AppAllPages.homePage).clickOnProfile();

    }
}