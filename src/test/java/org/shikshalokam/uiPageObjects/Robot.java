package org.shikshalokam.uiPageObjects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.PropertyLoader;

public class Robot {

    public  void openApp()
    {
        PWBasePage.page.navigate(PropertyLoader.PROP_LIST.getProperty("mentor.qa.portal.url"));
    }
    private static final Logger logger = LogManager.getLogger(Robot.class);
    public void quitAppBrowser()
    {
        PWBasePage.browserContext.close();
        PWBasePage.browser.close();
        PWBasePage.playwright.close();

    }

    public void debugAPP()
    {
        PWBasePage.page.pause();
    }

    public <T extends PWBasePage> T sees(T PWBasePage)
    {

        return PWBasePage;
    }

}
