package org.shikshalokam.frontend.scp;

import org.shikshalokam.backend.scp.SelfCreationPortalBaseTest;
import org.shikshalokam.uiPageObjects.AppAllPages;
import org.shikshalokam.uiPageObjects.Robot;
import org.testng.annotations.Test;

import static org.shikshalokam.uiPageObjects.PWBasePage.fetchProperty;

public class TestSample {
    @Test(description = "Verifies the UI functionality of logging in and logging out from the app as an user.")
    public void testUISCP() {

        SelfCreationPortalBaseTest.loginToScp("sl.scp.userascontentcreator","sl.scp.passwordforcontentcreator");
    }
}
