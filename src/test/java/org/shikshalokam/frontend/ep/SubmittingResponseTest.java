package org.shikshalokam.frontend.ep;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.shikshalokam.uiPageObjects.ep.AppSubmittingResponse;
import org.testng.annotations.Test;

public class SubmittingResponseTest {

    @Test (priority = 1, description = "Submit response for multiple users")
    public void testSubmitResponseForProjectWithCertificate() {
        Robot robot = new Robot();
        robot.sees(AppAllPages.eploginpage).openURL();
        AppSubmittingResponse submit =new AppSubmittingResponse("Submitting Response");
//        robot.debugAPP();
        submit.readDataFromSheet();
        robot.quitAppBrowser();

    }
}
