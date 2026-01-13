package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.FileChooser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.PWBasePage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AppSubmitProjectFromProgram extends PWBasePage {
    private static final Logger logger = LogManager.getLogger(AppSubmitProjectFromProgram.class);

    public AppSubmitProjectFromProgram(String givenTitleName) {
        super(givenTitleName);
    }

    public AppSubmitProjectFromProgram selectProgramName(String selectProgramName) {
        logger.info("Selecting Program name started");
        page.getByText("Programs", new Page.GetByTextOptions().setExact(true)).click();
        page.getByPlaceholder("Search your program here").click();
        page.getByPlaceholder("Search your program here").fill(selectProgramName);
        page.getByText(selectProgramName).click();
        logger.info("Selecting Program name ended");
        return this;
    }

    public AppSubmitProjectFromProgram submitProjectFromProgram(String projectName) {
        logger.info("Submitting a Project (from Program) started.");
        page.locator("ion-title").filter(new Locator.FilterOptions().setHasText("projects")).locator("div").click();
        page.getByText(projectName, new Page.GetByTextOptions().setExact(true)).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Start Improvement")).click();
        page.getByText("Task details").click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("1. Teachers should understand")).click();
        page.locator("#mat-select-value-1").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Completed")).click();
        page.getByRole(AriaRole.BANNER).locator("path").click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("2. Teachers should implement")).click();
        page.getByPlaceholder("Add subtask name").click();
        page.getByPlaceholder("Add subtask name").fill("Sub task 1");
        page.getByPlaceholder("Add subtask name").press("ArrowLeft");
        page.getByPlaceholder("Add subtask name").press("ArrowRight");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add subtask")).click();
        page.locator(".sub-task-body > div:nth-child(2) > .mat-mdc-form-field > .mat-mdc-text-field-wrapper > .mat-mdc-form-field-flex > .mat-mdc-form-field-infix").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Completed")).click();

        attachEvidenceAtTaskLevel();

        page.getByRole(AriaRole.BANNER).locator("path").click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("3. Teachers should implement")).click();
        page.getByText("Not Started").click();
        page.getByText("Completed").click();
        page.getByRole(AriaRole.BANNER).locator("path").click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("4. Teachers should implement")).click();
        page.getByText("Not Started").click();
        page.getByText("Completed").click();
        attachEvidenceAtTaskLevel();
        page.getByRole(AriaRole.BANNER).locator("path").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Load more")).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("5. Teachers should upload")).click();
        page.getByText("Not Started").click();
        page.getByText("Completed").click();
        page.getByRole(AriaRole.BANNER).locator("path").click();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit Improvement")).click();
        Path png = evidencePath("PNG_Evidence.png");
        Path mp4 = evidencePath("MP4_Evidence.mp4");
        Path pdf = evidencePath("PDF_Evidence.pdf");
        uploadProjectLevelEvidence(png, "Images");
        uploadProjectLevelEvidence(mp4, "Videos");
        uploadProjectLevelEvidence(pdf, "Files");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit Improvement")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        page.getByText("You have submitted your project successfully").isVisible();

        page.getByRole(AriaRole.BANNER).locator("svg").click();
        page.getByRole(AriaRole.BANNER).locator("path").click();

        logger.info("Submitting a Project (from Program) ended.");
        return this;
    }

    public AppSubmitProjectFromProgram attachEvidenceAtTaskLevel() {
        logger.info("Attaching Evidence for Project Started.");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add files")).click();
        page.getByLabel("", new Page.GetByLabelOptions().setExact(true)).check();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Upload").setExact(true)).click();

        Path png = evidencePath("PNG_Evidence.png");
        Path pdf = evidencePath("PDF_Evidence.pdf");
        Path mp4 = evidencePath("MP4_Evidence.mp4");

        uploadTaskLevelEvidence(png, "Images");
        uploadTaskLevelEvidence(mp4, "Videos");
        uploadTaskLevelEvidence(pdf, "Files");
        page.locator("mat-card").filter(new Locator.FilterOptions().setHasText("Links")).click();
        page.getByPlaceholder("Add link here").click();
        page.getByPlaceholder("Add link here").fill(fetchProperty("ep.url"));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Attach files")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add files")).click();
        page.getByLabel("", new Page.GetByLabelOptions().setExact(true)).check();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Upload").setExact(true)).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Attach files")).click();
        logger.info("Attaching Evidence for Project ended.");
        return this;
    }

    public Path evidencePath(String fileName) {
        return Paths.get(
                System.getProperty("user.dir"),
                "src", "main", "resources", "AllEvidence", fileName
        );
    }

    public void uploadTaskLevelEvidence(Path filePath, String fileType) {
        FileChooser fileChooser = page.waitForFileChooser(() -> {
            page.locator("mat-card").filter(new Locator.FilterOptions().setHasText(fileType)).click();
        });
        fileChooser.setFiles(filePath);
        page.getByText("Attached Successfully").isVisible();
    }

    public void uploadProjectLevelEvidence(Path filePaths, String fileType) {
        FileChooser fileChooser = page.waitForFileChooser(() -> {
            page.locator("mat-card").filter(new Locator.FilterOptions().setHasText(fileType)).click();
            page.getByLabel("", new Page.GetByLabelOptions().setExact(true)).check();
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Upload").setExact(true)).click();
        });
        fileChooser.setFiles(filePaths);
        page.getByText("Attached Successfully").isVisible();
    }


}
