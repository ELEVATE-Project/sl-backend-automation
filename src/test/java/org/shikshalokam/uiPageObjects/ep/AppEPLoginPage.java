package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;
import org.shikshalokam.uiPageObjects.AppLoginPage;
import org.shikshalokam.uiPageObjects.PWBasePage;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.Assert;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.shikshalokam.backend.TestCRUDUserPermissions.logger;

public class AppEPLoginPage extends PWBasePage {
    AppEPLoginPage login;
    private static final Logger logger = LogManager.getLogger(AppEPLoginPage.class);

    public AppEPLoginPage(String givenTitleName) {
        super(givenTitleName);
        this.login = this;
    }

    public void openURL() {
        PWBasePage.reInitializePage();
        PWBasePage.page.navigate(PropertyLoader.PROP_LIST.getProperty("ep.url"));
    }

    public AppEPLoginPage logIntoPortal() {
        logger.info("login with email started");
        String userName = fetchProperty("ep.mail");
        String password = fetchProperty("ep.password");
        page.locator("//input[@name='login-username']").click();
        page.locator("//input[@name='login-username']").fill(userName);
        page.locator("//input[@name='login-password']").click();
        page.locator("//input[@name='login-password']").fill(password);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
        logger.info("login with email started ended");
        return login;
    }

    public AppEPLoginPage testLoginWithMultipleOptions(String userName,String password,String loggerMessage) {
        logger.info("login with "+loggerMessage+" started");
        page.locator("//input[@name='login-username']").click();
        page.locator("//input[@name='login-username']").fill(userName);
        page.locator("//input[@name='login-password']").click();
        page.locator("//input[@name='login-password']").fill(password);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
        if(loggerMessage.equals("Negative test"))
        {
            assertThat(page.getByText("Welcome")).not().isVisible();
        }
        logger.info("login with "+loggerMessage+" ended");
        return login;
    }
}