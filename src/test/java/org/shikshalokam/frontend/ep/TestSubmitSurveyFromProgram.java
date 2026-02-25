package org.shikshalokam.frontend.ep;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestSubmitSurveyFromProgram {
    @Test(description = "Submit survey from program")
    public void testSubmitSurveyFromProgram() {
        Robot robot = new Robot();
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).clickOnPrograms();
        robot.sees(AppAllPages.submitProjectFromProgram).selectProgramName(fetchProperty("ep.programName"));
        robot.sees(AppAllPages.submitSurveyFromProgram).submitSurveyFromProgram(fetchProperty("ep.surveyName"));
        robot.quitAppBrowser();
    }
}

