package org.shikshalokam.uiPageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class AppLoginPage extends PWBasePage {

    private AppLoginPage loginPage;
    private static final Logger logger = LogManager.getLogger(AppLoginPage.class);

    public AppLoginPage(String givenTitleName) {
        super(givenTitleName);
        this.loginPage = this;
    }

    public AppLoginPage loginToApp(String userName, String password) {
        this.validPage();
        Locator loginbutton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("mail outline Login"));
        page.waitForTimeout(3000);
        if (loginbutton.isVisible()) {
            loginbutton.click();
            logger.info("Login Button clicked.");
        } else {
            logger.info("Login Button not found, skipping click.");
        }
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email *")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Email *")).fill(userName);
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password *")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Password *")).fill(password);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();

        return loginPage;
    }


    public AppLoginPage signUp() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("mail outline Sign up")).click();
        return loginPage;
    }

    public AppLoginPage userForgetsPassword() {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("mail outline Login")).click();
        page.locator("div").filter(new Locator.FilterOptions().setHasText("Forgot password?")).nth(2).click();
        return loginPage;
    }

    public AppLoginPage logOutFromApp() {
        if (PWBasePage.PWBrowser == PWBrowser.chromePixel4a || PWBasePage.PWBrowser == PWBrowser.msedgePixel4a) {
            logger.info(" Pixel 4a interface detected, clicking on menu button.");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("menu")).click();
        }
        page.locator("div").filter(new Locator.FilterOptions().setHasText("Logout")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Logout")).click();
        return loginPage;
    }
}

