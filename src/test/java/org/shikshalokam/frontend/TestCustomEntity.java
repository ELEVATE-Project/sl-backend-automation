package org.shikshalokam.frontend;

import org.json.simple.JSONObject;
import org.shikshalokam.backend.ReadForm;
import org.shikshalokam.backend.UpdateForm;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestCustomEntity {

    public void removeEditProfileCustomEnity() {
        JSONObject object = ReadForm.readForm(fetchProperty("mentor.qa.admin.login.user"), fetchProperty("mentor.qa.admin.login.password"), "editProfile", "editProfileForm", "college", false);
        UpdateForm.updateForm(fetchProperty("mentor.qa.admin.login.user"), fetchProperty("mentor.qa.admin.login.password"), object);
    }

    public void addEditProfileCustomEnity() {
        JSONObject object = ReadForm.readForm(fetchProperty("mentor.qa.admin.login.user"), fetchProperty("mentor.qa.admin.login.password"), "editProfile", "editProfileForm", "college", true);
        UpdateForm.updateForm(fetchProperty("mentor.qa.admin.login.user"), fetchProperty("mentor.qa.admin.login.password"), object);
    }

    @Test(description = "Removing the custom Entity from Profile and verifying it.", dependsOnMethods = {"testEditProfileFormWithAddedCustomEntity"})
    public void testEditProfileFormWithRemovedCustomEntity() {
        removeEditProfileCustomEnity();
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("mentor.qa.admin.login.user"), fetchProperty("mentor.qa.admin.login.password"));
        robot.sees(AppAllPages.welcomePage).profile();
        robot.sees(AppAllPages.profileDetailsPage).editProfile();
        robot.sees(AppAllPages.profileDetailsPage).verifyRemovedCustomEntity("college");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
    }

    @Test(description = "Adding the custom Entity to Profile form and verifying it.")
    public void testEditProfileFormWithAddedCustomEntity() {
        addEditProfileCustomEnity();
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("mentor.qa.admin.login.user"), fetchProperty("mentor.qa.admin.login.password"));
        robot.sees(AppAllPages.welcomePage).profile();
        robot.sees(AppAllPages.profileDetailsPage).editProfile();
        robot.sees(AppAllPages.profileDetailsPage).verifyAddedCustomEntity("college");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
    }

    //-------------------ManagersSessionForm----------------------------//
    public void removeManagersSessionCustomEntity() {
        JSONObject object = ReadForm.readForm(fetchProperty("mentor.qa.admin.login.user"), fetchProperty("mentor.qa.admin.login.password"), "managersSession", "managersSessionForm", "location", false);
        UpdateForm.updateForm(fetchProperty("mentor.qa.admin.login.user"), fetchProperty("mentor.qa.admin.login.password"), object);
    }

    public void addManagersSessionCustomEntity() {
        JSONObject object = ReadForm.readForm(fetchProperty("mentor.qa.admin.login.user"), fetchProperty("mentor.qa.admin.login.password"), "managersSession", "managersSessionForm", "location", true);
        UpdateForm.updateForm(fetchProperty("mentor.qa.admin.login.user"), fetchProperty("mentor.qa.admin.login.password"), object);
    }

    @Test(description = "Removing the custom Entity from managersSession form and verifying it.", dependsOnMethods = {"testManagersSessionFormWithAddedCustomEntity"})
    public void testManagersSessionFormWithRemovedCustomEntity() {
        removeManagersSessionCustomEntity();
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("mentor.qa.admin.login.user"), fetchProperty("mentor.qa.admin.login.password"));
        robot.sees(AppAllPages.welcomePage).workspace();
        robot.sees(AppAllPages.workspacePage).manageSession();
        robot.sees(AppAllPages.welcomePage).createSession();
        robot.sees(AppAllPages.profileDetailsPage).verifyRemovedCustomEntity("location");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
    }

    @Test(description = "Adding the custom Entity to managersSession form and verifying it.")
    public void testManagersSessionFormWithAddedCustomEntity() {
        addManagersSessionCustomEntity();
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("mentor.qa.admin.login.user"), fetchProperty("mentor.qa.admin.login.password"));
        robot.sees(AppAllPages.welcomePage).workspace();
        robot.sees(AppAllPages.workspacePage).manageSession();
        robot.sees(AppAllPages.welcomePage).createSession();
        robot.sees(AppAllPages.profileDetailsPage).verifyAddedCustomEntity("location");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
    }

    //-------------------MentorsSessionForm----------------------------//
    public void removeMentorsSessionCustomEntity() {
        JSONObject object = ReadForm.readForm(fetchProperty("mentor.qa.admin.login.user"), fetchProperty("mentor.qa.admin.login.password"), "session", "sessionForm", "grade", false);
        UpdateForm.updateForm(fetchProperty("mentor.qa.admin.login.user"), fetchProperty("mentor.qa.admin.login.password"), object);
    }

    public void addMentorsSessionCustomEntity() {
        JSONObject object = ReadForm.readForm(fetchProperty("mentor.qa.admin.login.user"), fetchProperty("mentor.qa.admin.login.password"), "session", "sessionForm", "grade", true);
        UpdateForm.updateForm(fetchProperty("mentor.qa.admin.login.user"), fetchProperty("mentor.qa.admin.login.password"), object);
    }

    @Test(description = "Removing the custom Entity from session form and verifying it.", dependsOnMethods = {"testMentorsSessionFormWithAddedCustomEntity"})
    public void testMentorsSessionFormWithRemovedCustomEntity() {
        removeMentorsSessionCustomEntity();
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("sl.mentor.user"), fetchProperty("sl.mentor.password"));
        robot.sees(AppAllPages.welcomePage).myMentoringSessionTab();
        robot.sees(AppAllPages.welcomePage).createSession();
        robot.sees(AppAllPages.profileDetailsPage).verifyRemovedCustomEntity("grade");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
    }

    @Test(description = "Adding the custom Entity to session form and verifying it.")
    public void testMentorsSessionFormWithAddedCustomEntity() {
        addMentorsSessionCustomEntity();
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("sl.mentor.user"), fetchProperty("sl.mentor.password"));
        robot.sees(AppAllPages.welcomePage).myMentoringSessionTab();
        robot.sees(AppAllPages.welcomePage).createSession();
        robot.sees(AppAllPages.profileDetailsPage).verifyAddedCustomEntity("grade");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
    }
}