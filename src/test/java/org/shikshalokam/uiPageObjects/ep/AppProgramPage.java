package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.PWBasePage;

import java.util.regex.Pattern;


public class AppProgramPage extends PWBasePage {

    AppProgramPage programPage;
    private static final Logger logger = LogManager.getLogger(AppProgramPage.class);

    public AppProgramPage(String givenTitleName) {
        super(givenTitleName);
        this.programPage = this;
    }

//    public AppProgramPage selectProgramName(String selectProgramName) {
//        logger.info("Selecting Program name started");
//        page.getByText("Programs", new Page.GetByTextOptions().setExact(true)).click();
//
//        page.getByText(selectProgramName).click();
//        logger.info("Selecting Program name ended");
//        return programPage;
//    }

    public AppProgramPage submitProjectFromProgram(String projectName) {
        logger.info("Submitting a Project inside the program has started.");
        page.locator("ion-title").filter(new Locator.FilterOptions().setHasText("projects")).locator("div").click();
        page.getByText(projectName, new Page.GetByTextOptions().setExact(true)).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Start Improvement")).click();
        page.getByText("Task details").click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("1. Teachers should understand")).click();
        page.locator("#mat-select-value-1").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Completed")).click();
        page.getByRole(AriaRole.BANNER).locator("path").click();
//        page.getByRole(AriaRole.BANNER).locator("svg").click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("2. Teachers should implement")).click();
//        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Completed")).click();
        page.getByPlaceholder("Add subtask name").click();
        page.getByPlaceholder("Add subtask name").fill("Sub task 1");
        page.getByPlaceholder("Add subtask name").press("ArrowLeft");
        page.getByPlaceholder("Add subtask name").press("ArrowRight");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add subtask")).click();
        page.locator(".sub-task-body > div:nth-child(2) > .mat-mdc-form-field > .mat-mdc-text-field-wrapper > .mat-mdc-form-field-flex > .mat-mdc-form-field-infix").click();
        page.getByRole(AriaRole.OPTION, new Page.GetByRoleOptions().setName("Completed")).click();
        page.getByRole(AriaRole.BANNER).locator("path").click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("3. Teachers should implement")).click();
        page.getByText("Not Started").click();
        page.getByText("Completed").click();
        page.getByRole(AriaRole.BANNER).locator("path").click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("4. Teachers should implement")).click();
        page.getByText("Not Started").click();
        page.getByText("Completed").click();
        page.getByRole(AriaRole.BANNER).locator("path").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Load more")).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("5. Teachers should upload")).click();
        page.getByText("Not Started").click();
        page.getByText("Completed").click();
        page.getByRole(AriaRole.BANNER).locator("path").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit Improvement")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit Improvement")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
//        page.locator("lib-icon-list mat-icon").nth(2).click();
        page.getByRole(AriaRole.BANNER).locator("svg").click();
        page.getByRole(AriaRole.BANNER).locator("path").click();
        logger.info("Submitting a Project inside the program has ended.");
        return programPage;
    }

    public AppProgramPage submitObservationWithRubricFromProgram(String ObservationWithRubricName) {
        logger.info("Submitting a Observation With Rubric inside the program has started.");
        page.locator("ion-title").filter(new Locator.FilterOptions().setHasText("observations")).locator("div").click();
        page.getByText(ObservationWithRubricName).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("BAGAFA ASRAM H.S SCHOOL")).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Observation")).click();
        page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^0%$"))).nth(1).click();
        page.getByText("Planning & Execution").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Start")).click();
        page.getByPlaceholder("Enter your response").fill("Stud name");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add remarks")).click();
        page.getByPlaceholder("Add a remark").fill("Remarks");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
