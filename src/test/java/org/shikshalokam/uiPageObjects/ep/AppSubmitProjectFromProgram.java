package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.FileChooser;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.PWBasePage;

import java.nio.file.Path;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AppSubmitProjectFromProgram extends PWBasePage {
    private static final Logger logger = LogManager.getLogger(AppSubmitProjectFromProgram.class);

    public AppSubmitProjectFromProgram(String givenTitleName) {
        super(givenTitleName);
    }

    public AppSubmitProjectFromProgram submitProjectFromProgram(String projectName) {
        logger.info("Submitting a Project (from Program) started.");
        page.locator("ion-title").filter(new Locator.FilterOptions().setHasText("projects")).locator("div").click();
        page.getByText(projectName, new Page.GetByTextOptions().setExact(true)).click();
        accessProject();
        page.getByRole(AriaRole.BANNER).locator("svg").click();
        page.getByRole(AriaRole.BANNER).locator("path").click();
        logger.info("Submitting a Project (from Program) ended.");
        return this;
    }
}
