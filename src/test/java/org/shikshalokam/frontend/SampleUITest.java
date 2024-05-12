package org.shikshalokam.frontend;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.AppLoginPage;
import org.shikshalokam.uiPageObjects.PWBasePage;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SampleUITest {


    @Test
    public void testUI()
    {
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp("adithya@shikshalokam.org", "Welcome@123").logOutFromApp();
        robot.quitAppBrowser();



    }



}
