package org.shikshalokam.frontend;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.AppAllPages.createSessionPage;
import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestSessionManagerSessionCreation {
    @Test(description = "Verify creation, verification, and deletion of a private session.")
    public void testPrivateSessionCreation() {
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("defaultorgadmin.user"),
                fetchProperty("defaultorgadmin.password"));
        robot.sees(AppAllPages.welcomePage).workspace();
        robot.sees(AppAllPages.workspacePage).manageSession();
        robot.sees(AppAllPages.welcomePage).createSession();
        robot.sees(AppAllPages.createSessionPage).creatingPrivateSession();
        robot.sees(AppAllPages.sessionDeatilsPage).verifyAddedMentorName(fetchProperty("addMentorName"));
        robot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeCount("1");
        robot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeListName(fetchProperty("addMenteeName"));
        robot.sees(AppAllPages.welcomePage).logOutFromApp();

        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentor.user"),
                fetchProperty("default.mentor.password"));
        robot.sees(AppAllPages.welcomePage).myMentoringSessionTab();
        robot.sees(AppAllPages.welcomePage).verifyTags(createSessionPage.privateSessionTitle, "Assigned");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();

        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentee.user"),
                fetchProperty("default.mentee.password"));
        robot.sees(AppAllPages.welcomePage).enrolledSession();
        robot.sees(AppAllPages.welcomePage).verifyTags(createSessionPage.privateSessionTitle, "Invited");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();

        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("defaultorgadmin.user"),
                fetchProperty("defaultorgadmin.password"));
        robot.sees(AppAllPages.welcomePage).workspace();
        robot.sees(AppAllPages.workspacePage).manageSession();
        robot.sees(AppAllPages.welcomePage).selectSessionFromList(createSessionPage.privateSessionTitle);
        robot.sees(AppAllPages.sessionDeatilsPage).deleteSession();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();

    }

    @Test(description = "Verify creation, verification, and deletion of a public session.")
    public void testPublicSessionCreation() {
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("defaultorgadmin.user"),
                fetchProperty("defaultorgadmin.password"));
        robot.sees(AppAllPages.welcomePage).workspace();
        robot.sees(AppAllPages.workspacePage).manageSession();
        robot.sees(AppAllPages.welcomePage).createSession();
        robot.sees(AppAllPages.createSessionPage).creatingPublicSession();
        robot.sees(AppAllPages.sessionDeatilsPage).verifyAddedMentorName(fetchProperty("addMentorName"));
        robot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeCount("0");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();

        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentor.user"),
                fetchProperty("default.mentor.password"));
        robot.sees(AppAllPages.welcomePage).myMentoringSessionTab();
        robot.sees(AppAllPages.welcomePage).verifyTags(createSessionPage.publicSessionTitle, "Assigned");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();

        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("defaultorgadmin.user"),
                fetchProperty("defaultorgadmin.password"));
        robot.sees(AppAllPages.welcomePage).workspace();
        robot.sees(AppAllPages.workspacePage).manageSession();
        robot.sees(AppAllPages.welcomePage).selectSessionFromList(createSessionPage.publicSessionTitle);
        robot.sees(AppAllPages.sessionDeatilsPage).editSession();
        robot.sees(AppAllPages.createSessionPage).addMentee();
        robot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeCount("1");
        robot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeListName(fetchProperty("addMenteeName"));
        robot.sees(AppAllPages.welcomePage).logOutFromApp();

        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentee.user"),
                fetchProperty("default.mentee.password"));
        robot.sees(AppAllPages.welcomePage).enrolledSession();
        robot.sees(AppAllPages.welcomePage).verifyTags(createSessionPage.publicSessionTitle, "Invited");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();

        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("defaultorgadmin.user"),
                fetchProperty("defaultorgadmin.password"));
        robot.sees(AppAllPages.welcomePage).workspace();
        robot.sees(AppAllPages.workspacePage).manageSession();
        robot.sees(AppAllPages.welcomePage).selectSessionFromList(createSessionPage.publicSessionTitle);
        robot.sees(AppAllPages.sessionDeatilsPage).deleteSession();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
    }
}