package org.shikshalokam.uiPageObjects;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.shikshalokam.backend.MentorEDBaseTest;
import org.shikshalokam.backend.PropertyLoader;
import com.microsoft.playwright.*;

public class PWBasePage extends MentorEDBaseTest {
    private static final Logger logger = LogManager.getLogger(PWBasePage.class);
    public static BrowserContext browserContext;
    public static Page page;
    public static PWBrowser PWBrowser;
    public static Playwright playwright = Playwright.create();
    public static Browser browser;
    static {

        Boolean headless=true;
        if(PropertyLoader.PROP_LIST.getProperty("mentor.qa.browser.setHeadless").equals("false")){
            headless=false;
        }
        switch (PropertyLoader.PROP_LIST.getProperty("mentor.qa.browser")) {
            case "chromium":
                logger.info("Using Chromium browser for Test suite ");
                PWBasePage.PWBrowser = PWBrowser.chromium;
                browser = playwright.chromium().launch( new BrowserType.LaunchOptions().setHeadless(headless));
                browserContext = browser.newContext();
                page = browserContext.newPage();
                break;
            case "msedge":
                logger.info("Using Edge browser for Test suite ");
                PWBasePage.PWBrowser = PWBrowser.msedge;
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("msedge").setHeadless(headless));
                browserContext = browser.newContext();
                page = browserContext.newPage();
                break;
            case "firefox":
                logger.info("Using firefox browser for Test suite");
                PWBasePage.PWBrowser = PWBrowser.firefox;
                browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                browserContext = browser.newContext();
                page = browserContext.newPage();
                break;
            case "webkit":
                logger.info("Using Safri browser for Test suite");
                PWBasePage.PWBrowser = PWBrowser.webkit;
                browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(headless));
                browserContext = browser.newContext();
                page = browserContext.newPage();
                break;
            // Add cases for other supported browser types as needed
            default:
                logger.info("Unsupported browser type for Test suite hence terminating the suite runs ");
                System.exit(1);
        }
    }


}





