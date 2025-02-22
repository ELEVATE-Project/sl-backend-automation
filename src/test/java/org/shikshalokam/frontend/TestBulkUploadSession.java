package org.shikshalokam.frontend;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.MentorEDSessionUpdate;
import org.shikshalokam.backend.SMsampleCSVBulkPuload;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.backend.PropertyLoader.PROP_LIST;
import static org.shikshalokam.uiPageObjects.PWBasePage.*;

public class TestBulkUploadSession extends SMsampleCSVBulkPuload {
    private static final Logger logger = LogManager.getLogger(TestBulkUploadSession.class);

    @Test(description = "downloading the sample Csv")
    public void downloadSampleCSV() {
        logger.info("Logging into the application :");
        loginToMentorED(PROP_LIST.get("mentor.qa.admin.login.user").toString(), PROP_LIST.get("mentor.qa.admin.login.password").toString());
        uploadSampleFileToCloud();
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("mentor.qa.admin.login.user"),
                fetchProperty("mentor.qa.admin.login.password"));
        robot.sees(AppAllPages.welcomePage).workspace();
        robot.sees(AppAllPages.workspacePage).manageSession();
        robot.sees(AppAllPages.manageSessionPage).bulkUploadTab();
        robot.sees(AppAllPages.manageSessionPage).downloadSampleCsv("bulksession_files", "bulk_session_creation.csv");
    }

    @Test(description = "Uploading the Csv file via bulk upload option to Create sessions",
            dependsOnMethods = {"downloadSampleCSV"})
    public void sessionCreationViaBulkUpload() {
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("mentor.qa.admin.login.user"),
                fetchProperty("mentor.qa.admin.login.password"));
        robot.sees(AppAllPages.welcomePage).workspace();
        robot.sees(AppAllPages.workspacePage).manageSession();
        robot.sees(AppAllPages.manageSessionPage).bulkUploadTab();
        robot.sees(AppAllPages.manageSessionPage).uploadSessionCreationCsv();
        robot.sees(AppAllPages.manageSessionPage).createSessionsTab();
        robot.sees(AppAllPages.manageSessionPage).verifyCreatedSession("AutoPrivateBulkUploadSession");
        robot.sees(AppAllPages.welcomePage).selectSessionFromList("AutoPrivateBulkUploadSession");
        robot.sees(AppAllPages.sessionDeatilsPage).deleteSession();
        robot.sees(AppAllPages.welcomePage).workspace();
        robot.sees(AppAllPages.workspacePage).manageSession();
        robot.sees(AppAllPages.manageSessionPage).verifyCreatedSession("AutoPublicBulkUploadSession");
        robot.sees(AppAllPages.welcomePage).selectSessionFromList("AutoPublicBulkUploadSession");
        robot.sees(AppAllPages.sessionDeatilsPage).deleteSession();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
    }


    @Test(description = "Uploading the Csv file via bulk upload option to Edit sessions")
    public void sessionEditViaBulkUpload() {
        MentorEDSessionUpdate.sessionCreationAndFetchId();
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("defaultorgadmin.user"),
                fetchProperty("defaultorgadmin.password"));
        robot.sees(AppAllPages.welcomePage).workspace();
        robot.sees(AppAllPages.workspacePage).manageSession();
        robot.sees(AppAllPages.manageSessionPage).bulkUploadTab();
        robot.sees(AppAllPages.manageSessionPage).uploadEditedCsv();
        robot.sees(AppAllPages.manageSessionPage).createSessionsTab();
        robot.sees(AppAllPages.welcomePage).selectSessionFromList("AutoPublicSession");
        robot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeCount("1");
        robot.sees(AppAllPages.sessionDeatilsPage).backbutton();
        robot.sees(AppAllPages.welcomePage).selectSessionFromList("AutoPrivateSession");
        robot.sees(AppAllPages.sessionDeatilsPage).verifyMenteeCount("2");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
    }

    @Test(description = "Uploading the Csv file via bulk upload option to Delete sessions", dependsOnMethods = {"sessionEditViaBulkUpload"})
    public void sessionDeleteViaBulkUpload() {
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("defaultorgadmin.user"),
                fetchProperty("defaultorgadmin.password"));
        robot.sees(AppAllPages.welcomePage).workspace();
        robot.sees(AppAllPages.workspacePage).manageSession();
        robot.sees(AppAllPages.manageSessionPage).bulkUploadTab();
        robot.sees(AppAllPages.manageSessionPage).uploadDeleteCsv();
        robot.sees(AppAllPages.manageSessionPage).createSessionsTab();
        robot.sees(AppAllPages.manageSessionPage).verifyDeletedSessions("AutoPublicSession");
        robot.sees(AppAllPages.manageSessionPage).verifyDeletedSessions("AutoPrivateSession");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
    }
}