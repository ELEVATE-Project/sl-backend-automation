package org.shikshalokam.frontend.ep;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestSurvey
{
    private static final Logger logger = LogManager.getLogger(TestSurvey.class);


    @Test( description = "Verify All Entity are accessible")
    public void AccessProgram() {
        logger.info("Logging into the application :");
        Robot robot = new Robot();
        robot.openApp(PropertyLoader.PROP_LIST.getProperty("saas.portal.url"));
        robot.sees(AppAllPages.eploginpage).logIntoPortal(fetchProperty("ep.username"), fetchProperty("ep.password"));
        robot.sees(AppAllPages.survey).verifySurvey();

    }
}
