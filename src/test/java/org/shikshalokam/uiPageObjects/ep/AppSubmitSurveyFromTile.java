package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.PWBasePage;

public class AppSubmitSurveyFromTile extends PWBasePage {
    private static final Logger logger = LogManager.getLogger(AppSubmitSurveyFromTile.class);

    public AppSubmitSurveyFromTile(String givenTitleName) {
        super(givenTitleName);
    }

    public AppSubmitSurveyFromTile submitSurveyFromTile(String surveyName) {
        logger.info("Submitting a Survey (from Tile) started.");
        page.getByPlaceholder("Search your survey here").click();
        page.getByPlaceholder("Search your survey here").fill(surveyName);
        page.getByText(surveyName, new Page.GetByTextOptions().setExact(true)).click();
        accessingSurvey();
        // Wait up to 30 seconds for the success message to appear (regex for robustness)
        Locator successMsg = page.getByText("Your survey has been submitted successfully.");
        successMsg.waitFor(new Locator.WaitForOptions().setTimeout(30000).setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));
        if (!successMsg.isVisible(new Locator.IsVisibleOptions().setTimeout(5000))) {
            logger.error("Success message not visible after waiting.");
            throw new AssertionError("Survey submission success message not visible");
        }
        page.locator("(//button[@mat-ripple-loader-class-name=\"mat-mdc-button-ripple\"])[1]").click();
        logger.info("Submitting a Survey (from Tile) ended.");
        return this;
    }
}
