package org.shikshalokam.uiPageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppMentorsPage extends PWBasePage {
    private AppMentorsPage mentorsPage;
    private static final Logger logger = LogManager.getLogger(AppMentorsPage.class);

    public AppMentorsPage(String givenTitleName) {
        super(givenTitleName);
        this.mentorsPage = this;
    }

    public AppMentorsPage searchMentor(String mentor) {
        this.validPage();
        page.getByPlaceholder("Search for mentors").first().click();
        page.waitForTimeout(1000);
        page.getByRole(AriaRole.SEARCHBOX, new Page.GetByRoleOptions().setName("search text")).fill(mentor);
        page.getByRole(AriaRole.SEARCHBOX, new Page.GetByRoleOptions().setName("search text")).press("Enter");
        page.waitForTimeout(1000);
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName(mentor)).click();
        return mentorsPage;
    }


    public AppMentorsPage viewRoles(){
        this.validPage();
        page.locator("app-mentor-details").getByText("View roles").click();
        return mentorsPage;
    }
}
