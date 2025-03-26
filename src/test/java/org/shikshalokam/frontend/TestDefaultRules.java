package org.shikshalokam.frontend;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.shikshalokam.backend.MentorEDBaseTest;
import org.shikshalokam.backend.PropertyLoader;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.shikshalokam.uiPageObjects.AppAllPages.createSessionPage;

public class TestDefaultRules extends MentorEDBaseTest {
    public static final Logger logger = LogManager.getLogger(TestDefaultRules.class);
    static String ProfileID;
    static String SessionID;

    @BeforeMethod
    public void login() {
        loginToMentorED(PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.OrgAdmin.email"), PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.OrgAdmin.password"));
    }

    @Test(description = "Test case to verify the creating and reading the default rules for profile", priority = 1)
    public void testCreateDefaultRulesForProfile() {
        Robot robot = new Robot();
        robot.openApp();
        logger.info("logging in with org admin credentials");
        loginToMentorED(PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.OrgAdmin.email"), PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.OrgAdmin.password"));
        Response response = createDefaultRules("mentor", "designation", false, "designation", "overlap");
        Assert.assertEquals(response.getStatusCode(), 201);
        ProfileID = response.jsonPath().getString("result.id");
        Response responseRead = readDefaultRules();
        Assert.assertEquals(responseRead.getStatusCode(), 200, "Status code incorrect");
        Assert.assertEquals(responseRead.jsonPath().getString("result.data[0].id"), ProfileID, "The Default rule is not created");
        robot.sees(AppAllPages.loginPage).loginToApp(PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentor.email"), PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentor.password"));
        robot.sees(AppAllPages.welcomePage).profile();
        robot.sees(AppAllPages.profileDetailsPage).editProfile().updateProfileForDefaultSearchRuleCreate("Block education officer", "Default rules mentor");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentee.email"), PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentee.password"));
        robot.sees(AppAllPages.welcomePage).profile();
        robot.sees(AppAllPages.profileDetailsPage).editProfile().updateProfileForDefaultSearchRuleCreate("Block education officer", "Default rules mentee");
        robot.sees(AppAllPages.welcomePage).mentors();
        robot.sees(AppAllPages.mentorsPage).searchMentorForDefaultRules("Default rules mentor", "Block education officer");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
        logger.info("The verification of creating and reading of default rules for profile is completed ");
    }

    @Test(description = "Test case to verify the updating, reading  and deletion flow of default rules for profile", priority = 2)
    public void testUpdateDefaultRulesForProfile() {
        Response response = updateDefaultRules(ProfileID, "mentor", "area_of_expertise", false, "area_of_expertise", "overlap");
        Assert.assertEquals(response.getStatusCode(), 202);
        Assert.assertEquals(response.jsonPath().getString("result.target_field"), "[area_of_expertise]");
        Response responseRead = readDefaultRules();
        Assert.assertEquals(responseRead.getStatusCode(), 200, "Status code incorrect");
        Assert.assertEquals(responseRead.jsonPath().getString("result.data[0].id"), ProfileID, "The Default rule is not created");
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentor.email"), PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentor.password"));
        robot.sees(AppAllPages.welcomePage).profile();
        robot.sees(AppAllPages.profileDetailsPage).editProfile().updateProfileForDefaultSearchRuleUpdate("Communication", "Default rules mentor");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentee.email"), PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentee.password"));
        robot.sees(AppAllPages.welcomePage).profile();
        robot.sees(AppAllPages.profileDetailsPage).editProfile().updateProfileForDefaultSearchRuleUpdate("Communication", "Default rules mentee");
        robot.sees(AppAllPages.welcomePage).mentors();
        robot.sees(AppAllPages.mentorsPage).searchMentorForDefaultRules("Default rules mentor", "Communication");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        deleteDefaultRules(ProfileID);
        robot.quitAppBrowser();
        logger.info("The verification of updating, reading and deleting flows of default rules for profile is completed ");
    }

    @Test(description = "Test case to verify the creating and reading the default rules for session", priority = 3)
    public void testCreateDefaultRulesForSession() {
        Response response = createDefaultRules("session", "Recommended_for", false, "designation", "overlap");
        Assert.assertEquals(response.getStatusCode(), 201);
        SessionID = response.jsonPath().getString("result.id");
        Response responseRead = readDefaultRules();
        Assert.assertEquals(responseRead.getStatusCode(), 200, "Status code incorrect");
        Assert.assertEquals(responseRead.jsonPath().getString("result.data[0].id"), SessionID, "The Default rule is not created");
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentor.email"),
                PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentor.password"));
        robot.sees(AppAllPages.welcomePage).myMentoringSessionTab();
        robot.sees(AppAllPages.welcomePage).createSession();
        robot.sees(createSessionPage).bbbSessionCreation();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentee.email"),
                PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentee.password"));
        robot.quitAppBrowser();
        logger.info("The verification of creating and reading default rules for session is completed ");
    }

    @Test(description = "Test case to verify the updating, reading and deleting flows of default rules for session", priority = 4)
    public void testUpdateDefaultRulesForSession() {
        Response response = updateDefaultRules(SessionID, "session", "categories", false, "area_of_expertise", "overlap");
        Assert.assertEquals(response.getStatusCode(), 202);
        Assert.assertEquals(response.jsonPath().getString("result.target_field"), "[categories]");
        Response responseRead = readDefaultRules();
        Assert.assertEquals(responseRead.getStatusCode(), 200, "Status code incorrect");
        Assert.assertEquals(responseRead.jsonPath().getString("result.data[0].id"), SessionID, "The Default rule is not created");
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentee.email"),
                PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentee.password"));
        robot.sees(AppAllPages.welcomePage).sessionSearch(createSessionPage.bbbSessionTitle);
        robot.sees(AppAllPages.sessionDeatilsPage).enrollSession();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentor.email"),
                PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentor.password"));
        robot.sees(AppAllPages.welcomePage).myMentoringSessionTab();
        robot.sees(AppAllPages.welcomePage).selectcreatedSession(createSessionPage.bbbSessionTitle);
        robot.sees(AppAllPages.sessionDeatilsPage).startSession();
        robot.sees(AppAllPages.sessionDeatilsPage).bbbSessionOptions();
        robot.sees(AppAllPages.sessionDeatilsPage).leaveSession();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentee.email"),
                PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentee.password"));
        robot.sees(AppAllPages.welcomePage).sessionSearch(createSessionPage.bbbSessionTitle);
        robot.sees(AppAllPages.sessionDeatilsPage).joinSession();
        robot.sees(AppAllPages.sessionDeatilsPage).bbbSessionOptions();
        robot.sees(AppAllPages.sessionDeatilsPage).leaveSession();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentor.email"),
                PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentor.password"));
        robot.sees(AppAllPages.welcomePage).myMentoringSessionTab();
        robot.sees(AppAllPages.welcomePage).selectcreatedSession(createSessionPage.bbbSessionTitle);
        robot.sees(AppAllPages.sessionDeatilsPage).startSession();
        robot.sees(AppAllPages.sessionDeatilsPage).bbbSessionOptions();
        robot.sees(AppAllPages.sessionDeatilsPage).terminateSession();
        robot.sees(AppAllPages.sessionDeatilsPage).submitFeedbackMentor();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.sees(AppAllPages.loginPage).loginToApp(PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentee.email"),
                PropertyLoader.PROP_LIST.getProperty("qa.defaultrules.login.mentee.password"));
        robot.sees(AppAllPages.sessionDeatilsPage).submitFeedbackMentee();
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        deleteDefaultRules(SessionID);
        robot.quitAppBrowser();
        logger.info("The verification of updating, reading and deleting flows of default rules for session is completed ");
    }
}