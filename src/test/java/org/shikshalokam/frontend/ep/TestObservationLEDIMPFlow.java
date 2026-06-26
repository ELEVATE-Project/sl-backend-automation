package org.shikshalokam.frontend.ep;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestObservationLEDIMPFlow {
    @Test(description = "Verify that user is able to Observation LED IMP.")
    public void testObservationLEDIMP() {
        Robot robot = new Robot();
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).clickOnProject();
        robot.sees(AppAllPages.observationLEDIMPFlowPage).checkProjectIsVisible(fetchProperty("ep.projectNameLEDIMP"), false);
        robot.sees(AppAllPages.homePage).clickOnObservation();
        robot.sees(AppAllPages.observationLEDIMPFlowPage).submitObservationLEDIMP(fetchProperty("ep.observationLEDIMPName"));
        robot.sees(AppAllPages.homePage).clickOnProject();
        robot.sees(AppAllPages.observationLEDIMPFlowPage).checkProjectIsVisible(fetchProperty("ep.projectNameLEDIMP"), true);
        robot.quitAppBrowser();
    }
}
