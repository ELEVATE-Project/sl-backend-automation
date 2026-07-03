package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.PWBasePage;

public class AppSubmitSurveyFromProgram extends PWBasePage {
    private static final Logger logger = LogManager.getLogger(AppSubmitSurveyFromProgram.class);

    public AppSubmitSurveyFromProgram(String givenTitleName) {
        super(givenTitleName);
    }

    public AppSubmitSurveyFromProgram submitSurveyFromProgram(String surveyName) {
        logger.info("Submitting a Survey (from Program) started.");
        page.locator("ion-title").filter(new Locator.FilterOptions().setHasText("surveys")).locator("div").click();
        page.getByText(surveyName, new Page.GetByTextOptions().setExact(true)).click();
        accessingSurvey();
        page.getByRole(AriaRole.BANNER).locator("path").click();
        logger.info("Submitting a Survey (from Program) ended.");
        return this;
    }
}

