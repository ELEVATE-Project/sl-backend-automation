package org.shikshalokam.frontend.ep;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestSubmitObservationWithRubricFromTile {
    Robot robot = new Robot();
    @Test(priority = 1, description = "Submit observation with rubric from Tile")
    public void testSubmitObservationWithRubricFromTile() {
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).clickOnObservation();
        robot.sees(AppAllPages.submitObservationWithRubricFromTile).submitObservationWithRubricFromTile(fetchProperty("ep.observationWithRubricNameOnObservationPage"));
        robot.quitAppBrowser();
    }

    @Test(priority = 2,description = "Submit observation with rubric from Tile again")
    public void testObservationWithRubricFromTileObserveAgain() {
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).clickOnObservation();
        robot.sees(AppAllPages.submitObservationWithRubricFromTile).submitObservationWithRubricFromTileAgain(fetchProperty("ep.observationWithRubricNameOnObservationPage"),1);
        robot.quitAppBrowser();
    }
    @Test(priority = 3, description = "Submit observation with rubric from Tile by adding new Entity")
    public void testObservationWithRubricFromTileAddEntity() {
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).clickOnObservation();
        robot.sees(AppAllPages.submitObservationWithRubricFromTile).submitObservationWithRubricFromTileAddEntity(fetchProperty("ep.observationWithRubricNameOnObservationPage"),fetchProperty("ep.addEntityName"));
        robot.quitAppBrowser();
    }
}

