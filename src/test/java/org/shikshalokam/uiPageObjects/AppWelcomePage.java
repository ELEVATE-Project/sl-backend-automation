package org.shikshalokam.uiPageObjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class AppWelcomePage extends PWBasePage{
	
	private AppWelcomePage welcomePage;
    private static final Logger logger = LogManager.getLogger(AppWelcomePage.class);
    public AppWelcomePage(String givenTitleName) {
        super(givenTitleName);
        this.welcomePage=this;
    }
    
    public AppWelcomePage becomeMentor()  {
        this.validPage();
	     page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Become a Mentor")).click();
		return welcomePage;
    }
    
    public AppWelcomePage  workspace()  {
		this.validPage();
		 page.getByText("Workspace").click();
		return welcomePage;
    }
    
    public AppWelcomePage verifiedUserAsaMentor() {
        this.validPage();
		 page.getByRole(AriaRole.NAVIGATION).getByText("Mentor", new Locator.GetByTextOptions()).isVisible();
		 return welcomePage;
	}
    
    public AppWelcomePage  logOutFromApp()
    {
        this.validPage();
        page.locator("div").filter(new Locator.FilterOptions().setHasText("Logout")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Logout")).click();
        return welcomePage;
    }

    public AppWelcomePage  updateButton() {
        this.validPage();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Update")).click();
        return welcomePage;
    }
}
