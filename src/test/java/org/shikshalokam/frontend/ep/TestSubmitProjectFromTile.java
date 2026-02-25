package org.shikshalokam.frontend.ep;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestSubmitProjectFromTile {
    @Test(description = "Submit project from tile")
    public void testSubmitProjectWithCertificateFromTile() {
        Robot robot = new Robot();
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).clickOnProject();
        robot.sees(AppAllPages.submitProjectFromTile).submitProjectFromTile(fetchProperty("ep.projectWithCertificateNameOnProjectPage"));
        robot.quitAppBrowser();
    }

    @Test(description = "Submit project from tile")
    public void testSubmitProjectWithOutCertificateFromTile() {
        Robot robot = new Robot();
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).clickOnProject();
        robot.sees(AppAllPages.submitProjectFromTile).submitProjectFromTile(fetchProperty("ep.projectWithOutCertificateNameOnProjectPage"));
        robot.quitAppBrowser();
    }
}

