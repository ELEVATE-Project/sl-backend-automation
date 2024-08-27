package org.shikshalokam.uiPageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AppCreateSessionPage extends PWBasePage {

    private AppCreateSessionPage createSessionPage;
    private static final Logger logger = LogManager.getLogger(AppCreateSessionPage.class);

    public AppCreateSessionPage(String givenTitleName) {
        super(givenTitleName);
        this.createSessionPage = this;
    }

    public String bbbSessionTitle = "bbbsession" + RandomStringUtils.randomAlphabetic(3);

    public AppCreateSessionPage bbbSessionCreation() {
        this.validPage();
        page.getByLabel("Session title *").click();
        page.getByLabel("Session title *").fill(bbbSessionTitle);
        page.getByLabel("Description *").click();
        page.getByLabel("Description *").fill("my session");
        page.locator("#mat-input-0").fill(getCurrentDateTime());
        page.locator("#mat-input-1").fill(getFutureDateTime(32));
        genericSessionCreationFields();
        return createSessionPage;
    }

    public String menteeCountSessionTitle = "menteeCountsession" + RandomStringUtils.randomAlphabetic(3);

    public AppCreateSessionPage menteeCountSessionCreation() {
        this.validPage();
        page.getByLabel("Session title *").click();
        page.getByLabel("Session title *").fill(menteeCountSessionTitle);
        page.getByLabel("Description *").click();
        page.getByLabel("Description *").fill("my session");
        page.locator("#mat-input-0").fill(getFutureDateTime(40));
        page.locator("#mat-input-1").fill(getFutureDateTime(75));
        genericSessionCreationFields();
        return createSessionPage;
    }

    //Editing & Updating the Created Session with Gmeet link
    public AppCreateSessionPage updateCreatedSessionWithGmeetPlatform() {
        this.validPage();
        page.getByText("2Meeting link").click();
        page.getByText("BigBlueButton (Default) Google meet Zoom WhatsApp BigBlueButton (Default)").click();
        page.locator("ion-radio").filter(new Locator.FilterOptions().setHasText("Google meet")).click();
        page.getByPlaceholder("Eg: https://meet.google.com/").click();
        page.getByPlaceholder("Eg: https://meet.google.com/").fill(fetchProperty("gmeetLink"));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        verifyToastMessage("Your session details have been updated.");
        return createSessionPage;
    }

    public String addMenteeName = "AutoDefaultMentee";
    public String addMentorName = "AutoDefaultMentor";
    public String privateSessionTitle = "PrivateSession" + RandomStringUtils.randomAlphabetic(3);

    public AppCreateSessionPage creatingPrivateSession() {
        this.validPage();
        page.getByLabel("Session title *").click();
        page.getByLabel("Session title *").fill(privateSessionTitle);
        page.getByLabel("Description *").click();
        page.getByLabel("Description *").fill("My Private Session");
        page.getByText("PrivatePublic Session type *").click();
        page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Private")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Ok")).click();
        page.locator("ion-chip").filter(new Locator.FilterOptions().setHasText("Add mentor")).click();
        page.getByPlaceholder("Search for mentor").click();
        page.getByPlaceholder("Search for mentor").fill(addMentorName);
        page.getByPlaceholder("Search for mentor").press("Enter");
        String mentorAdd = String.format("//td//div[text()=' %s']/../..//td[6]/div//div/ion-button[contains(text(),' Add ')]", addMentorName);
        page.locator(mentorAdd).first().click();
        page.locator("ion-chip").filter(new Locator.FilterOptions().setHasText("Add Mentee")).click();
        page.getByPlaceholder("Search for mentee").click();
        page.getByPlaceholder("Search for mentee").fill(addMenteeName);
        page.getByPlaceholder("Search for mentee").press("Enter");
        String menteeAdd = String.format("//td//div[text()=' %s']/../..//td[6]/div//div/ion-button[contains(text(),' Add ')]", addMenteeName);
        page.locator(menteeAdd).first().click();
        page.getByLabel("close outline").getByRole(AriaRole.IMG).click();
        page.waitForTimeout(2000);
        page.locator("#mat-input-0").fill(getFutureDateTime(100));
        page.locator("#mat-input-1").fill(getFutureDateTime(135));
        genericSessionCreationFields();
        return createSessionPage;
    }

    public String publicSessionTitle = "PublicSession" + RandomStringUtils.randomAlphabetic(3);

    public AppCreateSessionPage creatingPublicSession() {
        this.validPage();
        page.getByLabel("Session title *").click();
        page.getByLabel("Session title *").fill(publicSessionTitle);
        page.getByLabel("Description *").click();
        page.getByLabel("Description *").fill("My Public Session");
        page.getByText("PrivatePublic Session type *").click();
        page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Public")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Ok")).click();
        page.locator("ion-chip").filter(new Locator.FilterOptions().setHasText("Add mentor")).click();
        page.getByPlaceholder("Search for mentor").click();
        page.getByPlaceholder("Search for mentor").fill(addMentorName);
        page.getByPlaceholder("Search for mentor").press("Enter");
        String mentorAdd = String.format("//td//div[text()=' %s']/../..//td[6]/div//div/ion-button[contains(text(),' Add ')]", addMentorName);
        page.locator(mentorAdd).first().click();
        page.waitForTimeout(2000);
        page.locator("#mat-input-0").fill(getFutureDateTime(150));
        page.locator("#mat-input-1").fill(getFutureDateTime(185));
        genericSessionCreationFields();
        return createSessionPage;
    }

    public AppCreateSessionPage genericSessionCreationFields() {
        this.validPage();
        page.locator("ion-chip").filter(new Locator.FilterOptions().setHasText("Recommended for")).click();
        page.locator("ion-checkbox").filter(new Locator.FilterOptions().setHasText("Block education officer")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        page.locator("ion-chip").filter(new Locator.FilterOptions().setHasText("Categories")).click();
        page.locator("ion-checkbox").filter(new Locator.FilterOptions().setHasText("Communication")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        page.locator("ion-chip").filter(new Locator.FilterOptions().setHasText("Select medium")).click();
        page.locator("ion-checkbox").filter(new Locator.FilterOptions().setHasText("English")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Publish and Add link")).click();
        verifyToastMessage("Session created and shared with the community.");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        verifyToastMessage("Your session details have been updated.");
        return createSessionPage;
    }


    public AppCreateSessionPage addMentee() {
        this.validPage();
        page.locator("ion-chip").filter(new Locator.FilterOptions().setHasText("Add Mentee")).click();
        page.getByPlaceholder("Search for mentee").click();
        page.getByPlaceholder("Search for mentee").fill(addMenteeName);
        page.getByPlaceholder("Search for mentee").press("Enter");
        String menteeAdd = String.format("//td//div[text()=' %s']/../..//td[6]/div//div/ion-button[contains(text(),' Add ')]", addMenteeName);
        page.locator(menteeAdd).first().click();
        page.getByLabel("close outline").getByRole(AriaRole.IMG).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Save")).click();
        verifyToastMessage("Your session details have been updated.");
        page.locator("ion-button").filter(new Locator.FilterOptions().setHasText("Submit")).locator("button").click();
        page.waitForTimeout(3000);
        return createSessionPage;
    }
}

