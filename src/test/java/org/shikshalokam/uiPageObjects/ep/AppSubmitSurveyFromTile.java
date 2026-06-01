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
        page.locator("(//button[@mat-ripple-loader-class-name=\"mat-mdc-button-ripple\"])[1]").click();
        logger.info("Submitting a Survey (from Tile) ended.");
        return this;
    }
}
