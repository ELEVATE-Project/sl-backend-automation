package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.PWBasePage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AppSurveyPage extends PWBasePage
{
    private AppSurveyPage surveyPage;
    private static final Logger logger = LogManager.getLogger(AppSurveyPage.class);

    public AppSurveyPage(String givenTitleName) {
        super(givenTitleName);
        this.surveyPage=this;
    }

    public AppSurveyPage verifySurvey()
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
        page.locator("//button[@class=\"btn btn-primary mr-2\"]").click();
        page.locator("(//button[@class=\"btn btnMargin\"])[1]").click();


        return surveyPage;

    }
}
