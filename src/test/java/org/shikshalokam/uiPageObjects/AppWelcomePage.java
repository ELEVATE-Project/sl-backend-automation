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

    public AppWelcomePage backToWorkSpace() {
        this.validPage();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("back")).click();
        return welcomePage;
    }

    public AppWelcomePage workspace() {
        this.validPage();
        if (PWBasePage.PWBrowser == PWBrowser.chromePixel4a || PWBasePage.PWBrowser == PWBrowser.msedgePixel4a) {
            logger.info(" Pixel 4a interface detected, clicking on menu button.");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("menu")).click();
        }
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
        if (PWBasePage.PWBrowser == PWBrowser.chromePixel4a || PWBasePage.PWBrowser == PWBrowser.msedgePixel4a) {
            logger.info(" Pixel 4a interface detected, clicking on menu button.");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("menu")).click();
        }
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

    public AppWelcomePage enrolledSession() {
        this.validPage();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Enrolled sessions")).click();
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
        page.waitForTimeout(1000);
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

    public AppWelcomePage verifyTags(String session, String tags) {
        this.validPage();
        String sessionAndTag = String.format("//div/h5[text()='%s']/../../../../..//span[text()=' %s ']", session, tags);
        page.locator(sessionAndTag).isVisible();
        String tag = page.locator(sessionAndTag).textContent();
        logger.info(tag);
        return welcomePage;

    }

    public AppWelcomePage selectSessionFromList(String session) {
        this.validPage();
        page.getByRole(AriaRole.CELL, new Page.GetByRoleOptions().setName(session)).locator("div").click();
        //page.getByText(session).click();
        return welcomePage;
    }

    public AppWelcomePage profile() {
        this.validPage();
        if (PWBasePage.PWBrowser == PWBrowser.chromePixel4a || PWBasePage.PWBrowser == PWBrowser.msedgePixel4a) {
            if (!page.locator("div").filter(new Locator.FilterOptions().setHasText("Logout")).isVisible()) {
                page.locator("ion-tab-button").filter(new Locator.FilterOptions().setHasText("Profile")).click();
            }
        }
        else {
            page.getByRole(AriaRole.NAVIGATION).getByText("Profile").click();
        }
        return welcomePage;
    }

    public AppWelcomePage viewRoles() {
        this.validPage();
        if (PWBasePage.PWBrowser == PWBrowser.chromePixel4a || PWBasePage.PWBrowser == PWBrowser.msedgePixel4a) {
            logger.info(" Pixel 4a interface detected, clicking on menu button.");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("menu")).click();
        }
        page.getByRole(AriaRole.NAVIGATION).getByText("View roles").click();
        return welcomePage;
    }

    public AppWelcomePage mentors() {
        this.validPage();
        if (PWBasePage.PWBrowser == PWBrowser.chromePixel4a || PWBasePage.PWBrowser == PWBrowser.msedgePixel4a) {
            page.locator("ion-tab-button").filter(new Locator.FilterOptions().setHasText("Mentors")).click();
        }
        else {
            page.locator("ion-item").filter(new Locator.FilterOptions().setHasText("Mentors")).click();
        }
        return welcomePage;
    }

    public AppWelcomePage verifyRoles( String... expectedRoles) {
        this.validPage();
        page.getByText("Roles", new Page.GetByTextOptions().setExact(true)).isVisible();
        for (String expectedRole : expectedRoles) {
            page.locator("//ion-item[contains(text(),'" + expectedRole + "')]").isVisible();
            logger.info("Role '" + expectedRole + "' is on the Role list.");
        }
        page.locator("//ion-label[text()='Close']").click();
            return welcomePage;
    }
}