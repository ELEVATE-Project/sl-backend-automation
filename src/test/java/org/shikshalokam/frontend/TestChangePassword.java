package org.shikshalokam.frontend;

import org.shikshalokam.backend.MentorEDBaseTest;
import org.shikshalokam.backend.PropertyLoader;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

public class TestChangePassword extends MentorEDBaseTest {

    @Test
    public void changePassword() {
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp((PropertyLoader.PROP_LIST.getProperty("mentoring.qa.user")), (PropertyLoader.PROP_LIST.getProperty("mentoring.qa.password")));
        robot.sees(AppAllPages.welcomePage).requesttochangePassword();
        robot.sees(AppAllPages.changePassword).changePassword((PropertyLoader.PROP_LIST.getProperty("mentoring.qa.password")), (PropertyLoader.PROP_LIST.getProperty("mentoring.qa.newpassword")),
                (PropertyLoader.PROP_LIST.getProperty("mentoring.qa.newpassword")));
        robot.sees(AppAllPages.loginPage).loginToApp((PropertyLoader.PROP_LIST.getProperty("mentoring.qa.user")), (PropertyLoader.PROP_LIST.getProperty("mentoring.qa.newpassword")));
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();

    }
}
