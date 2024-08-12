package org.shikshalokam.frontend;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.PWBasePage;
import org.shikshalokam.uiPageObjects.PWBrowser;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.AppAllPages.createSessionPage;
import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestMentorSessionCreation {

    @Test
    public void testMentorSessionBBB() {
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentor.user"),
                fetchProperty("default.mentor.password"));
        robot.sees(AppAllPages.welcomePage).myMentoringSessionTab();
        robot.sees(AppAllPages.welcomePage).createSession();
        robot.sees(createSessionPage).bbbSessionCreation();
        if (PWBasePage.PWBrowser == PWBrowser.chromePixel4a || PWBasePage.PWBrowser == PWBrowser.msedgePixel4a) {
            robot.sees(AppAllPages.welcomePage).backToWorkSpace();
        }
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentee.user"),
                fetchProperty("default.mentee.password"));
        robot.sees(AppAllPages.welcomePage).sessionSearch(createSessionPage.bbbSessionTitle);
        robot.sees(AppAllPages.sessionDeatilsPage).enrollSession();
        if (PWBasePage.PWBrowser == PWBrowser.chromePixel4a || PWBasePage.PWBrowser == PWBrowser.msedgePixel4a) {
            robot.sees(AppAllPages.welcomePage).backToWorkSpace();
        }
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
        robot.quitAppBrowser();

    }

    @Test
    public void testMentorSessionGmeetMenteeCount() {
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentor.user"),
                fetchProperty("default.mentor.password"));
        robot.sees(AppAllPages.welcomePage).myMentoringSessionTab();
        robot.sees(AppAllPages.welcomePage).createSession();
        robot.sees(createSessionPage).menteeCountSessionCreation();
        robot.sees(AppAllPages.sessionDeatilsPage).editSession();
        robot.sees(createSessionPage).updateCreatedSessionWithGmeetPlatform();
        if (PWBasePage.PWBrowser == PWBrowser.chromePixel4a || PWBasePage.PWBrowser == PWBrowser.msedgePixel4a) {
            robot.sees(AppAllPages.welcomePage).backToWorkSpace();
        }
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentee.user"),
                fetchProperty("default.mentee.password"));
        robot.sees(AppAllPages.welcomePage).sessionSearch(createSessionPage.menteeCountSessionTitle);
        robot.sees(AppAllPages.sessionDeatilsPage).enrollSession();
        if (PWBasePage.PWBrowser == PWBrowser.chromePixel4a || PWBasePage.PWBrowser == PWBrowser.msedgePixel4a) {
            robot.sees(AppAllPages.welcomePage).backToWorkSpace();
        }
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentor.user"),
                fetchProperty("default.mentor.password"));
        robot.sees(AppAllPages.welcomePage).myMentoringSessionTab();
        robot.sees(AppAllPages.welcomePage).selectcreatedSession(createSessionPage.menteeCountSessionTitle);
        robot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeCount("1");
        if (PWBasePage.PWBrowser == PWBrowser.chromePixel4a || PWBasePage.PWBrowser == PWBrowser.msedgePixel4a) {
            robot.sees(AppAllPages.welcomePage).backToWorkSpace();
        }
        else {
            robot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeListEmail(fetchProperty("default.mentee.user"));
        }
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentee.user"),
                fetchProperty("default.mentee.password"));
        robot.sees(AppAllPages.welcomePage).sessionSearch(createSessionPage.menteeCountSessionTitle);
        robot.sees(AppAllPages.sessionDeatilsPage).unEnrollSession();
        if (PWBasePage.PWBrowser == PWBrowser.chromePixel4a || PWBasePage.PWBrowser == PWBrowser.msedgePixel4a) {
            robot.sees(AppAllPages.welcomePage).backToWorkSpace();
        }
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentor.user"),
                fetchProperty("default.mentor.password"));
        robot.sees(AppAllPages.welcomePage).myMentoringSessionTab();
        robot.sees(AppAllPages.welcomePage).selectcreatedSession(createSessionPage.menteeCountSessionTitle);
        robot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeCount("0");
        robot.sees(AppAllPages.sessionDeatilsPage).deleteSession();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();

    }
}