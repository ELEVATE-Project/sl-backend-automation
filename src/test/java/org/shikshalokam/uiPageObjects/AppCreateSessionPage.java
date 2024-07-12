package org.shikshalokam.uiPageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

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
        page.locator("ion-chip").filter(new Locator.FilterOptions().setHasText("Block education officer")).click();
        page.locator("ion-chip").filter(new Locator.FilterOptions().setHasText("Communication")).click();
        page.locator("ion-chip").filter(new Locator.FilterOptions().setHasText("English")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Publish and Add link")).click();
        verifyToastMessage("Session created and shared with the community.");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        verifyToastMessage("Your session details have been updated.");
        page.getByLabel("back").click();
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
        page.locator("ion-chip").filter(new Locator.FilterOptions().setHasText("Block education officer")).click();
        page.locator("ion-chip").filter(new Locator.FilterOptions().setHasText("Communication")).click();
        page.locator("ion-chip").filter(new Locator.FilterOptions().setHasText("English")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Publish and Add link")).click();
        verifyToastMessage("Session created and shared with the community.");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        verifyToastMessage("Your session details have been updated.");
        page.getByLabel("create outline").getByRole(AriaRole.IMG).click();
        page.getByText("2Meeting link").click();
        page.getByText("BigBlueButton (Default) Google meet Zoom WhatsApp BigBlueButton (Default)").click();
        page.locator("ion-radio").filter(new Locator.FilterOptions().setHasText("Google meet")).click();
        page.getByPlaceholder("Eg: https://meet.google.com/").click();
        page.getByPlaceholder("Eg: https://meet.google.com/").fill(fetchProperty("gmeetLink"));//"https://meet.google.com/pqr-gfwc-jsn");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        verifyToastMessage("Your session details have been updated.");
        return createSessionPage;
    }
}

