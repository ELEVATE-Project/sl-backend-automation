package org.shikshalokam.uiPageObjects;

import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.microsoft.playwright.Page;
import org.shikshalokam.backend.PropertyLoader;

import static org.shikshalokam.uiPageObjects.PWBasePage.page;

public class AppChangePasswordPage extends PWBasePage {


    private AppChangePasswordPage changepasswordpage;
    private static final Logger logger = LogManager.getLogger(AppLoginPage.class);

    public AppChangePasswordPage(String givenTitleName) {
        super(givenTitleName);
        this.changepasswordpage = this;
    }

    public AppChangePasswordPage changePassword(String oldpassword, String newpassword) {

        this.validPage();
        page.getByLabel("Old password *").click();
        page.getByLabel("Old password *").fill(oldpassword);

        page.getByLabel("Enter new password *").click();
        page.getByLabel("Enter new password *").fill(newpassword);

        page.getByLabel("Confirm new password *").click();
        page.getByLabel("Confirm new password *").fill(newpassword);

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Change password")).click();
        verifyToastMessage("Your password has been changed successfully. Please log-in to continue.");

        return changepasswordpage;
    }
}
