package org.shikshalokam.frontend.ep;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestSubmitSurveyFromTile {
    @Test(description = "Submit survey from tile")
    public void testSubmitSurveyFromTile() {
        Robot robot = new Robot();
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).clickOnSurvey();
        robot.sees(AppAllPages.submitSurveyFromTile).submitSurveyFromTile(fetchProperty("ep.surveyNameOnSurveyPage"));
        robot.quitAppBrowser();
    }
}

