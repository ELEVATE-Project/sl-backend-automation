package org.shikshalokam.frontend.ep;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestRegistration
{

    @Test(description = "Verify Registration page working properly.")
    public void testRegistration()
    {
        Robot robot = new Robot();
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.registration).openRegistrationPage();
        robot.sees(AppAllPages.registration).getOTP();

    }
}
