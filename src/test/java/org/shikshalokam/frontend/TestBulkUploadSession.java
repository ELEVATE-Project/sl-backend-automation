package org.shikshalokam.frontend;

import com.microsoft.playwright.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.SMsampleCSVBulkPuload;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import static org.shikshalokam.backend.PropertyLoader.PROP_LIST;
import static org.shikshalokam.uiPageObjects.PWBasePage.*;

public class TestBulkUploadSession extends SMsampleCSVBulkPuload {
    private static final Logger logger = LogManager.getLogger(TestBulkUploadSession.class);

    @BeforeMethod
    public void init() {
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
        robot.sees(AppAllPages.manageSessionPage).downloadSampleCsv("bulksession_files","bulk_session_creation.csv");
    }


    @Test
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
        robot.sees(AppAllPages.manageSessionPage).verifyCreatedSession("AutoPublicBulkUploadSession");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
    }
}
