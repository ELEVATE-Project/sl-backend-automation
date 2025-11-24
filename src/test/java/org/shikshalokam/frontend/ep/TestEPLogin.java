package org.shikshalokam.frontend.ep;

//import org.shikshalokam.backend.PropertyLoader;
//import org.shikshalokam.backend.elevateUtility.DeleteUserSAAS;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestEPLogin
{

    @Test(description = "Verify log in page working properly.")
    public void testLoginActivity()
    {
        Robot robot = new Robot();

//        String userId = DeleteUserSAAS.User_ID;
//        System.out.println(userId);
        robot.openApp(fetchProperty("ep.url"));
        robot.sees(AppAllPages.eploginpage).logIntoPortal(fetchProperty("ep.mail"), fetchProperty("ep.password"));
        robot.sees(AppAllPages.homePage).verifyHomePage();
//        robot.debugAPP();

    }
}