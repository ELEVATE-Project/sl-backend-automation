package org.shikshalokam.frontend.ep;

import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestEPLogin {
    @Test(priority = 1, description = "Verify log in page working properly.")
    public void testLoginActivity() {
        Robot robot = new Robot();
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal();
        robot.sees(AppAllPages.homePage).verifyHomePage();
        robot.quitAppBrowser();
    }

    @Test(priority = 2, description = "Verify log in page with Username and password working properly.")
    public void testLoginWithUserName() {
        Robot robot = new Robot();
        String userName = fetchProperty("ep.username");
        String password = fetchProperty("ep.password");
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).testLoginWithMultipleOptions(userName,password,"Username");
        robot.sees(AppAllPages.homePage).verifyHomePage();
        robot.quitAppBrowser();
    }
    @Test(priority = 3, description = "Verify log in page with Name and password working properly.")
    public void testLoginWithPhoneNumber() {
        Robot robot = new Robot();
        String userName = fetchProperty("ep.phoneNumber");
        String password = fetchProperty("ep.password");
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).testLoginWithMultipleOptions(userName,password,"Phone Number");
        robot.sees(AppAllPages.homePage).verifyHomePage();
        robot.quitAppBrowser();
    }

//Negative test cases for login with wrong credentials
    @Test(priority = 4, description = "Verify log in page with Name and password working.")
    public void testLoginWithName() {
        Robot robot = new Robot();
        String userName = fetchProperty("ep.nameoftheuser");
        String password = fetchProperty("ep.password");
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).testLoginWithMultipleOptions(userName,password,"Negative test");
        robot.quitAppBrowser();
    }

    @Test(priority = 5, description = "Verify log in page with Wrong Phone Number and password.")
    public void testLoginWithWrongPhoneNumber() {
        Robot robot = new Robot();
        String password = fetchProperty("ep.password");
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).testLoginWithMultipleOptions("9999999991",password,"Negative test");
        robot.quitAppBrowser();
    }
    @Test(priority = 6, description = "Verify log in page with Empty User Name.")
    public void testLoginWithEmptyUserName() {
        Robot robot = new Robot();
        String password = fetchProperty("ep.password");
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage)
                .testLoginWithMultipleOptions("", password, "Negative test");
        robot.quitAppBrowser();
    }

    @Test(priority = 7, description = "Verify log in page with Empty Password.")
    public void testLoginWithEmptyPassword() {
        Robot robot = new Robot();
        String userName = fetchProperty("ep.username");
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage)
                .testLoginWithMultipleOptions(userName, "", "Negative test");
        robot.quitAppBrowser();
    }

    @Test(priority = 8, description = "Verify log in page with Empty User Name and Password.")
    public void testLoginWithEmptyCredentials() {
        Robot robot = new Robot();
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage)
                .testLoginWithMultipleOptions("", "", "Negative test");
        robot.quitAppBrowser();
    }

    @Test(priority = 9, description = "Verify log in page with Wrong User Name and Wrong Password.")
    public void testLoginWithInvalidCredentials() {
        Robot robot = new Robot();
        String userName = fetchProperty("ep.username");
        String password = fetchProperty("ep.password");
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage)
                .testLoginWithMultipleOptions(userName + "1", password + "1", "Negative test");
        robot.quitAppBrowser();
    }

    @Test(priority = 10, description = "Verify log in page with Short Phone Number.")
    public void testLoginWithShortPhoneNumber() {
        Robot robot = new Robot();
        String password = fetchProperty("ep.password");
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage)
                .testLoginWithMultipleOptions("12345", password, "Negative test");
        robot.quitAppBrowser();
    }

    @Test(priority = 11, description = "Verify log in page with Long Phone Number.")
    public void testLoginWithLongPhoneNumber() {
        Robot robot = new Robot();
        String password = fetchProperty("ep.password");
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage)
                .testLoginWithMultipleOptions("123456789012345", password, "Negative test");
        robot.quitAppBrowser();
    }

    @Test(priority = 12, description = "Verify log in page with User Name and Wrong password.")
    public void testLoginWithWrongPassword() {
        Robot robot = new Robot();
        String userName = fetchProperty("ep.username");
        String password = fetchProperty("ep.password");
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).testLoginWithMultipleOptions(userName+"1",password,"Negative test");
        robot.quitAppBrowser();
    }
    @Test(priority = 13, description = "Verify log in page with Wrong email and password.")
    public void testLoginWithWrongUserName() {
        Robot robot = new Robot();
        String userName = fetchProperty("ep.mail");
        String password = fetchProperty("ep.password");
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).testLoginWithMultipleOptions(userName,password+"1","Negative test");
        robot.quitAppBrowser();
    }
}