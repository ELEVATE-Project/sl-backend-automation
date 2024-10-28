package org.shikshalokam.uiPageObjects;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.GmailAPI;


public class AppResetPasswordPage extends PWBasePage {
    private AppResetPasswordPage resetPasswordPage;

    private static final Logger logger = LogManager.getLogger(AppResetPasswordPage.class);

    public AppResetPasswordPage(String givenTitleName) {
        super(givenTitleName);
        this.resetPasswordPage = this;
    }

    public AppResetPasswordPage resetPassword(String Email, String password) {
        this.validPage();
        page.click("input[name=\"ion-input-2\"]");
        page.fill("input[name=\"ion-input-2\"]", Email);
        page.getByLabel("Enter new password *").click();
        page.getByLabel("Enter new password *").fill(password);

        page.getByLabel("Confirm new password *").click();
        page.getByLabel("Confirm new password *").fill(password);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Validate OTP")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Generate OTP")).click();
        verifyToastMessage("OTP has been sent to your registered email ID. Please enter the number to update your password.");
        page.waitForTimeout(5000);
        String script = "navigator.clipboard.writeText('" + GmailAPI.getOTP() + "')";
        logger.info(script);
        page.locator("input.otp-input").nth(0).click();
        page.evaluate(script);
        page.waitForTimeout(5000);
        page.keyboard().down("Control");
        page.keyboard().press("KeyV");
        page.keyboard().up("Control");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Verify and login")).click();
        verifyToastMessage("Password reset successfully.");
        return resetPasswordPage;
    }


}