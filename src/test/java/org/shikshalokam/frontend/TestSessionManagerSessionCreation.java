package org.shikshalokam.frontend;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.PWBasePage;
import org.shikshalokam.uiPageObjects.PWBrowser;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.AppAllPages.createSessionPage;
import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestSessionManagerSessionCreation {
    @Test
    public void testPrivateSessionCreation() {
        Robot robot = new Robot();
        robot.openApp();
        if (PWBasePage.PWBrowser == PWBrowser.chromePixel4a || PWBasePage.PWBrowser == PWBrowser.msedgePixel4a) {
            // Reinitialize the browser for the admin tasks
            PWBasePage.setBrowserType(PWBrowser.chromium.toString());
            PWBasePage.reInitializePage();

            Robot adminRobot = new Robot();
            adminRobot.openApp();
            adminRobot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("defaultorgadmin.user"),
                    fetchProperty("defaultorgadmin.password"));
            adminRobot.sees(AppAllPages.welcomePage).workspace();
            adminRobot.sees(AppAllPages.workspacePage).manageSession();
            adminRobot.sees(AppAllPages.welcomePage).createSession();
            adminRobot.sees(AppAllPages.createSessionPage).creatingPrivateSession();
            adminRobot.sees(AppAllPages.sessionDeatilsPage).verifyAddedMentorName(createSessionPage.addMentorName);
            adminRobot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeCount("1");
            adminRobot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeListName(createSessionPage.addMenteeName);
            adminRobot.sees(AppAllPages.welcomePage).backToWorkSpace();
            adminRobot.sees(AppAllPages.welcomePage).logOutFromApp();
            adminRobot.quitAppBrowser();
            PWBasePage.setDefaultBrowserType();
            robot.openApp();
        }
        else {
            robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("defaultorgadmin.user"),
                    fetchProperty("defaultorgadmin.password"));
            robot.sees(AppAllPages.welcomePage).workspace();
            robot.sees(AppAllPages.workspacePage).manageSession();
            robot.sees(AppAllPages.welcomePage).createSession();
            robot.sees(AppAllPages.createSessionPage).creatingPrivateSession();
            robot.sees(AppAllPages.sessionDeatilsPage).verifyAddedMentorName(createSessionPage.addMentorName);
            robot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeCount("1");
            robot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeListName(createSessionPage.addMenteeName);
            robot.sees(AppAllPages.welcomePage).logOutFromApp();
        }

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

        if (PWBasePage.PWBrowser == PWBrowser.chromePixel4a || PWBasePage.PWBrowser == PWBrowser.msedgePixel4a) {
            // Reinitialize the browser for the admin tasks
            PWBasePage.setBrowserType(PWBrowser.chromium.toString());
            PWBasePage.reInitializePage();

            Robot adminRobot = new Robot();
            adminRobot.openApp();
            adminRobot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("defaultorgadmin.user"),
                    fetchProperty("defaultorgadmin.password"));
            adminRobot.sees(AppAllPages.welcomePage).workspace();
            adminRobot.sees(AppAllPages.workspacePage).manageSession();
            adminRobot.sees(AppAllPages.welcomePage).selectSessionFromList(createSessionPage.privateSessionTitle);
            adminRobot.sees(AppAllPages.sessionDeatilsPage).deleteSession();
            adminRobot.sees(AppAllPages.welcomePage).logOutFromApp();
            adminRobot.quitAppBrowser();
            PWBasePage.setDefaultBrowserType();
        }
        else {
            robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("defaultorgadmin.user"),
                    fetchProperty("defaultorgadmin.password"));
            robot.sees(AppAllPages.welcomePage).workspace();
            robot.sees(AppAllPages.workspacePage).manageSession();
            robot.sees(AppAllPages.welcomePage).selectSessionFromList(createSessionPage.privateSessionTitle);
            robot.sees(AppAllPages.sessionDeatilsPage).deleteSession();
            robot.sees(AppAllPages.welcomePage).logOutFromApp();
            robot.quitAppBrowser();
        }


    }

    @Test
    public void testPublicSessionCreation() {
        Robot robot = new Robot();
        robot.openApp();
        if (PWBasePage.PWBrowser == PWBrowser.chromePixel4a || PWBasePage.PWBrowser == PWBrowser.msedgePixel4a) {
            // Reinitialize the browser for the admin tasks
            PWBasePage.setBrowserType(PWBrowser.chromium.toString());
            PWBasePage.reInitializePage();

            Robot adminRobot = new Robot();
            adminRobot.openApp();
            adminRobot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("defaultorgadmin.user"),
                    fetchProperty("defaultorgadmin.password"));
            adminRobot.sees(AppAllPages.welcomePage).workspace();
            adminRobot.sees(AppAllPages.workspacePage).manageSession();
            adminRobot.sees(AppAllPages.welcomePage).createSession();
            adminRobot.sees(AppAllPages.createSessionPage).creatingPublicSession();
            adminRobot.sees(AppAllPages.sessionDeatilsPage).verifyAddedMentorName(createSessionPage.addMentorName);
            adminRobot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeCount("0");
            adminRobot.sees(AppAllPages.welcomePage).logOutFromApp();
            adminRobot.quitAppBrowser();
            PWBasePage.setDefaultBrowserType();
            robot.openApp();
        }
        else {
            robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("defaultorgadmin.user"),
                    fetchProperty("defaultorgadmin.password"));
            robot.sees(AppAllPages.welcomePage).workspace();
            robot.sees(AppAllPages.workspacePage).manageSession();
            robot.sees(AppAllPages.welcomePage).createSession();
            robot.sees(AppAllPages.createSessionPage).creatingPublicSession();
            robot.sees(AppAllPages.sessionDeatilsPage).verifyAddedMentorName(createSessionPage.addMentorName);
            robot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeCount("0");
            robot.sees(AppAllPages.welcomePage).logOutFromApp();
        }

        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentor.user"),
                fetchProperty("default.mentor.password"));
        robot.sees(AppAllPages.welcomePage).myMentoringSessionTab();
        robot.sees(AppAllPages.welcomePage).verifyTags(createSessionPage.publicSessionTitle, "Assigned");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();

        if (PWBasePage.PWBrowser == PWBrowser.chromePixel4a || PWBasePage.PWBrowser == PWBrowser.msedgePixel4a) {
            // Reinitialize the browser for the admin tasks
            PWBasePage.setBrowserType(PWBrowser.chromium.toString());
            PWBasePage.reInitializePage();

            Robot adminRobot = new Robot();
            adminRobot.openApp();
            adminRobot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("defaultorgadmin.user"),
                    fetchProperty("defaultorgadmin.password"));
            adminRobot.sees(AppAllPages.welcomePage).workspace();
            adminRobot.sees(AppAllPages.workspacePage).manageSession();
            adminRobot.sees(AppAllPages.welcomePage).selectSessionFromList(createSessionPage.publicSessionTitle);
            adminRobot.sees(AppAllPages.sessionDeatilsPage).editSession();
            adminRobot.sees(AppAllPages.createSessionPage).addMentee();
            adminRobot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeCount("1");
            adminRobot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeListName(createSessionPage.addMenteeName);
            adminRobot.sees(AppAllPages.welcomePage).backToWorkSpace();
            adminRobot.sees(AppAllPages.welcomePage).logOutFromApp();
            adminRobot.quitAppBrowser();
            PWBasePage.setDefaultBrowserType();
            robot.openApp();
        }
        else {
            robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("defaultorgadmin.user"),
                    fetchProperty("defaultorgadmin.password"));
            robot.sees(AppAllPages.welcomePage).workspace();
            robot.sees(AppAllPages.workspacePage).manageSession();
            robot.sees(AppAllPages.welcomePage).selectSessionFromList(createSessionPage.publicSessionTitle);
            robot.sees(AppAllPages.sessionDeatilsPage).editSession();
            robot.sees(AppAllPages.createSessionPage).addMentee();
            robot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeCount("1");
            robot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeListName(createSessionPage.addMenteeName);
            robot.sees(AppAllPages.welcomePage).logOutFromApp();
        }

        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("default.mentee.user"),
                fetchProperty("default.mentee.password"));
        robot.sees(AppAllPages.welcomePage).enrolledSession();
        robot.sees(AppAllPages.welcomePage).verifyTags(createSessionPage.publicSessionTitle, "Invited");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();


        if (PWBasePage.PWBrowser == PWBrowser.chromePixel4a || PWBasePage.PWBrowser == PWBrowser.msedgePixel4a) {
            // Reinitialize the browser for the admin tasks
            PWBasePage.setBrowserType(PWBrowser.chromium.toString());
            PWBasePage.reInitializePage();

            Robot adminRobot = new Robot();
            adminRobot.openApp();
            adminRobot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("defaultorgadmin.user"),
                    fetchProperty("defaultorgadmin.password"));
            adminRobot.sees(AppAllPages.welcomePage).workspace();
            adminRobot.sees(AppAllPages.workspacePage).manageSession();
            adminRobot.sees(AppAllPages.welcomePage).selectSessionFromList(createSessionPage.publicSessionTitle);
            adminRobot.sees(AppAllPages.sessionDeatilsPage).deleteSession();
            adminRobot.sees(AppAllPages.welcomePage).logOutFromApp();
            adminRobot.quitAppBrowser();
            PWBasePage.setDefaultBrowserType();
        }
        else {
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
}
