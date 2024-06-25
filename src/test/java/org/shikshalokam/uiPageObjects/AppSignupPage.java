package org.shikshalokam.uiPageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.GmailAPI;

import java.util.concurrent.TimeUnit;

public class AppSignupPage extends PWBasePage {
    private AppSignupPage signupPage;
    private static final Logger logger = LogManager.getLogger(AppSignupPage.class);
    public AppSignupPage(String givenTitleName) {
        super(givenTitleName);
        this.signupPage = this;
    }
    public AppSignupPage SignupToApp(String Name, String Email, String password) throws InterruptedException {
        this.validPage();

        page.getByLabel("Name *").click();
        page.getByLabel("Name *").fill(Name);

        page.getByLabel("Email *").click();
        page.getByLabel("Email *").fill(Email);

        page.getByLabel("Password *", new Page.GetByLabelOptions().setExact(true)).click();
        page.getByLabel("Password *", new Page.GetByLabelOptions().setExact(true)).fill(password);

        page.getByLabel("Confirm password *").click();
        page.getByLabel("Confirm password *").fill(password);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Validate OTP")).click();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Generate OTP")).click();
        TimeUnit.SECONDS.sleep(10);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            logger.info("Exception from the sleep method "+ e.getMessage());
        }
        String script = "navigator.clipboard.writeText('"+ GmailAPI.getOTP("Your OTP to sign-up on MentorED")+"')";
        System.out.println(script);
        page.locator("//div[@class=\"otp-field\"]").click();
        page.evaluate(script);
        TimeUnit.SECONDS.sleep(5);
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            logger.info("Exception from the sleep method "+ e.getMessage());
        }
        page.keyboard().down("Control");
        page.keyboard().press("KeyV");
        page.keyboard().up("Control");
        page.locator("ion-checkbox").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Verify and login")).click();

        return signupPage;
    }

    public AppSignupPage logOutFromApp() {
        page.locator("div").filter(new Locator.FilterOptions().setHasText("Logout")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Logout")).click();
        return signupPage;
    }
}

