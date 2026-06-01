package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.PWBasePage;

public class AppSubmitObservationWithOutRubricDeeplink extends PWBasePage {
    AppSubmitObservationWithOutRubricDeeplink observationWithOutRubricDeeplink;
    private static final Logger logger = LogManager.getLogger(AppHomePage.class);

    public AppSubmitObservationWithOutRubricDeeplink(String givenTitleName) {
        super(givenTitleName);
        this.observationWithOutRubricDeeplink = this;
    }

    public AppSubmitObservationWithOutRubricDeeplink submitObservationWithOutRubricDeeplink() {
        logger.info("Submitting a observation WithOut Rubric via deeplink started.");
        page.navigate(fetchProperty("ep.observationWithOutRubricDeeplink"));
        page.waitForLoadState();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Start")).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("BAGAFA ASRAM H.S SCHOOL")).click();
        accessingObservationWithOUTRubric();
        logger.info("Submitting a observation WithOut Rubric via deeplink ended.");
        return observationWithOutRubricDeeplink;
    }
}