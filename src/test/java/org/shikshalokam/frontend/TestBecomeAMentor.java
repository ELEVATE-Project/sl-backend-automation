package org.shikshalokam.frontend;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

public class TestBecomeAMentor {

@Test
<<<<<<< Updated upstream
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

=======
public void becomeMentor()  {
	Robot robot = new Robot();
	robot.openApp();
	robot.sees(AppAllPages.loginPage).loginToApp("slautoraj@gmail.com", "PassworD@@@123");
	robot.sees(AppAllPages.welcomePage).becomeMentor();
	robot.sees(AppAllPages.welcomePage).updateButton();
	robot.sees(AppAllPages.updateProfilePage).updateProfilePage();
	robot.sees(AppAllPages.welcomePage).becomeMentor();
	robot.sees(AppAllPages.mentorPage).requestToBeAMentor();
	robot.sees(AppAllPages.welcomePage).logOutFromApp();
	robot.sees(AppAllPages.loginPage).loginToApp("jubedhashaik029@gmail.com", "PAssword@@123$");
	robot.sees(AppAllPages.welcomePage).workspace();
	robot.sees(AppAllPages.workspacePage).acceptAsMentor();
	robot.sees(AppAllPages.welcomePage).logOutFromApp();
	robot.sees(AppAllPages.loginPage).loginToApp("slautoraj@gmail.com", "PassworD@@@123");
	robot.sees(AppAllPages.welcomePage).verifiedUserAsaMentor();
	robot.sees(AppAllPages.welcomePage).logOutFromApp();
	robot.quitAppBrowser();
}
>>>>>>> Stashed changes
}