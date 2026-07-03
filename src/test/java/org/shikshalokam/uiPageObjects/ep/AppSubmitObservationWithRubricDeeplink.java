package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.PWBasePage;
import java.util.regex.Pattern;

public class AppSubmitObservationWithRubricDeeplink extends PWBasePage {
    AppSubmitObservationWithRubricDeeplink observationWithRubricDeeplink;
    private static final Logger logger = LogManager.getLogger(AppHomePage.class);

    public AppSubmitObservationWithRubricDeeplink(String givenTitleName) {
        super(givenTitleName);
        this.observationWithRubricDeeplink = this;
    }

    public AppSubmitObservationWithRubricDeeplink submitObservationWithRubricDeeplink() {
        logger.info("Submitting Observation with rubric via deeplink started.");
        page.navigate(fetchProperty("ep.observationWithRubricDeeplink"));
        page.waitForLoadState();
        page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^0%$"))).nth(1).click();
        page.getByText("Planning & Execution").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Start")).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("BAGAFA ASRAM H.S SCHOOL")).click();
        accessingObservationWithRubric();
        logger.info("Submitting Observation with rubric via deeplink ended.");
        return observationWithRubricDeeplink;
    }
}