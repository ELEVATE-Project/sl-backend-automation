package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.GmailAPI;
import org.shikshalokam.uiPageObjects.PWBasePage;

public class AppRegistrationPage extends PWBasePage {
    AppRegistrationPage registration;
    private static final Logger logger = LogManager.getLogger(AppRegistrationPage.class);

    public AppRegistrationPage(String Elevate) {
        super("Welcome to shikshagrahanew");
        this.registration = this;

    }

    public static void openAndFillRegistrationForm() {
        logger.info("Registration form filling started");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Register")).click();
        page.getByLabel("First Name *").click();
        page.getByLabel("First Name *").fill(fetchProperty("ep.username"));
        page.getByLabel("Username *").click();
        page.getByLabel("Username *").fill(fetchProperty("ep.mail"));
        page.getByLabel("Password *", new Page.GetByLabelOptions().setExact(true)).click();
        page.getByLabel("Password *", new Page.GetByLabelOptions().setExact(true)).fill(fetchProperty("ep.password"));
        page.getByLabel("Confirm Password*").click();
        page.getByLabel("Confirm Password*").fill(fetchProperty("ep.password"));
        page.getByLabel("Student").first().click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(fetchProperty("ep.role"))).first().click();
        page.getByLabel("Sub-Role *").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName(fetchProperty("ep.subrole1"))).getByRole(AriaRole.CHECKBOX).check();
        page.keyboard().press("Escape");

//    Based on org select and enter the registration code


        String URL = fetchProperty("ep.url");

        String searchString = "shikshagraha";
        boolean isPresent = URL.contains(searchString);
        if (isPresent == true) {
            System.out.println("shikshagraha:" + URL);
        } else {
            page.getByLabel("Registration Code *").click();
            page.getByLabel("Registration Code *").fill(fetchProperty("ep.registrationCode"));
            boolean present = page.getByLabel("Registration Code Dummy *").isVisible();

            if (present == true) {
                page.getByLabel("Registration Code Dummy *").click();
                page.getByLabel("Registration Code Dummy *").fill("ABCD");
            }


        }


        page.getByLabel("Udise *").click();
        page.getByLabel("Udise *").fill(fetchProperty("ep.udiseCode"));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Fetch")).click();
        logger.info("Registration form filling ended");

    }

    public static void getOTPAndFill() {
        logger.info("OTP filling started");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Send OTP")).click();
        page.waitForTimeout(2000);
        String script = "navigator.clipboard.writeText('" + GmailAPI.getOTP() + "')";
        logger.info(script);
        page.getByLabel("OTP digit 1").click();
        page.evaluate(script);
        page.waitForTimeout(2000);
        page.keyboard().down("Control");
        page.keyboard().press("KeyV");
        page.keyboard().up("Control");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Verify")).click();


        logger.info("OTP filling ended");
    }

}