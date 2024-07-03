package org.shikshalokam.frontend;

import org.shikshalokam.backend.PropertyLoader;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestBecomeAMentor {

    @Test(dependsOnMethods = {"org.shikshalokam.frontend.TestMenteeSignUp.AppSignupTest"})
    public void becomeMentor() {
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("gmailsignup.userEmail"),
                fetchProperty("gmailsignup.userPassword"));
        robot.sees(AppAllPages.welcomePage).becomeMentor();
        robot.sees(AppAllPages.welcomePage).requestForProfileUpdate();
        robot.sees(AppAllPages.profileDetailsPage).updateProfile();
        robot.sees(AppAllPages.welcomePage).becomeMentor();
        robot.sees(AppAllPages.mentorPage).requestToBeAMentor();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("defaultorgadmin.user"), fetchProperty("defaultorgadmin.password"));
        robot.sees(AppAllPages.welcomePage).workspace();
        robot.sees(AppAllPages.workspacePage).acceptAsMentor();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("gmailsignup.userEmail"),
                fetchProperty("gmailsignup.userPassword"));
        robot.sees(AppAllPages.welcomePage).verifiedUserAsaMentor();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
    }

}