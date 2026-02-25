package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.PWBasePage;

public class AppSubmitObservationWithoutRubricFromProgram extends PWBasePage {
    private static final Logger logger = LogManager.getLogger(AppSubmitObservationWithoutRubricFromProgram.class);

    public AppSubmitObservationWithoutRubricFromProgram(String givenTitleName) {
        super(givenTitleName);
    }

    public AppSubmitObservationWithoutRubricFromProgram submitObservationWithoutRubricFromProgram(String ObservationWithOutRubricName) {
        logger.info("Submitting an Observation Without Rubric (from Program) started.");
        page.locator("ion-title").filter(new Locator.FilterOptions().setHasText("observations")).locator("div").click();
        page.getByText(ObservationWithOutRubricName, new Page.GetByTextOptions().setExact(true)).click();
        page.locator("mat-card-content").first().click();
        accessingObservationWithOUTRubric();
        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        page.getByRole(AriaRole.BANNER).locator("path").click();
        page.getByRole(AriaRole.BANNER).locator("svg").click();
        logger.info("Submitting an Observation Without Rubric (from Program) ended.");
        return this;
    }
    public AppSubmitObservationWithoutRubricFromProgram submitObservationWithoutRubricFromProgramAgain(String ObservationWithRubricName,int observationCount) {
        logger.info("Submitting an Observation With Rubric (from Program) Again started.");
        page.locator("ion-title").filter(new Locator.FilterOptions().setHasText("observations")).locator("div").click();
        page.getByText(ObservationWithRubricName, new Page.GetByTextOptions().setExact(true)).click();
        page.locator("mat-card-content").first().click();
        observeAgain(observationCount,false);
        page.locator("//button[contains(@class,'back-button mdc-icon-button mat-mdc-icon-button mat-unthemed mat-mdc-button-base')]").click();
        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        page.getByRole(AriaRole.BANNER).locator("svg").click();
        logger.info("Submitting an Observation With Rubric (from Program) Again ended.");
        return this;
    }
    public AppSubmitObservationWithoutRubricFromProgram submitObservationWithoutRubricFromProgramAddEntity(String ObservationWithRubricName, String addEntityName) {
        logger.info("Submitting an Observation With Rubric (from Program) Add Entity started.");
        page.locator("ion-title").filter(new Locator.FilterOptions().setHasText("observations")).locator("div").click();
        page.getByText(ObservationWithRubricName, new Page.GetByTextOptions().setExact(true)).click();
        addEntity(addEntityName,false);
        page.locator("//button[contains(@class,'back-button mdc-icon-button mat-mdc-icon-button mat-unthemed mat-mdc-button-base')]").click();
        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        page.getByRole(AriaRole.BANNER).locator("svg").click();
        logger.info("Submitting an Observation With Rubric (from Program) Add Entity ended.");
        return this;
    }
}

