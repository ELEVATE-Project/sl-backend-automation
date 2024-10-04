package org.shikshalokam.uiPageObjects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.GmailAPI;


public class AppSignupPage extends PWBasePage {
    private AppSignupPage signupPage;
    private static final Logger logger = LogManager.getLogger(AppSignupPage.class);

    public AppSignupPage(String givenTitleName) {
        super(givenTitleName);
        this.signupPage = this;
    }

    public AppSignupPage SignupToApp(String Name, String Email, String password) {
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
        verifyToastMessage("OTP has been sent to your registered email ID. Please enter the otp to complete the registration process.");
        page.waitForTimeout(5000);
        String script = "navigator.clipboard.writeText('" + GmailAPI.getOTP() + "')";
        logger.info(script);
        page.locator("input.otp-input").nth(0).click();
        page.evaluate(script);
        page.waitForTimeout(5000);
        page.keyboard().down("Control");
        page.keyboard().press("KeyV");
        page.keyboard().up("Control");
        page.locator("ion-checkbox").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Verify and login")).click();

        return signupPage;
    }

}

