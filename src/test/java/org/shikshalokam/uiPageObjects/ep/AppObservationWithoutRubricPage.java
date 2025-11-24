package org.shikshalokam.uiPageObjects.ep;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.PWBasePage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AppObservationWithoutRubricPage extends PWBasePage {
    private AppObservationWithoutRubricPage observationwithoutrubricpage;
    private static final Logger logger = LogManager.getLogger(AppObservationWithoutRubricPage.class);

    public AppObservationWithoutRubricPage(String givenTitleName)
    {
        super(givenTitleName);
        this.observationwithoutrubricpage = this;
    }

    public AppObservationWithoutRubricPage verifyObeservationWithoutRub(){
        page.getByRole(AriaRole.IMG, new Page.GetByRoleOptions().setName("Programs")).click();
        page.getByText("SANITY_SAASELEVATE_QA_PROGRAM").click();
        page.locator("ion-card-content").filter(new Locator.FilterOptions().setHasText("observations")).click();
        page.getByText("SANITY_SAASELEVATE_QA_OBSWITHOUT").click();
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

        return observationwithoutrubricpage;
    }

}
