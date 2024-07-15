package org.shikshalokam.uiPageObjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class AppBecomeMentorPage extends PWBasePage {
    private AppBecomeMentorPage mentorPage;
    private static final Logger logger = LogManager.getLogger(AppBecomeMentorPage.class);

    public AppBecomeMentorPage(String givenTitleName) {
        super(givenTitleName);
        this.mentorPage = this;
    }

    public AppBecomeMentorPage becomeMentor() {
        this.validPage();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Become a Mentor")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Request to be a Mentor")).click();
        return mentorPage;
    }

    public AppBecomeMentorPage acceptAsMentor() {
        this.validPage();
        page.getByText("Workspace").click();
        page.locator("ion-card-content").filter(new Locator.FilterOptions().setHasText("Manage user")).click();
        page.locator("//td[text()='UserSignup']/..//td[4]//ion-button[1]").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes")).click();
        return mentorPage;
    }

    public AppBecomeMentorPage requestToBeAMentor() {
        this.validPage();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Request to be a Mentor")).click();
        verifyToastMessage("Admin has received your request. Further information will be received through Email.");
        return mentorPage;

    }
}

	
	
