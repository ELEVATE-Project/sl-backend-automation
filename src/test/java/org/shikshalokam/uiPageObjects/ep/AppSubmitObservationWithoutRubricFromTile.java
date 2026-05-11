package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.PWBasePage;

public class AppSubmitObservationWithoutRubricFromTile extends PWBasePage {
    private static final Logger logger = LogManager.getLogger(AppSubmitObservationWithoutRubricFromTile.class);

    public AppSubmitObservationWithoutRubricFromTile(String givenTitleName) {
        super(givenTitleName);
    }

    public AppSubmitObservationWithoutRubricFromTile submitObservationWithoutRubricFromTile(String ObservationWithOutRubricName) {
        logger.info("Submitting an Observation Without Rubric (from Tile) started.");
        page.getByPlaceholder("Search your observation here").click();
        page.getByPlaceholder("Search your observation here").fill(ObservationWithOutRubricName);
        page.getByText(ObservationWithOutRubricName, new Page.GetByTextOptions().setExact(true)).click();
        page.locator("mat-card-content").first().click();
        accessingObservationWithOUTRubric();
        logger.info("Submitting an Observation Without Rubric (from Tile) ended.");
        return this;
    }
    public AppSubmitObservationWithoutRubricFromTile submitObservationWithoutRubricFromTileObserveAgain(String ObservationWithOutRubricName, int observationCount) {
        logger.info("Submitting an Observation Without Rubric (from Tile) Observe again started.");
        page.getByPlaceholder("Search your observation here").click();
        page.getByPlaceholder("Search your observation here").fill(ObservationWithOutRubricName);
        page.getByText(ObservationWithOutRubricName, new Page.GetByTextOptions().setExact(true)).click();
        page.locator("mat-card-content").first().click();
        observeAgain(observationCount,false);
        logger.info("Submitting an Observation Without Rubric (from Tile) Observe again ended.");
        return this;
    }
    public AppSubmitObservationWithoutRubricFromTile submitObservationWithoutRubricFromTileAddEntity(String ObservationWithOutRubricName, String addEntityName) {
        logger.info("Submitting an Observation Without Rubric (from Tile) Add Entity started.");
        page.getByPlaceholder("Search your observation here").click();
        page.getByPlaceholder("Search your observation here").fill(ObservationWithOutRubricName);
        page.getByText(ObservationWithOutRubricName, new Page.GetByTextOptions().setExact(true)).click();
        addEntity(addEntityName,false);
        logger.info("Submitting an Observation Without Rubric (from Tile) Add Entity ended.");
        return this;
    }
}

