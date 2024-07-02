package org.shikshalokam.frontend;

import org.shikshalokam.backend.PropertyLoader;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class SampleUITest {

    @Test
    public void testUI() {
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("mentor.qa.admin.login.user"),
                fetchProperty("mentor.qa.admin.login.password")).logOutFromApp();
        robot.quitAppBrowser();
    }

}
