package org.shikshalokam.frontend.ep;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

public class TestProjectDeeplink {

    @Test(description = "Verify that user is able to submit a project using deeplink.")
    public void testSubmitProjectUsingDeeplink() {
        Robot robot = new Robot();
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.submitProjectDeeplink).submitProjectUsingDeeplink();
        robot.quitAppBrowser();
    }
}
