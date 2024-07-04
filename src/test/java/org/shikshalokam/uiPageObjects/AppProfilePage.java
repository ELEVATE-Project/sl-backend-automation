package org.shikshalokam.uiPageObjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class AppProfilePage extends PWBasePage {

    private AppProfilePage profileDetailsPage;
    private static final Logger logger = LogManager.getLogger(AppProfilePage.class);

    public AppProfilePage(String givenTitleName) {
        super(givenTitleName);
        this.profileDetailsPage = this;
    }

    public AppProfilePage updateProfile() {
        this.validPage();
        page.getByText("Andhra PradeshArunachal").click();
        page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Andhra Pradesh")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Ok")).click();
        page.locator("ion-chip").filter(new Locator.FilterOptions().setHasText("Block education officer")).click();
        page.getByLabel("Your experience in years *").click();
        page.getByLabel("Your experience in years *").fill("5");
        page.getByLabel("Tell us about yourself *").click();
        page.getByLabel("Tell us about yourself *").fill("demo");
        page.getByText("Communication").click();
        page.getByLabel("Education qualification *").click();
        page.getByLabel("Education qualification *").fill("BE");
        page.locator("ion-chip").filter(new Locator.FilterOptions().setHasText("English")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        verifyToastMessage("Profile Updated Successfully");
        return profileDetailsPage;
    }
}
