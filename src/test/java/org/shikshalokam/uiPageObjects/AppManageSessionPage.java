package org.shikshalokam.uiPageObjects;

import com.microsoft.playwright.Download;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppManageSessionPage extends PWBasePage {
    private AppManageSessionPage manageSessionPage;
    private static final Logger logger = LogManager.getLogger(AppMentorsPage.class);

    public AppManageSessionPage(String givenTitleName) {
        super(givenTitleName);
        this.manageSessionPage = this;
    }

    public AppManageSessionPage bulkUploadTab() {
        this.validPage();
        page.locator("ion-segment-button").filter(new Locator.FilterOptions().setHasText("Bulk upload")).click();
        return manageSessionPage;
    }

    public AppManageSessionPage downloadSampleCsv(String folderName, String desiredFilename) {
        this.validPage();
        // Specify the desired folder and filename
        Download download = page.waitForDownload(() -> {
            page.click("ion-button:text('Download sample CSV')");
        });
        // Handle the download with the specified filename
        handleDownload(download, folderName, desiredFilename);
        return manageSessionPage;
    }

    public AppManageSessionPage createSessionsTab() {
        this.validPage();
        page.locator("ion-segment-button").filter(new Locator.FilterOptions().setHasText("Create sessions")).click();
        return manageSessionPage;
    }

    public AppManageSessionPage verifyCreatedSession(String session) {
        this.validPage();
        String createdsession = page.getByRole(AriaRole.CELL, new Page.GetByRoleOptions().setName(String.format(" %s ", session)))
                .locator("div").first().textContent();
        page.locator(createdsession).isVisible();
        logger.info(createdsession + " Session created Successfully");
        return manageSessionPage;
    }

    public AppManageSessionPage verifyDeletedSessions(String deletedSession) {
        this.validPage();
        Locator element = page.getByRole(AriaRole.CELL, new Page.GetByRoleOptions().setName(deletedSession)).locator("div");
        if (element.isVisible()) {
            throw new AssertionError("Deleted Session '" + deletedSession + "' is unexpectedly visible on the page.");
        }
        logger.info(deletedSession + " Session Deleted Successfully");
        return manageSessionPage;
    }

    public AppManageSessionPage uploadSessionCreationCsv() {
        this.validPage();
        Path filePath = null;
        String currentWorkingDirectory = System.getProperty("user.dir");
        if ("QA".equalsIgnoreCase(fetchProperty("environment"))) {
            filePath = Paths.get(currentWorkingDirectory, "target", "bulksession_files", "bulk_session_creation.csv");
            logger.info("File Path: " + filePath.toString());
        } else {
            filePath = Paths.get(currentWorkingDirectory, "src/main/resources", "bulk_session_creation.csv");
            logger.info("File Path: " + filePath.toString());
        }

        // Set the file to upload on the input[type="file"]
        page.setInputFiles("//input[@type='file' and contains(@accept, '.csv')]", filePath);
        verifyToastMessage("Bulk Session Creation CSV Uploaded Successfully");
        page.reload();
        return manageSessionPage;
    }

    public AppManageSessionPage uploadEditedCsv() {
        this.validPage();
        String currentWorkingDirectory = System.getProperty("user.dir");
        Path filePath = Paths.get(currentWorkingDirectory, "target", "bulksession_files", "session_edit.csv");
        logger.info("File Path: " + filePath.toString());
        // Set the file to upload on the input[type="file"]
        page.setInputFiles("//input[@type='file' and contains(@accept, '.csv')]", filePath);
        verifyToastMessage("Bulk Session Creation CSV Uploaded Successfully");
        page.reload();
        return manageSessionPage;
    }

    public AppManageSessionPage uploadDeleteCsv() {
        this.validPage();
        String currentWorkingDirectory = System.getProperty("user.dir");
        Path filePath = Paths.get(currentWorkingDirectory, "target", "bulksession_files", "session_delete.csv");
        logger.info("File Path: " + filePath.toString());
        // Set the file to upload on the input[type="file"]
        page.setInputFiles("//input[@type='file']", filePath);
        verifyToastMessage("Bulk Session Creation CSV Uploaded Successfully");
        page.reload();
        return manageSessionPage;
    }
}


