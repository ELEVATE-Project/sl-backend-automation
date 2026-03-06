package org.shikshalokam.frontend.ep;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestSubmitObservationWithoutRubricFromTile {
    Robot robot = new Robot();
    @Test(priority = 1, description = "Submit observation without rubric from tile")
    public void testSubmitObservationWithoutRubricFromTile() {
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).clickOnObservation();
        robot.sees(AppAllPages.submitObservationWithoutRubricFromTile).submitObservationWithoutRubricFromTile(fetchProperty("ep.observationWithoutRubricNameOnObservationPage"));
        robot.quitAppBrowser();
    }
    @Test(priority = 2, description = "Submit observation without rubric from tile Observe Again")
    public void testSubmitObservationWithoutRubricFromTileObserveAgain() {
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).clickOnObservation();
        robot.sees(AppAllPages.submitObservationWithoutRubricFromTile).submitObservationWithoutRubricFromTileObserveAgain(fetchProperty("ep.observationWithoutRubricNameOnObservationPage"),1);
        robot.quitAppBrowser();
    }
    @Test(priority = 3, description = "Submit observation without rubric from tile Add Entity")
    public void testSubmitObservationWithoutRubricFromTileAddEntity() {
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).clickOnObservation();
        robot.sees(AppAllPages.submitObservationWithoutRubricFromTile).submitObservationWithoutRubricFromTileAddEntity(fetchProperty("ep.observationWithoutRubricNameOnObservationPage"),fetchProperty("ep.addEntityName"));
        robot.quitAppBrowser();
    }
}

