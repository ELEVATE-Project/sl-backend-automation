package org.shikshalokam.uiPageObjects;

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

    public AppManageSessionPage createSessionsTab() {
        this.validPage();
        page.locator("ion-segment-button").filter(new Locator.FilterOptions().setHasText("Create sessions")).click();
        return manageSessionPage;
    }
    public AppManageSessionPage verifyCreatedSession(String session) {
        this.validPage();
        String createdsession = page.getByRole(AriaRole.CELL, new Page.GetByRoleOptions().setName(String.format(" %s ", session)))
                .locator("div").textContent();
        page.locator(createdsession).isVisible();
        logger.info(createdsession +" Session created Successfully");
        return manageSessionPage;
    }
    public AppManageSessionPage uploadSessionCreationCsv() {
        this.validPage();
        String currentWorkingDirectory = System.getProperty("user.dir");
        Path filePath = Paths.get(currentWorkingDirectory, "target","bulksession_files", "bulk_session_creation.csv");
        logger.info("File Path: " + filePath.toString());
        // Set the file to upload on the input[type="file"]
        page.setInputFiles("//input[@type='file']", filePath);
        verifyToastMessage("Bulk Session Creation CSV Uploaded Successfully");
        page.reload();
        return manageSessionPage;
    }
}