//      page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm")).click();
        page.locator("(//button[@class=\"btn btnMargin ng-star-inserted\"])[1]").click();

        page.locator("div").filter(new Locator.FilterOptions().setHasText(Pattern.compile("^0%$"))).nth(1).click();
        page.getByText("Data based Governance").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Start")).click();
        page.getByLabel("Male", new Page.GetByLabelOptions().setExact(true)).check();
        page.getByLabel("IAF").check();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("2")).click();
        page.getByPlaceholder("Enter your response").fill("Selector");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add remarks")).click();
        page.getByPlaceholder("Add a remark").fill("Remarks");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        page.locator("(//button[@class=\"btn btnMargin ng-star-inserted\"])[1]").click();

        page.locator("//button[contains(@class,'back-button mdc-icon-button mat-mdc-icon-button mat-unthemed mat-mdc-button-base')]").click();
        page.locator("//button[contains(@class,'back-button mdc-icon-button mat-mdc-icon-button mat-unthemed mat-mdc-button-base')]").click();
        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        page.getByRole(AriaRole.BANNER).locator("svg").click();
        logger.info("Submitting a Observation With Rubric inside the program has ended.");
        return programPage;
    }

    public AppProgramPage submitObservationWithOutRubricFromProgram(String ObservationWithOutRubricName) {
        logger.info("Submitting a Observation WithOut Rubric inside the program has started.");
        page.locator("ion-title").filter(new Locator.FilterOptions().setHasText("observations")).locator("div").click();

//        page.getByRole(AriaRole.BANNER).locator("path").click();
        page.getByText(ObservationWithOutRubricName, new Page.GetByTextOptions().setExact(true)).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("BAGAFA ASRAM H.S SCHOOL")).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Observation")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Start")).click();
        page.getByLabel("KARNATAKA PUBLIC SCHOOLS GHPS").check();
        page.getByLabel("Open calendar").click();
        page.getByLabel("Choose month and year").click();
        page.getByLabel("2026").click();
        page.getByLabel("Jan").click();
        page.getByLabel("11 January").click();
        page.locator("[id=\"\\30 \"] span").filter(new Locator.FilterOptions().setHasText("3 . Has the teacher received")).getByLabel("Yes").check();
        page.getByText("Yes", new Page.GetByTextOptions().setExact(true)).nth(1).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("2")).click();
        page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Yes").setExact(true)).check();
        page.getByLabel("Yes, a fully equipped science").check();
        page.getByLabel("YouTube").check();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("3")).click();

        page.getByPlaceholder("Enter your response").fill("2");
        page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Yes")).check();
        page.getByPlaceholder("Enter your response").click();
        page.getByRole(AriaRole.SLIDER).fill("5");
        page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Yes")).check();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        page.locator("(//button[@class=\"btn btnMargin ng-star-inserted\"])[1]").click();

        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        page.getByRole(AriaRole.BANNER).locator("path").click();
        page.getByRole(AriaRole.BANNER).locator("svg").click();

        logger.info("Submitting a Observation WithOut Rubric inside the program has ended.");
        return programPage;
    }

    public AppProgramPage submitSurveyFromProgram(String surveyName) {
        logger.info("Submitting a survey inside the program has started.");
        page.locator("ion-title").filter(new Locator.FilterOptions().setHasText("surveys")).locator("div").click();
        page.getByText(surveyName).click();
        page.getByLabel("Always").check();
        page.getByLabel("Internet videos").check();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add remarks")).click();
        page.getByPlaceholder("Add a remark").fill("Remarks");
        page.getByLabel("Group discussions").check();
        page.getByLabel("Very confident").check();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("2")).click();
        page.getByLabel("Lack of materials").check();
        page.getByLabel("Lack of training").check();
        page.getByLabel("Participate with support").check();
        page.getByLabel("Significant improvement").check();
        page.getByRole(AriaRole.SLIDER).fill("6");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Submit")).click();
        page.locator("(//button[@class=\"btn btnMargin ng-star-inserted\"])[1]").click();
        page.getByRole(AriaRole.BANNER).locator("path").click();
        logger.info("Submitting a survey inside the program has ended.");
        return programPage;
    }
}
