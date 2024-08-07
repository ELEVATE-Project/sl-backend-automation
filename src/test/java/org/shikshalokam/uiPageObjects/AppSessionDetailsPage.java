package org.shikshalokam.uiPageObjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
        page.waitForTimeout(2000);
        page.locator("//ion-button[text()='Enroll']").click();
        verifyToastMessage("You have enrolled for this session successfully.");
        return sessionDeatilsPage;
    }

    public AppSessionDetailsPage unEnrollSession() {
        this.validPage();
        page.waitForTimeout(2000);
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
        Locator joinButtonOnPopUp = page.locator("(//ion-button[text()='Join'])[2]");
        joinButtonOnPopUp.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        joinButtonOnPopUp.click();

        return sessionDeatilsPage;
    }

    public AppSessionDetailsPage bbbSessionOptions() {
        Locator closeButton = page.locator("[data-test=\"closeModal\"]");
        // Wait for the locator to be visible and then click
        closeButton.waitFor(new Locator.WaitForOptions().setTimeout(10000));
        closeButton.click();
        page.locator("[data-test=\"optionsButton\"]").click();
        return sessionDeatilsPage;
    }

    public AppSessionDetailsPage leaveSession() {
        page.locator("[data-test=\"logout\"]").click();
        page.getByLabel("OK").click();
        return sessionDeatilsPage;
    }

    public AppSessionDetailsPage terminateSession() {
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

    public AppSessionDetailsPage verifyMenteeCount(String count) {
        this.validPage();
        Locator menteeCount = page.locator("//p[@class='mentee-count']");
        String actualCount = menteeCount.textContent();
        logger.info("mentee count:{}", actualCount);
        assertThat(menteeCount).containsText(count);
        return sessionDeatilsPage;
    }

    public AppSessionDetailsPage verifyAddedMentorName(String mentorName ) {
        this.validPage();
        Locator mentor = page.locator("//p[text()='Mentor']/..//div/p");
        String actualMentor = mentor.textContent();
        logger.info("mentor Name:{}", actualMentor);
        assertThat(mentor).containsText(mentorName);
        return sessionDeatilsPage;
    }

    public AppSessionDetailsPage verifyMenteeListEmail(String expectedEnrolledMentee) {
        this.validPage();
        page.locator("//ion-button[text()='View list']").click();
        Locator menteeOnList = page.locator("//td[@class='mat-cell cdk-cell cell-container cdk-column-email mat-column-email ng-star-inserted']");
        String menteeEmail = menteeOnList.textContent();
        logger.info("Enrolled Mentee in the List :{}", menteeEmail);
        assertThat(menteeOnList).hasText(expectedEnrolledMentee);
        page.getByLabel("close").locator("path").click();
        return sessionDeatilsPage;
    }

    public AppSessionDetailsPage verifyMenteeListName(String expectedEnrolledMentee) {
        this.validPage();
        page.locator("//ion-button[text()='View list']").click();
        Locator menteeOnList = page.locator("(//td//div[@class='cell-text session-name'])[6]");
        String menteeName = menteeOnList.textContent();
        logger.info("Enrolled Mentee in the List :{}", menteeName);
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

    public AppSessionDetailsPage editSession() {
        this.validPage();
        page.getByLabel("create outline").getByRole(AriaRole.IMG).click();
        return sessionDeatilsPage;
    }

}
