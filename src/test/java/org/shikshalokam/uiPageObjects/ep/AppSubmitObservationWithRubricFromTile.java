package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.PWBasePage;

import java.util.regex.Pattern;

public class AppSubmitObservationWithRubricFromTile extends PWBasePage {
    private static final Logger logger = LogManager.getLogger(AppSubmitObservationWithRubricFromTile.class);

    public AppSubmitObservationWithRubricFromTile(String givenTitleName) {
        super(givenTitleName);
    }

    public AppSubmitObservationWithRubricFromTile submitObservationWithRubricFromTile(String ObservationWithRubricName) {
        logger.info("Submitting an Observation With Rubric (from Tile) started.");
        page.getByPlaceholder("Search your observation here").click();
        page.getByPlaceholder("Search your observation here").fill(ObservationWithRubricName);
        page.getByText(ObservationWithRubricName).click();
        page.locator("mat-card-content").first().click();
        accessingObservationWithRubric();
        page.locator("//button[contains(@class,'back-button mdc-icon-button mat-mdc-icon-button mat-unthemed mat-mdc-button-base')]").click();
        page.locator("//button[contains(@class,'back-button mdc-icon-button mat-mdc-icon-button mat-unthemed mat-mdc-button-base')]").click();
        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        logger.info("Submitting an Observation With Rubric (from Tile) ended.");
        return this;
    }
    public AppSubmitObservationWithRubricFromTile submitObservationWithRubricFromTileAgain(String ObservationWithRubricName,int observationCount) {
        logger.info("Submitting an Observation With Rubric (from Program) Again started.");
        page.getByPlaceholder("Search your observation here").click();
        page.getByPlaceholder("Search your observation here").fill(ObservationWithRubricName);
        page.getByText(ObservationWithRubricName, new Page.GetByTextOptions().setExact(true)).click();
        page.locator("mat-card-content").first().click();
        observeAgain(observationCount,true);
        page.locator("//button[contains(@class,'back-button mdc-icon-button mat-mdc-icon-button mat-unthemed mat-mdc-button-base')]").click();
        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        logger.info("Submitting an Observation With Rubric (from Program) Again ended.");
        return this;
    }
    public AppSubmitObservationWithRubricFromTile submitObservationWithRubricFromTileAddEntity(String ObservationWithRubricName, String addEntityName) {
        logger.info("Submitting an Observation With Rubric (from Program) started.");
        page.getByPlaceholder("Search your observation here").click();
        page.getByPlaceholder("Search your observation here").fill(ObservationWithRubricName);
        page.getByText(ObservationWithRubricName, new Page.GetByTextOptions().setExact(true)).click();
        addEntity(addEntityName, true);
        page.locator("//button[contains(@class,'back-button mdc-icon-button mat-mdc-icon-button mat-unthemed mat-mdc-button-base')]").click();
        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        logger.info("Submitting an Observation With Rubric (from Program) ended.");
        return this;
    }
}

