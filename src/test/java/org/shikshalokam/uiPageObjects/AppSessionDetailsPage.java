package org.shikshalokam.uiPageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AppSessionDetailsPage extends PWBasePage {
    private AppSessionDetailsPage sessionDeatilsPage;
    private static final Logger logger = LogManager.getLogger(AppSessionDetailsPage.class);


    public AppSessionDetailsPage(String givenTitleName) {
        super(givenTitleName);
        this.sessionDeatilsPage = this;
    }


    public AppSessionDetailsPage enrollSession() {
        this.validPage();
        page.locator("//ion-button[text()='Enroll']").click();
        verifyToastMessage("You have enrolled for this session successfully.");
        return sessionDeatilsPage;
    }

    public AppSessionDetailsPage unEnrollSession() {
        this.validPage();
        page.locator("//ion-button[text()='Unenroll']").click();
        page.locator("//span[normalize-space()='Unenroll']").click();
        verifyToastMessage("You have un-enrolled for this session successfully.");
        return sessionDeatilsPage;
    }

    public AppSessionDetailsPage startSession() {
        this.validPage();
        page.locator("//ion-button[text()='Start session']").click();
        return sessionDeatilsPage;
    }

    public AppSessionDetailsPage joinSession() {
        this.validPage();
        page.locator("//ion-button[text()='Join']").click();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            logger.info("Exception from the sleep method " + e.getMessage());
        }
        page.locator("(//ion-button[text()='Join'])[2]").click();
        return sessionDeatilsPage;
    }

    public AppSessionDetailsPage leaveSession() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            logger.info("Exception from the sleep method " + e.getMessage());
        }
        page.locator("[data-test=\"closeModal\"]").click();
        page.locator("[data-test=\"optionsButton\"]").click();
        page.locator("[data-test=\"logout\"]").click();
        page.getByLabel("OK").click();
        return sessionDeatilsPage;
    }

    public AppSessionDetailsPage terminateSession() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            logger.info("Exception from the sleep method " + e.getMessage());
        }
        page.locator("[data-test=\"closeModal\"]").click();
        page.locator("[data-test=\"optionsButton\"]").click();
        page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName(" End meeting Terminates the")).click();
        page.locator("[data-test=\"confirmEndMeeting\"]").click();
        page.getByLabel("OK").click();
        return sessionDeatilsPage;
    }

    public AppSessionDetailsPage submitFeedback() {
        this.validPage();
        page.locator("app-star-rating svg").nth(4).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit Feedback")).click();
        verifyToastMessage("Feedback submitted successfully.");
        return sessionDeatilsPage;
    }

    public AppSessionDetailsPage verifyMenteeCount(int count) {
        this.validPage();
        Locator menteeCount = page.locator("//p[@class='mentee-count']");
        String actualCount = menteeCount.textContent();
        logger.info("mentee count:{}", actualCount);
        assertThat(menteeCount).containsText(String.valueOf(count));
        return sessionDeatilsPage;
    }

    public AppSessionDetailsPage viewAndVerifyList(String expectedEnrolledMentee) {
        this.validPage();
        page.locator("//ion-button[text()='View list']").click();
        Locator menteeOnList = page.locator("//td[@class='mat-cell cdk-cell cell-container cdk-column-email mat-column-email ng-star-inserted']");
        String menteeEmail = menteeOnList.textContent();
        logger.info("Enrolled Mentee in the List :{}", menteeEmail);
        assertThat(menteeOnList).hasText(expectedEnrolledMentee);
        page.getByLabel("close").locator("path").click();
        return sessionDeatilsPage;
    }

    public AppSessionDetailsPage deleteSession() {
        this.validPage();
        page.getByLabel("trash outline").locator("svg").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes delete")).click();
        verifyToastMessage("Session deleted. This session is no longer available.");
        return sessionDeatilsPage;
    }
}
