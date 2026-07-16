package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.PWBasePage;

public class AppObservationAsTask extends PWBasePage {
    private AppObservationAsTask observationAsTask;

    private static final Logger logger = LogManager.getLogger(AppObservationAsTask.class);

    public AppObservationAsTask(String givenTitleName) {
        super(givenTitleName);
        this.observationAsTask = this;
    }

    public AppObservationAsTask submitObservationAsTask() throws InterruptedException {
        logger.info("Accessing Observation as Task started");
        selectProgramName(fetchProperty("ep.programName"));
        page.locator("ion-card-content").filter(new Locator.FilterOptions().setHasText("projects")).click();

        String projectName = fetchProperty("ep.ObservationAsTaskName")
                .replace("â", "–");
        page.getByText(projectName, new Page.GetByTextOptions()).click();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Start Improvement")).click();
        page.getByText("Task details").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Start observation")).first().click();
        // Observation without Rubric
        accessingObservationWithOUTRubric();
//        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        page.locator("button:not([disabled])")
                .filter(new Locator.FilterOptions().setHasText("arrow_back"))
                .click();
        page.getByText("Task details").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Start observation")).first().click();
        // Observation with Rubric
        accessingObservationWithRubric();
        Thread.sleep(2000);
        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        page.getByText("Task details").click();

        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("3. Teachers should implement")).click();
        page.getByText("Not Started").click();
        page.getByText("Completed").click();
        page.getByRole(AriaRole.BANNER).locator("path").click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("4. Teachers should implement")).click();
        page.getByText("Not Started").click();
        page.getByText("Completed").click();
        page.getByRole(AriaRole.BANNER).locator("path").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Load more")).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("5. Teachers should upload")).click();
        page.getByText("Not Started").click();
        page.getByText("Completed").click();
        page.getByRole(AriaRole.BANNER).locator("path").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit Improvement")).click();
        logger.info("Accessing Observation as Task ended");
        return observationAsTask;
    }
}
