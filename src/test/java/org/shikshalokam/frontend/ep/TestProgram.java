package org.shikshalokam.frontend.ep;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;
import org.shikshalokam.frontend.ep.TestEPLogin;
import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestProgram
{
    private static final Logger logger = LogManager.getLogger(TestProgram.class);
//    TestEPLogin TestEPLogin= new TestEPLogin();



    @Test( description = "Verify All Entity are accessible")
    public void AccessProgram()
    {
        logger.info("Logging into the application :");
        Robot robot = new Robot();
        robot.sees(AppAllPages.eploginpage).openURL();
        robot.sees(AppAllPages.eploginpage).logIntoPortal(fetchProperty("ep.username"), fetchProperty("ep.password"));

        robot.sees(AppAllPages.program).verifyProgram();
//        robot.sees(AppAllPages.Program).verifyProjectsInProgram();
//        robot.sees(AppAllPages.Program).verifyObsWithRubInProgram();
//        robot.sees(AppAllPages.Program).verifyObsWithOutRubInProgram();
//        robot.sees(AppAllPages.Program).verifySurveyInProgram();


    }
}
