package org.shikshalokam.uiPageObjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class AppWorkspacePage extends PWBasePage {

    private AppWorkspacePage workspacePage;
    private static final Logger logger = LogManager.getLogger(AppWorkspacePage.class);

    public AppWorkspacePage(String givenTitleName) {
        super(givenTitleName);
        this.workspacePage = this;
    }

    public AppWorkspacePage acceptAsMentor() {
        this.validPage();
        page.waitForTimeout(5000);
        page.locator("ion-card-content").filter(new Locator.FilterOptions().setHasText("Manage user")).click();
        page.locator("//td[text()='UserSignup']/..//td[4]//ion-button[1]").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes")).click();
        verifyToastMessage("Organisation request approved successfully");
        return workspacePage;
    }

    public AppWorkspacePage manageSession() {
        this.validPage();
        page.locator("ion-card-content").filter(new Locator.FilterOptions().setHasText("Manage session")).click();
        return workspacePage;
    }
}
