package org.shikshalokam.frontend;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

public class TestBecomeAMentor {

@Test
public void becomeMentor() throws InterruptedException {
	Robot robot = new Robot();
	robot.openApp();
	robot.sees(AppAllPages.loginPage).loginToApp("slautoraj@gmail.com", "PassworD@@@123");
	robot.sees(AppAllPages.mentorPage).becomeMentor();
	robot.sees(AppAllPages.loginPage).logOutFromApp();
	
	robot.sees(AppAllPages.loginPage).loginToApp("jubedhashaik029@gmail.com", "PAssword@@123$");
	robot.sees(AppAllPages.mentorPage).acceptAsMentor();
	robot.sees(AppAllPages.loginPage).logOutFromApp();

	robot.sees(AppAllPages.loginPage).loginToApp("slautoraj@gmail.com", "PassworD@@@123");
	robot.sees(AppAllPages.mentorPage).becomeMentorSuccessfull();
	robot.sees(AppAllPages.loginPage).logOutFromApp();
	
	robot.quitAppBrowser();
}

}