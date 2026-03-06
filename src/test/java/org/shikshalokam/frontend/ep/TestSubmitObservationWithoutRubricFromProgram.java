package org.shikshalokam.frontend.ep;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestSubmitObservationWithoutRubricFromProgram {
    Robot robot = new Robot();
    @Test(priority = 1, description = "Submit observation without rubric from program")
    public void testSubmitObservationWithoutRubricFromProgram() {
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).clickOnPrograms();
        robot.sees(AppAllPages.submitProjectFromProgram).selectProgramName(fetchProperty("ep.programName"));
        robot.sees(AppAllPages.submitObservationWithoutRubricFromProgram).submitObservationWithoutRubricFromProgram(fetchProperty("ep.observationWithoutRubricName"));
        robot.quitAppBrowser();
    }

    @Test(priority = 2, description = "Submit observation without rubric from program Observe Again")
    public void testSubmitObservationWithoutRubricFromProgramObserveAgain() {
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).clickOnPrograms();
        robot.sees(AppAllPages.submitProjectFromProgram).selectProgramName(fetchProperty("ep.programName"));
        robot.sees(AppAllPages.submitObservationWithoutRubricFromProgram).submitObservationWithoutRubricFromProgramAgain(fetchProperty("ep.observationWithoutRubricName"),1);
        robot.quitAppBrowser();
    }

    @Test(priority = 3, description = "Submit observation without rubric from program Add Entity")
    public void testSubmitObservationWithoutRubricFromProgramAddEntity() {
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).clickOnPrograms();
        robot.sees(AppAllPages.submitProjectFromProgram).selectProgramName(fetchProperty("ep.programName"));
        robot.sees(AppAllPages.submitObservationWithoutRubricFromProgram).submitObservationWithoutRubricFromProgramAddEntity(fetchProperty("ep.observationWithoutRubricName"),fetchProperty("ep.addEntityName"));
        robot.quitAppBrowser();
    }
}

