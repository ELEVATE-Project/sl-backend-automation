package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.PWBasePage;

import static org.shikshalokam.uiPageObjects.PWBasePage.page;

public class AppProgramPage extends PWBasePage
{
    private AppProgramPage programpage;

    private static final Logger logger = LogManager.getLogger(AppProgramPage.class);

    public AppProgramPage(String Elevate)
    {
        super("Welcome to shikshagrahanew");
        this.programpage = this;

    }

    public AppProgramPage verifyProgram()
    {
        page.getByText("Programs").click();
        page.getByPlaceholder("Search your program here").click();
        page.getByPlaceholder("Search your program here").fill("SANITY_SAASELEVATE_QA_PROGRAM");
        page.getByText("SANITY_SAASELEVATE_QA_PROGRAM").click();
        page.locator("ion-title").filter(new Locator.FilterOptions().setHasText("observations")).locator("div").click();
        page.getByText("SANITY_SAASELEVATE_QA_OBSWITH", new Page.GetByTextOptions().setExact(true)).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add school")).click();
        page.getByText("KAILASHAHAR GIRLS HS SCHOOL").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add")).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("KAILASHAHAR GIRLS HS SCHOOL")).click();
        page.locator("button").filter(new Locator.FilterOptions().setHasText("more_vert")).click();
        page.getByRole(AriaRole.MENUITEM, new Page.GetByRoleOptions().setName("Edit")).click();
        page.getByPlaceholder("Enter name here").fill("Observation");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Update")).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Observation")).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Domain 1")).click();
        page.getByText("Planning & Execution").click();
        page.getByPlaceholder("Enter your response").click();
        page.getByPlaceholder("Enter your response").fill("Student Name");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add remarks")).click();
        page.getByPlaceholder("Add a remark").click();
        page.getByPlaceholder("Add a remark").fill("Remarks");
        page.getByText(" Submit ").click();
        page.getByText("Confirm").nth(1).click();

        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        page.getByText("Status: completed").click();

        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_downward")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes")).click();

        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        page.getByRole(AriaRole.BANNER).locator("svg").click();
        page.getByRole(AriaRole.BANNER).locator("svg").click();


        page.getByText("Downloads").click();
        page.getByText("SANITY_SAASELEVATE_QA_OBSWITH").click();
        page.locator("ion-title").filter(new Locator.FilterOptions().setHasText("observations")).locator("div").click();



        page.getByText("Status: not Started").click();
        page.getByText("Data based Governance").click();
        page.getByLabel("Male", new Page.GetByLabelOptions().setExact(true)).check();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add remarks")).first().click();
        page.getByPlaceholder("Add a remark").click();
        page.getByPlaceholder("Add a remark").fill("Remarks");
        page.getByLabel("BFSI").check();
        page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("Hospitality")).check();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add remarks")).click();
        page.locator("#mat-input-6").click();
        page.locator("#mat-input-6").fill("Remarks for Sectors");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("2")).click();
        page.getByPlaceholder("Enter your response").click();
        page.getByPlaceholder("Enter your response").fill("Status");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add remarks")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Add a remark")).click();
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Add a remark")).fill("Status Remarks");

        page.getByText(" Submit ").click();
        page.getByText("Confirm").nth(1).click();




        return programpage;
    }

    public AppProgramPage verifyProjectsInProgram()
    {

        return programpage;
    }

    public AppProgramPage verifyObsWithRubInProgram()
    {

        return programpage;
    }

    public AppProgramPage verifyObsWithOutRubInProgram()
    {
        page.getByRole(AriaRole.IMG, new Page.GetByRoleOptions().setName("Programs")).click();
        page.getByText("SANITY_SAASELEVATE_QA_PROGRAM").click();
        page.locator("ion-card-content").filter(new Locator.FilterOptions().setHasText("observations")).click();
//        page.getByText("SANITY_SAASELEVATE_QA_OBSWITHOUT").click();
        page.getByText("SANITY_SAASELEVATE_QA_OBSWITHOUT").nth(0).click();


        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add school")).click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Cancel")).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("PURATAN KANTIA T.P J.B SCHOOL")).click();
        page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Observation")).click();
        page.getByText("KARNATAKA PUBLIC SCHOOLS GHPS").click();
        page.getByLabel("Open calendar").click();
        page.getByLabel("11 September").click();
        page.getByText("Yes", new Page.GetByTextOptions().setExact(true)).first().click();
        page.getByText("Yes", new Page.GetByTextOptions().setExact(true)).first().click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("2")).click();
        page.locator("mat-radio-group").filter(new Locator.FilterOptions().setHasText("Yes No Others (Please specify)")).locator("mat-radio-button").first().click();
        page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Yes").setExact(true)).check();
        page.locator("lib-remarks").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add remarks")).click();
        page.locator("lib-remarks > .mat-mdc-form-field > .mat-mdc-text-field-wrapper > .mat-mdc-form-field-flex > .mat-mdc-form-field-infix").click();
        page.getByPlaceholder("Add a remark").click();
        page.getByPlaceholder("Add a remark").fill("Rem");
        page.getByPlaceholder("Add a remark").press("CapsLock");
        page.getByPlaceholder("Add a remark").fill("Rem");
        page.getByPlaceholder("Add a remark").press("CapsLock");
        page.getByPlaceholder("Add a remark").fill("Remarks");
        page.getByText("Yes, a fully equipped science").click();
        page.getByLabel("YouTube").check();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("3")).click();
        page.getByPlaceholder("Enter your response").click();
        page.getByPlaceholder("Enter your response").fill("10");
        page.locator("[id=\"\\32 \"]").getByText("Yes").click();
        page.getByRole(AriaRole.SLIDER).fill("8");
        page.getByText("arrow_back 1 2 3 arrow_forward").click();
        page.getByLabel("icon-button with a arrow_forward icon").click();

        return programpage;

    }

    public AppProgramPage verifySurveyInProgram()
    {
        page.getByText("Programs").click();
        page.getByText("SANITY_SAASELEVATE_QA_PROGRAM").click();
        page.locator("ion-title").filter(new Locator.FilterOptions().setHasText("surveys")).locator("div").click();
        page.getByText("SANITY_SAASELEVATE_QA_SURVEY").click();
        page.getByLabel("Always").check();
        page.getByText("Internet videos").click();
        page.getByText("Textbooks").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Add remarks")).click();
        page.getByPlaceholder("Add a remark").click();
        page.getByPlaceholder("Add a remark").fill("Remarks");
        page.getByText("Group discussions").click();
        page.getByText("Hands-on experiments").click();
        page.getByText("Somewhat confident").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("2")).click();
        page.getByText("Lack of materials").click();
        page.locator("#mat-mdc-checkbox-15 div").filter(new Locator.FilterOptions().setHasText("No challenges")).click();
        page.locator("[id=\"\\36 8cbd82166175ef9a1a93de5R1\"] > .mdc-form-field").click();
        page.locator("[id=\"\\36 8cbd82166175ef9a1a93de5R2\"] > .mdc-form-field").click();
        page.getByText("No change").click();
        page.getByText("Moderate improvement").click();
        page.getByRole(AriaRole.SLIDER).fill("7");
        page.getByText("arrow_back 1 2 arrow_forward").click();

        return programpage;
    }

}
