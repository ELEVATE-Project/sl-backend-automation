package org.shikshalokam.frontend;

import org.json.simple.JSONObject;
import org.shikshalokam.backend.ReadForm;

import org.shikshalokam.backend.UpdateForm;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;


public class TestCustomEntity {

    //------------------------EditProfileForm-------------------//
    public void removeEditProfileCustomEnity() {
        JSONObject object = ReadForm.readForm(fetchProperty("mentor.qa.admin.login.user"),
                fetchProperty("mentor.qa.admin.login.password"), "editProfile", "editProfileForm", "college", false);
        UpdateForm.updateForm("adithya@shikshalokam.org", "Welcome@123", object);
    }

    public void addEditProfileCustomEnity() {
        JSONObject object = ReadForm.readForm(fetchProperty("mentor.qa.admin.login.user"),
                fetchProperty("mentor.qa.admin.login.password"), "editProfile", "editProfileForm", "college", true);
        UpdateForm.updateForm("adithya@shikshalokam.org", "Welcome@123", object);
    }

    @Test(dependsOnMethods = {"testEditProfileFormWithAddedCustomEntity"})
    public void testEditProfileFormWithRemovedCustomEntity() {
        removeEditProfileCustomEnity();
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("mentor.qa.admin.login.user"),
                fetchProperty("mentor.qa.admin.login.password"));
        robot.sees(AppAllPages.welcomePage).profile();
        robot.sees(AppAllPages.profileDetailsPage).editProfile();
        robot.sees(AppAllPages.profileDetailsPage).verifyRemovedCustomEntity("college");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
    }

    @Test
    public void testEditProfileFormWithAddedCustomEntity() {
        addEditProfileCustomEnity();
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("mentor.qa.admin.login.user"),
                fetchProperty("mentor.qa.admin.login.password"));
        robot.sees(AppAllPages.welcomePage).profile();
        robot.sees(AppAllPages.profileDetailsPage).editProfile();
        robot.sees(AppAllPages.profileDetailsPage).verifyAddedCustomEntity("college");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
    }

    //-------------------ManagersSessionForm----------------------------//
    public void removeManagersSessionCustomEnity() {
        JSONObject object = ReadForm.readForm(fetchProperty("mentor.qa.admin.login.user"),
                fetchProperty("mentor.qa.admin.login.password"), "managersSession", "managersSessionForm", "Location", false);
        UpdateForm.updateForm("adithya@shikshalokam.org", "Welcome@123", object);
    }

    public void addManagersSessionCustomEnity() {
        JSONObject object = ReadForm.readForm(fetchProperty("mentor.qa.admin.login.user"),
                fetchProperty("mentor.qa.admin.login.password"), "managersSession", "managersSessionForm", "Location", true);
        UpdateForm.updateForm("adithya@shikshalokam.org", "Welcome@123", object);
    }

    @Test(dependsOnMethods = {"testManagersSessionFormWithAddedCustomEntity"})
    public void testManagersSessionFormWithRemovedCustomEntity() {
        removeManagersSessionCustomEnity();
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("mentor.qa.admin.login.user"),
                fetchProperty("mentor.qa.admin.login.password"));
        robot.sees(AppAllPages.welcomePage).workspace();
        robot.sees(AppAllPages.workspacePage).manageSession();
        robot.sees(AppAllPages.welcomePage).createSession();
        robot.sees(AppAllPages.profileDetailsPage).verifyRemovedCustomEntity("Location");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
    }

    @Test
    public void testManagersSessionFormWithAddedCustomEntity() {
        addManagersSessionCustomEnity();
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("mentor.qa.admin.login.user"),
                fetchProperty("mentor.qa.admin.login.password"));
        robot.sees(AppAllPages.welcomePage).workspace();
        robot.sees(AppAllPages.workspacePage).manageSession();
        robot.sees(AppAllPages.welcomePage).createSession();
        robot.sees(AppAllPages.profileDetailsPage).verifyAddedCustomEntity("Location");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
    }

    //-------------------MentorsSessionForm----------------------------//
    public void removeMentorsSessionCustomEnity() {
        JSONObject object = ReadForm.readForm(fetchProperty("mentor.qa.admin.login.user"),
                fetchProperty("mentor.qa.admin.login.password"), "session", "sessionForm", "GRADE", false);
        UpdateForm.updateForm("adithya@shikshalokam.org", "Welcome@123", object);
    }

    public void addMentorsSessionCustomEnity() {
        JSONObject object = ReadForm.readForm(fetchProperty("mentor.qa.admin.login.user"),
                fetchProperty("mentor.qa.admin.login.password"), "session", "sessionForm", "GRADE", true);
        UpdateForm.updateForm("adithya@shikshalokam.org", "Welcome@123", object);
    }

    @Test(dependsOnMethods = {"testMentorsSessionFormWithAddedCustomEntity"})
    public void testMentorsSessionFormWithRemovedCustomEntity() {
        removeMentorsSessionCustomEnity();
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("sl.mentor.user"),
                fetchProperty("sl.mentor.password"));
        robot.sees(AppAllPages.welcomePage).myMentoringSessionTab();
        robot.sees(AppAllPages.welcomePage).createSession();
        robot.sees(AppAllPages.profileDetailsPage).verifyRemovedCustomEntity("GRADE");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
    }

    @Test
    public void testMentorsSessionFormWithAddedCustomEntity() {
        addMentorsSessionCustomEnity();
        Robot robot = new Robot();
        robot.openApp();
        robot.sees(AppAllPages.loginPage).loginToApp(fetchProperty("sl.mentor.user"),
                fetchProperty("sl.mentor.password"));
        robot.sees(AppAllPages.welcomePage).myMentoringSessionTab();
        robot.sees(AppAllPages.welcomePage).createSession();
        robot.sees(AppAllPages.profileDetailsPage).verifyAddedCustomEntity("GRADE");
        robot.sees(AppAllPages.welcomePage).logOutFromApp();
        robot.quitAppBrowser();
    }

}


