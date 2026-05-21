package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.PWBasePage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AppSubmitProjectDeeplink extends PWBasePage {
    AppSubmitProjectDeeplink projectDeeplink;
    private static final Logger logger = LogManager.getLogger(AppHomePage.class);

    public AppSubmitProjectDeeplink(String givenTitleName) {
        super(givenTitleName);
        this.projectDeeplink = this;
    }

    public AppSubmitProjectDeeplink submitProjectUsingDeeplink() {
        logger.info("Submitting a Project via deeplink started.");
        page.navigate(fetchProperty("ep.projectDeeplink"));
        page.waitForLoadState(); // Ensure page is loaded
        accessProject();
        logger.info("Submitting a Project via deeplink ended.");
        return projectDeeplink;
    }
}