package org.shikshalokam.frontend;
import org.shikshalokam.backend.MentorEDBaseTest;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class TestMenteeSignUp extends MentorEDBaseTest {

@BeforeTest
    public void init() {
        loginToMentorED("jubedhashaik029@gmail.com","PAssword@@123$");
        deleteMenteeByGivenName("UserSignup");
    }

    @Test
    public void AppSignupTest() throws InterruptedException {
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).signUp();
        robot.sees(AppAllPages.SignupPage).SignupToApp("UserSignup", "slautoraj@gmail.com", "PassworD@@@123")
                .logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp("slautoraj@gmail.com", "PassworD@@@123").logOutFromApp();
        robot.quitAppBrowser();

    }
}
