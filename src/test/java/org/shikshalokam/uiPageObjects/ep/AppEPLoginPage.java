package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.GmailAPI;
import org.shikshalokam.backend.PropertyLoader;
import org.shikshalokam.uiPageObjects.AppLoginPage;
import org.shikshalokam.uiPageObjects.PWBasePage;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.Assert;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.shikshalokam.backend.TestCRUDUserPermissions.logger;
import static org.shikshalokam.uiPageObjects.ep.AppRegistrationPage.getOTPAndFill;

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

    public AppEPLoginPage testLoginWithMultipleOptions(String userName, String password, String loggerMessage) {
        logger.info("login with " + loggerMessage + " started");
        page.locator("//input[@name='login-username']").click();
        page.locator("//input[@name='login-username']").fill(userName);
        page.locator("//input[@name='login-password']").click();
        page.locator("//input[@name='login-password']").fill(password);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
        if (loggerMessage.equals("Negative test")) {
            assertThat(page.getByText("Welcome")).not().isVisible();
        }
        logger.info("login with " + loggerMessage + " ended");
        return login;
    }

    public AppEPLoginPage verifyForgotPassword() {
        logger.info("Verifying Forgot Password started");
        String userName = fetchProperty("ep.mail");
        String password = fetchProperty("ep.password");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Forgot Password?")).click();
        page.getByLabel("Email/Mobile/Username").fill(userName);
        page.getByLabel("New Password", new Page.GetByLabelOptions().setExact(true)).fill(password + "1");
        page.getByLabel("Confirm New Password").fill(password + "1");

        logger.info("OTP filling started");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Send OTP")).click();
        page.waitForTimeout(2000);
        String script = "navigator.clipboard.writeText('" + GmailAPI.getOTP() + "')";
        logger.info(script);
        page.locator("#otp-0").click();
        page.evaluate(script);
        page.waitForTimeout(10000);
        page.keyboard().down("Control");
        page.keyboard().press("KeyV");
        page.keyboard().up("Control");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Verify OTP")).click();

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("OK")).click();
        logger.info("Verifying Forgot Password Stop");
        return login;
    }

    public AppEPLoginPage verifyResetPassword() {
        logger.info("Verifying Forgot Password");
        String userName = fetchProperty("ep.mail");
        String password = fetchProperty("ep.password");
        page.getByTestId("AccountCircleIcon").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Reset Password")).click();
        page.getByLabel("Old Password").fill(password + "1");
        page.getByLabel("New Password").fill(password);
        page.getByLabel("Confirm Password").fill(password);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Reset Password")).click();
        return login;
    }

}