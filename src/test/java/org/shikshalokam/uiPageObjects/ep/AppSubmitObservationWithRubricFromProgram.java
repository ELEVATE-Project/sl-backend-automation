package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.PWBasePage;

import java.util.regex.Pattern;

public class AppSubmitObservationWithRubricFromProgram extends PWBasePage {
    private static final Logger logger = LogManager.getLogger(AppSubmitObservationWithRubricFromProgram.class);

    public AppSubmitObservationWithRubricFromProgram(String givenTitleName) {
        super(givenTitleName);
    }

    public AppSubmitObservationWithRubricFromProgram submitObservationWithRubricFromProgram(String ObservationWithRubricName) {
        logger.info("Submitting an Observation With Rubric (from Program) started.");
        page.locator("ion-title").filter(new Locator.FilterOptions().setHasText("observations")).locator("div").click();
        page.getByText(ObservationWithRubricName, new Page.GetByTextOptions().setExact(true)).click();
        page.locator("mat-card-content").filter(new Locator.FilterOptions().setHasText("BAGAFA ASRAM H.S SCHOOL")).click();
        accessingObservationWithRubric();
        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();

        Locator btn = page.locator("button.back-button");
        btn.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
        btn.click();

        Locator btn2 = page.locator("//mat-icon[contains(text(),'arrow_back')]");
        btn2.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
        btn2.click();
        logger.info("Submitting an Observation With Rubric (from Program) ended.");
        return this;
    }
    public AppSubmitObservationWithRubricFromProgram submitObservationWithRubricFromProgramAgain(String ObservationWithRubricName,int observationCount) {
        logger.info("Submitting an Observation With Rubric (from Program) Again started.");
        page.locator("ion-title").filter(new Locator.FilterOptions().setHasText("observations")).locator("div").click();
        page.getByText(ObservationWithRubricName, new Page.GetByTextOptions().setExact(true)).click();
        page.locator("mat-card-content").filter(new Locator.FilterOptions().setHasText("BAGAFA ASRAM H.S SCHOOL")).click();
        observeAgain(observationCount, true);
        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();

        Locator btn = page.locator("button.back-button");
        btn.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
        btn.click();

        Locator btn2 = page.locator("//mat-icon[contains(text(),'arrow_back')]");
        btn2.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
        btn2.click();
        logger.info("Submitting an Observation With Rubric (from Program) Again ended.");
        return this;
    }
    public AppSubmitObservationWithRubricFromProgram submitObservationWithRubricFromProgramAddEntity(String ObservationWithRubricName, String addEntityName) {
        logger.info("Submitting an Observation With Rubric (from Program) started.");
        page.locator("ion-title").filter(new Locator.FilterOptions().setHasText("observations")).locator("div").click();
        page.getByText(ObservationWithRubricName, new Page.GetByTextOptions().setExact(true)).click();
        addEntity(addEntityName, true);
        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();

        Locator btn = page.locator("button.back-button");
        btn.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
        btn.click();

        Locator btn2 = page.locator("//mat-icon[contains(text(),'arrow_back')]");
        btn2.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));
        btn2.click();

        logger.info("Submitting an Observation With Rubric (from Program) ended.");
        return this;
    }
}

