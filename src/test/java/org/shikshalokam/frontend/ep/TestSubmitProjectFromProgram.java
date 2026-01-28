package org.shikshalokam.frontend.ep;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestSubmitProjectFromProgram {
    @Test(priority = 1, description = "Submit project from program With Certificate")
    public void testSubmitProjectWithCertificateFromProgram() {
        Robot robot = new Robot();
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).clickOnPrograms();
        robot.sees(AppAllPages.submitProjectFromProgram).selectProgramName(fetchProperty("ep.programName"));
        robot.sees(AppAllPages.submitProjectFromProgram).submitProjectFromProgram(fetchProperty("ep.projectWithCertificateName"));
    }
    @Test(priority = 2, description = "Submit project from program WithOut Certificate")
    public void testSubmitProjectWithOutCertificateFromProgram() {
        Robot robot = new Robot();
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).clickOnPrograms();
        robot.sees(AppAllPages.submitProjectFromProgram).selectProgramName(fetchProperty("ep.programName"));
        robot.sees(AppAllPages.submitProjectFromProgram).submitProjectFromProgram(fetchProperty("ep.projectWithOutCertificateName"));
    }
}

