package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.PWBasePage;
import org.testng.Assert;

public class AppObservationLEDIMPFlowPage extends PWBasePage {
    private static final Logger logger = LogManager.getLogger(AppObservationLEDIMPFlowPage.class);

    public AppObservationLEDIMPFlowPage(String givenTitleName) {
        super(givenTitleName);
    }

    public AppObservationLEDIMPFlowPage checkProjectIsVisible(String projectNameLEDIMP, boolean expectedStatus) {

        logger.info("Checking project visibility started.");
        page.getByPlaceholder("Search your project here").click();
        page.getByPlaceholder("Search your project here").fill(projectNameLEDIMP);
        boolean actualProjectVisibility =
                page.getByText(projectNameLEDIMP,
                                new Page.GetByTextOptions().setExact(true))
                        .isVisible();

        Assert.assertEquals(
                actualProjectVisibility,
                expectedStatus,
                "Project visibility status does not match the expected value."
        );
        page.getByRole(AriaRole.BANNER).locator("svg").click();
        logger.info("Checking project visibility ended.");
        return this;
    }

    public AppObservationLEDIMPFlowPage submitObservationLEDIMP(String ObservationLEDIMPName) {
        logger.info("Submitting an Observation LED IMP (from Tile) started.");
        page.getByPlaceholder("Search your observation here").click();
        page.getByPlaceholder("Search your observation here").fill(ObservationLEDIMPName);
        page.getByText(ObservationLEDIMPName).click();
        page.locator("mat-card-content").first().click();
        accessingObservationWithRubric();
        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        page.getByText("Completed").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Reports")).first().click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Click here to improve")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("criteria 1 Level")).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("PROJECT WITHOUT CERTIFICATE")).click();
        accessProject();
        page.getByRole(AriaRole.BANNER).locator("svg").click();
        page.locator("button").click();
        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        page.getByRole(AriaRole.BUTTON).click();

        logger.info("Submitting an Observation LED IMP (from Tile) ended.");
        return this;
    }

}
