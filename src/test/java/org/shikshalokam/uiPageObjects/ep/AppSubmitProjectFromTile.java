package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.PWBasePage;

public class AppSubmitProjectFromTile extends PWBasePage {
    private static final Logger logger = LogManager.getLogger(AppSubmitProjectFromTile.class);

    public AppSubmitProjectFromTile(String givenTitleName) {
        super(givenTitleName);
    }

    public AppSubmitProjectFromTile submitProjectFromTile(String projectName) {
        logger.info("Submitting a Project (from Tile) started.");
        page.getByPlaceholder("Search your project here").click();
        page.getByPlaceholder("Search your project here").fill(projectName);
        page.getByText(projectName, new Page.GetByTextOptions().setExact(true)).first().click();
        accessProject();
        page.getByRole(AriaRole.BANNER).locator("svg").click();
        logger.info("Submitting a Project (from Tile) ended.");
        return this;
    }
}

