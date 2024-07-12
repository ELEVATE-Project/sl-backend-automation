package org.shikshalokam.frontend;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.AppAllPages.createSessionPage;
import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestMentorSessionCreation {

    @Test(description = "Mentor creates BBB session ,Mentee enroll's & joins the session, Mentor Ends the session and feedback is submitted by both.")
    public void testMentorSessionBBB() {
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentor.user"),
                fetchProperty("default.mentor.password"));
        robot.sees(AppAllPages.welcomePage).myMentoringSessionTab();
        robot.sees(AppAllPages.welcomePage).createSession();
        robot.sees(createSessionPage).bbbSessionCreation();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentee.user"),
                fetchProperty("default.mentee.password"));
        robot.sees(AppAllPages.welcomePage).sessionSearch(createSessionPage.bbbSessionTitle);
        robot.sees(AppAllPages.sessionDeatilsPage).enrollSession();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentor.user"),
                fetchProperty("default.mentor.password"));
        robot.sees(AppAllPages.welcomePage).myMentoringSessionTab();
        robot.sees(AppAllPages.welcomePage).selectcreatedSession(createSessionPage.bbbSessionTitle);
        robot.sees(AppAllPages.sessionDeatilsPage).startSession();
        robot.sees(AppAllPages.sessionDeatilsPage).bbbSessionOptions();
        robot.sees(AppAllPages.sessionDeatilsPage).leaveSession();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentee.user"),
                fetchProperty("default.mentee.password"));
        robot.sees(AppAllPages.welcomePage).sessionSearch(createSessionPage.bbbSessionTitle);
        robot.sees(AppAllPages.sessionDeatilsPage).joinSession();
        robot.sees(AppAllPages.sessionDeatilsPage).bbbSessionOptions();
        robot.sees(AppAllPages.sessionDeatilsPage).leaveSession();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentor.user"),
                fetchProperty("default.mentor.password"));
        robot.sees(AppAllPages.welcomePage).myMentoringSessionTab();
        robot.sees(AppAllPages.welcomePage).selectcreatedSession(createSessionPage.bbbSessionTitle);
        robot.sees(AppAllPages.sessionDeatilsPage).startSession();
        robot.sees(AppAllPages.sessionDeatilsPage).bbbSessionOptions();
        robot.sees(AppAllPages.sessionDeatilsPage).terminateSession();
        robot.sees(AppAllPages.sessionDeatilsPage).submitFeedback();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentee.user"),
                fetchProperty("default.mentee.password"));
        robot.sees(AppAllPages.sessionDeatilsPage).submitFeedback();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();

    }

    @Test(description = "Mentor initially creates BBB session & updates session link to Gmeet ,Mentee enroll's & Unenroll ,Mentee count & List is verified")
    public void testMentorSessionGmeetMenteeCount() {
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentor.user"),
                fetchProperty("default.mentor.password"));
        robot.sees(AppAllPages.welcomePage).myMentoringSessionTab();
        robot.sees(AppAllPages.welcomePage).createSession();
        robot.sees(createSessionPage).menteeCountSessionCreation();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentee.user"),
                fetchProperty("default.mentee.password"));
        robot.sees(AppAllPages.welcomePage).sessionSearch(createSessionPage.menteeCountSessionTitle);
        robot.sees(AppAllPages.sessionDeatilsPage).enrollSession();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentor.user"),
                fetchProperty("default.mentor.password"));
        robot.sees(AppAllPages.welcomePage).myMentoringSessionTab();
        robot.sees(AppAllPages.welcomePage).selectcreatedSession(createSessionPage.menteeCountSessionTitle);
        robot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeCount("1");
        robot.sees(AppAllPages.sessionDeatilsPage).viewAndVerifyList(fetchProperty("default.mentee.user"));
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentee.user"),
                fetchProperty("default.mentee.password"));
        robot.sees(AppAllPages.welcomePage).sessionSearch(createSessionPage.menteeCountSessionTitle);
        robot.sees(AppAllPages.sessionDeatilsPage).unEnrollSession();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentor.user"),
                fetchProperty("default.mentor.password"));
        robot.sees(AppAllPages.welcomePage).myMentoringSessionTab();
        robot.sees(AppAllPages.welcomePage).selectcreatedSession(createSessionPage.menteeCountSessionTitle);
        robot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeCount("0");
        robot.sees(AppAllPages.sessionDeatilsPage).deleteSession();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();

    }
}