package org.shikshalokam.uiPageObjects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;

public class AppLoginPage extends PWBasePage {

    private AppLoginPage loginPage;
    private static final Logger logger = LogManager.getLogger(AppLoginPage.class);
    public AppLoginPage(String givenTitleName) {
        super(givenTitleName);
        this.loginPage=this;
    }

    public AppLoginPage  loginToApp(String userName, String password){


        this.validPage();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("mail outline Login")).click();
        page.getByLabel("Email *").dblclick();
        page.getByLabel("Email *").fill(userName);
        page.getByLabel("Password *").click();
        page.getByLabel("Password *").fill(password);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
        //page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();

        return loginPage;
    }

    public AppLoginPage  logOutFromApp()
    {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Logout")).click();
        return loginPage;
    }



}
