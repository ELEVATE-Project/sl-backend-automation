package org.shikshalokam.uiPageObjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

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
        //page.locator("ion-chip").filter(new Locator.FilterOptions().setHasText("female")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        verifyToastMessage("Profile Updated Successfully");
        return profileDetailsPage;
    }

    public AppProfilePage editProfile() {
        this.validPage();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Edit profile")).click();
        return profileDetailsPage;
    }

    public AppProfilePage viewRoles() {
        this.validPage();
        page.locator("#main-content").getByText("View roles").click();
        return profileDetailsPage;
    }

    public AppProfilePage submit() {
        this.validPage();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        page.waitForTimeout(2000);
        return profileDetailsPage;
    }

    public AppProfilePage verifyAddedCustomEntity(String expectedEntityType) {
        this.validPage();
        Locator element = page.locator("//ion-label[contains(text(), '" + expectedEntityType + "')]");
        assertThat(element).isVisible();
        logger.info("Custom Entity Type is added successfully");
        return profileDetailsPage;
    }

    public AppProfilePage verifyRemovedCustomEntity(String expectedEntityType) {
        this.validPage();
        Locator element = page.locator("//ion-label[contains(text(), '" + expectedEntityType + "')]");
        if (element.isVisible()) {
            throw new AssertionError("Entity Type '" + expectedEntityType + "' is unexpectedly visible on the page.");
        }
        logger.info("Custom Entity Type is removed successfully");
        return profileDetailsPage;
    }

    public AppProfilePage otherOption(String expectedEntityType) {
        this.validPage();
        Locator otherOption = page.locator("//ion-label[contains(text(), '" + expectedEntityType + "')]/../..//ion-label[contains(text(), 'Other')]");
        otherOption.isVisible();
        otherOption.click();
        return profileDetailsPage;
    }

    public AppProfilePage otherOptionValuePopUp(String placeholder, String value) {
        this.validPage();
        String xpath = String.format("//div//input[@placeholder='%s']", placeholder);
        page.locator(xpath).fill(value);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Ok")).click();
        page.waitForTimeout(1000);
        return profileDetailsPage;

    }

    public AppProfilePage verifyAddedOptionIsSelectedAndUnselect(String value) {
        this.validPage();
        Locator element = page.locator("form ion-chip").filter(new Locator.FilterOptions().setHasText(value));
        assertThat(element).isVisible();
        assertThat(element).hasClass("selected-chip md ion-activatable hydrated");
        //unselect
        element.click();
        return profileDetailsPage;

    }

    public AppProfilePage verifyAddedOptionIsRemoved(String value) {
        this.validPage();
        Locator element = page.locator("ion-chip").filter(new Locator.FilterOptions().setHasText(value));
        if (element.isVisible()) {
            throw new AssertionError("Removed option '" + value + "' is unexpectedly visible on the page.");
        }
        logger.info("Added Option is no more available");
        return profileDetailsPage;
    }
}





