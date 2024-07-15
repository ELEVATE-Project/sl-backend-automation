package org.shikshalokam.uiPageObjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;


public class AppWelcomePage extends PWBasePage {

    private AppWelcomePage welcomePage;
    private static final Logger logger = LogManager.getLogger(AppWelcomePage.class);

    public AppWelcomePage(String givenTitleName) {
        super(givenTitleName);
        this.welcomePage = this;
    }

    public AppWelcomePage becomeMentor() {
        this.validPage();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Become a Mentor")).click();
        return welcomePage;
    }

    public AppWelcomePage workspace() {
        this.validPage();
        page.getByText("Workspace").click();
        return welcomePage;
    }

    public AppWelcomePage verifiedUserAsaMentor() {
        this.validPage();
        page.getByRole(AriaRole.NAVIGATION).getByText("Mentor", new Locator.GetByTextOptions()).isVisible();
        return welcomePage;
    }

    public AppWelcomePage logOutFromApp() {
        this.validPage();
        page.locator("div").filter(new Locator.FilterOptions().setHasText("Logout")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Logout")).click();
        return welcomePage;
    }

    public AppWelcomePage continueToSetupProfile() {
        this.validPage();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue")).click();
        return welcomePage;
    }

    public AppWelcomePage requestToChangePassword() {
        this.validPage();
        page.getByText("Change password").click();
        return welcomePage;
    }

    public AppWelcomePage myMentoringSessionTab() {
        this.validPage();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("My mentoring sessions")).click();
        return welcomePage;
    }

    public AppWelcomePage createSession() {
        this.validPage();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Create session add circle")).click();
        return welcomePage;
    }

    public AppWelcomePage sessionSearch(String session) {
        this.validPage();
        page.getByPlaceholder("Search for sessions").fill(session);
        page.getByPlaceholder("Search for sessions").press("Enter");
        page.waitForTimeout(3000);
        Locator noSessionsAvailableMessage = page.locator("//div[@class='title' and text()=' No sessions available']");
        if (noSessionsAvailableMessage.isVisible()) {
            page.reload();
            logger.info("Page reloaded because 'No sessions available' Message showed up.");
        } else {
            logger.info("No need to reload the page as searched Session is available");
        }
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName(session)).click();
        return welcomePage;
    }

    public AppWelcomePage selectcreatedSession(String session) {
        this.validPage();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName(session)).click();
        return welcomePage;
    }
}
