package org.shikshalokam.frontend.ep;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

public class TestObservationAsTask {

    @Test(description = "Verify Observation as task is working")
    public void testObservationAsTask() throws InterruptedException {
        Robot robot = new Robot();
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).clickOnPrograms();
        robot.sees(AppAllPages.observationAsTask).submitObservationAsTask();
        robot.quitAppBrowser();
    }


}
