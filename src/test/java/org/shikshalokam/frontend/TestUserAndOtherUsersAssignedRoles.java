package org.shikshalokam.frontend;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.PWBasePage;
import org.shikshalokam.uiPageObjects.PWBrowser;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

@Test
public class TestUserAndOtherUsersAssignedRoles {

    @Test(description = "Verify the assigned roles for the logged-in user and other users within the application.")
    public void testUserAndOtherUsersAssignedRoles() {
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("mentor.qa.admin.login.user"),
                fetchProperty("mentor.qa.admin.login.password"));

        robot.sees(AppAllPages.welcomePage).viewRoles();
        robot.sees(AppAllPages.welcomePage).verifyRoles("Mentee","Mentor","Org Admin","Session Manager");

        robot.sees(AppAllPages.welcomePage).profile();
        robot.sees(AppAllPages.profileDetailsPage).viewRoles();
        robot.sees(AppAllPages.welcomePage).verifyRoles("Mentee","Mentor","Org Admin","Session Manager");

        robot.sees(AppAllPages.welcomePage).mentors();
        robot.sees(AppAllPages.mentorsPage).searchMentor(fetchProperty("search.mentor"));
        robot.sees(AppAllPages.mentorsPage).viewRoles();
        robot.sees(AppAllPages.welcomePage).verifyRoles("Mentee","Mentor");
        if (PWBasePage.PWBrowser == PWBrowser.chromePixel4a || PWBasePage.PWBrowser == PWBrowser.msedgePixel4a) {
            robot.sees(AppAllPages.welcomePage).backToWorkSpace();
        }
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();

    }
}
