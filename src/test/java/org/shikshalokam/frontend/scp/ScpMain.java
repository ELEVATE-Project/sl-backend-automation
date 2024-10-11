package org.shikshalokam.frontend.scp;

import org.shikshalokam.backend.scp.SelfCreationPortalBaseTest;
import org.testng.annotations.Test;

public class ScpMain {
    @Test(description = "Verifies the UI functionality of logging in and logging out from the app as an user.")
    public void testUISCP() {

        SelfCreationPortalBaseTest.loginToScp("sl.scp.userascontentcreator","sl.scp.passwordforcontentcreator");
    }
}
