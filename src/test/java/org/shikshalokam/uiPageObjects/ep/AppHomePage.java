package org.shikshalokam.uiPageObjects.ep;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.uiPageObjects.PWBasePage;
import org.testng.Assert;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.shikshalokam.uiPageObjects.PWBasePage.page;

public class AppHomePage extends PWBasePage
{
    private AppHomePage homePage;

    private static final Logger logger = LogManager.getLogger(AppHomePage.class);

    public AppHomePage(String givenTitleName)
    {
        super(givenTitleName);
        this.homePage = this;

    }

    //Verify that After log in Land on Home page
    public  AppHomePage verifyHomePage()
    {
        logger.info("Login and Home page verification started");
        assertThat(page.getByText("Welcome, auto")).isVisible();
        logger.info("Login and Home page verification ended");
        return homePage;
    }

    //Check the Programs page is working fine
    public AppHomePage clickOnPrograms()
    {
        logger.info("Verify click On action for Programs started");
        page.getByText("Programs").click();
        Locator programsText = page.locator("h1").getByText("Programs", new Locator.GetByTextOptions().setExact(true));
        assertThat(programsText).isVisible();
        logger.info("Verify click On action for Programs ended");
        return homePage;
    }

    //Check the Projects page is working fine
    public AppHomePage clickOnProject(){
        logger.info("Verify click On action for Project started");
        page.getByText("Projects").click();
        assertThat(page.getByText("Projects")).isVisible();
        logger.info("Verify click On action for Project ended");
        return homePage;
    }

    //Check the Observation page is working fine
    public AppHomePage clickOnObservation()
    {
        logger.info("Verify click On action for Observation started");
        page.getByText("Observations").click();
        assertThat(page.getByText("Observations")).isVisible();
        logger.info("Verify click On action for Observation started");
        return homePage;
    }

    //Check the Survey page is working fine
    public AppHomePage  clickOnSurvey()
    {
        logger.info("Verify click On action for Survey started");
        page.getByText("Survey").click();
        Locator surveyText = page.getByText("Survey").nth(0);  // first match
        assertThat(surveyText).isVisible();
        logger.info("Verify click On action for Survey ended");
        return homePage;
    }

    //Check the Reports page is working fine
    public AppHomePage clickOnReports()
    {
        logger.info("Verify click On action for Reports started");
        page.getByText("Reports").click();
        assertThat(page.getByText("Reports", new Page.GetByTextOptions().setExact(true))).isVisible();
        logger.info("Verify click On action for Reports ended");
        return homePage;
    }

    //Check the Profile page is working fine
    public  AppHomePage clickOnProfile()
    {
        logger.info("Verify click On action for Profile started");
        page.getByTestId("AccountCircleIcon").click();
        page.getByLabel("Account").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Yes, Logout")).click();
        assertThat(page.getByText("Login")).isVisible();
        logger.info("Verify click On action for Profile ended");
        return homePage;
    }




    //Click back button Action from Program and land on Home page
    public AppHomePage backBTNfromPrograms()
    {
        logger.info("Back Button from Programs clicking started");
        page.getByRole(AriaRole.BANNER).locator("svg").click();
        logger.info("Back Button from Programs clicking ended");
        return homePage;
    }

    //Click back button Action from Projects and land on Home page
    public AppHomePage backBTNfromProject()
    {
        logger.info("Back Button from Project clicking started");
        page.getByRole(AriaRole.BANNER).locator("svg").click();
        logger.info("Back Button from Project clicking ended");
        return homePage;
    }

    //Click back button Action from Observation and land on Home page
    public AppHomePage backBTNfromObservations()
    {
        logger.info("Back Button from Observation clicking started");
        page.getByRole(AriaRole.BUTTON).click();
        logger.info("Back Button from Observation clicking ended");
        return homePage;
    }

    //Click back button Action from Survey and land on Home page
    public AppHomePage backBTNfromSurvey()
    {
        logger.info("Back Button from Survey clicking started");
        page.locator("button").filter(new Locator.FilterOptions().setHasText("arrow_back")).click();
        logger.info("Back Button from Survey clicking ended");
        return homePage;
    }

    //Click back button Action from Reports and land on Home page
    public AppHomePage backBTNfromReports()
    {
        logger.info("Back Button from Reports clicking started");
        page.getByRole(AriaRole.BANNER).locator("path").click();
        logger.info("Back Button from Reports clicking ended");
        return homePage;
    }


}
