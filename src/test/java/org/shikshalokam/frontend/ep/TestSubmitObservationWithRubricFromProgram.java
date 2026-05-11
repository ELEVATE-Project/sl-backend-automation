package org.shikshalokam.frontend.ep;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestSubmitObservationWithRubricFromProgram {
    Robot robot = new Robot();
    @Test(priority = 1, description = "Submit observation with rubric from program")
    public void testSubmitObservationWithRubricFromProgram() {
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).clickOnPrograms();
        robot.sees(AppAllPages.submitProjectFromProgram).selectProgramName(fetchProperty("ep.programName"));
//        robot.debugAPP();
        robot.sees(AppAllPages.submitObservationWithRubricFromProgram).submitObservationWithRubricFromProgram(fetchProperty("ep.observationWithRubricName"));
        robot.quitAppBrowser();
    }

    @Test(priority = 2,description = "Submit observation with rubric from program again")
    public void testObservationWithRubricFromProgramObserveAgain() {
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).clickOnPrograms();
        robot.sees(AppAllPages.submitProjectFromProgram).selectProgramName(fetchProperty("ep.programName"));
        robot.sees(AppAllPages.submitObservationWithRubricFromProgram).submitObservationWithRubricFromProgramAgain(fetchProperty("ep.observationWithRubricName"),1);
        robot.quitAppBrowser();
    }

    @Test(priority = 3, description = "Submit observation with rubric from program AddEntity")
    public void testObservationWithRubricFromProgramAddEntity() {
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).clickOnPrograms();
        robot.sees(AppAllPages.submitProjectFromProgram).selectProgramName(fetchProperty("ep.programName"));
        robot.sees(AppAllPages.submitObservationWithRubricFromProgram).submitObservationWithRubricFromProgramAddEntity(fetchProperty("ep.observationWithRubricName"),fetchProperty("ep.addEntityName"));
        robot.quitAppBrowser();
    }
}

