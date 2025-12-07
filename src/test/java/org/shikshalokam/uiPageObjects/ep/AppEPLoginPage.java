package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.AppLoginPage;
import org.shikshalokam.uiPageObjects.PWBasePage;
import org.shikshalokam.uiPageObjects.Robot;

public class AppEPLoginPage extends PWBasePage {
    AppEPLoginPage login;
    private static final Logger logger = LogManager.getLogger(AppEPLoginPage.class);

    public AppEPLoginPage(String givenTitleName) {
        super(givenTitleName);
        this.login = this;
    }

    public void openURL() {
        Robot robot = new Robot();
        robot.openApp(fetchProperty("ep.url"));
    }


    public AppEPLoginPage logIntoPortal() {
        logger.info("login started");
        String userName=fetchProperty("ep.mail");
        String password=fetchProperty("ep.password");
        page.getByLabel("Email / Mobile / Username").click();
        page.getByLabel("Email / Mobile / Username").fill(userName);
        page.getByLabel("Password").click();
        page.getByLabel("Password").fill(password);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
        logger.info("login ended");


        return login;
    }
}
